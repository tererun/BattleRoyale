package run.tere.plugin.battleroyale.guns.ammos;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.HealthUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AmmoHandler extends TimerTask {
    private Timer timer;
    private List<Ammo> ammos;

    public AmmoHandler() {
        this.timer = new Timer();
        this.timer.schedule(this, 0L, 1L);
        this.ammos = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        if (ammos.isEmpty()) return;
        for (Ammo ammo : ammos) {
            if (ammo.getNowTurn() % ammo.getAmmoSpeed() < 1) {
                if (ammo.getNowTurn() >= ammo.getAmmoSpeed()) {
                    ammo.setNowTurn(0);
                } else {
                    ammo.addNowTurn(1);
                }
                continue;
            }
            UUID uuid = ammo.getUUID();
            String gunName = ammo.getGunName();
            double damage = ammo.getDamage();
            double headShotDamageMagnification = ammo.getHeadShotDamageMagnification();
            double flyingDistance = ammo.getFlyingDistance();
            Location spawnLocation = ammo.getSpawnLocation();
            Location fallingStartLocation = ammo.getFallingStartLocation();
            Location nowLocation = ammo.getNowLocation();
            Vector direction = nowLocation.getDirection();
            double gravity = 0;
            ammo.addNowTurn(1);
            if (spawnLocation.distance(nowLocation) >= flyingDistance) {
                if (fallingStartLocation == null) {
                    ammo.setFallingStartLocation(nowLocation.clone());
                    fallingStartLocation = nowLocation.clone();
                }
                gravity = -0.01 * fallingStartLocation.distance(nowLocation);
            }
            nowLocation.add(direction.getX() / 7.5, direction.getY() / 7.5 + gravity, direction.getZ() / 7.5);
            ammo.setNowLocation(nowLocation);
            Material nowBlockType = nowLocation.getBlock().getType();
            if (nowBlockType.isSolid()) {
                nowLocation.getWorld().spawnParticle(Particle.BLOCK_DUST, nowLocation, 50, 0, 0, 0, 0, nowBlockType.createBlockData());
                nowLocation.getWorld().playSound(nowLocation, nowBlockType.createBlockData().getSoundGroup().getBreakSound(), 1F, 1F);
                ammos.remove(ammo);
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (nowLocation.distance(player.getLocation()) >= 3) {
                    if (spawnLocation.distance(nowLocation) % 2 <= 0.15) nowLocation.getWorld().spawnParticle(Particle.CRIT, nowLocation, 1, 0, 0, 0, 0);
                }
            }

            for (Player livingEntity : Bukkit.getOnlinePlayers()) {
                if (livingEntity.getBoundingBox().contains(nowLocation.getX(), nowLocation.getY(), nowLocation.getZ())) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    Player player = offlinePlayer.getPlayer();
                    double headDistance = livingEntity.getLocation().clone().add(0, livingEntity.getEyeHeight(), 0).distance(nowLocation);
                    double calcedDamage = damage;
                    if (player != null) {
                        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 2F, 0.5F);
                        GameMode gameMode = livingEntity.getGameMode();
                        if ((gameMode.equals(GameMode.SURVIVAL)) || (gameMode.equals(GameMode.ADVENTURE))) {
                            if (headDistance <= 0.4) {
                                calcedDamage *= headShotDamageMagnification;
                                player.sendTitle("", "§7§oHeadshot!              ", 0, 10, 2);
                            }
                            HealthUtils.addHealth(livingEntity, -calcedDamage, gunName);
                            livingEntity.setLastDamageCause(new EntityDamageByEntityEvent(livingEntity, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
                        }
                    }
                    ammos.remove(ammo);
                    return;
                }
            }
        }
    }

    public void addAmmo(Ammo ammo) {
        this.ammos.add(ammo);
    }

    public List<Ammo> getAmmos() {
        return ammos;
    }
}
