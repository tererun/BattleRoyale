package run.tere.plugin.battleroyale.apis;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import run.tere.plugin.battleroyale.BattleRoyale;

public class HealthUtils {
    public static void setHealth(LivingEntity player, double health, String damageCause) {
        double calcedHealth = health;
        if (player.isInvulnerable()) return;
        if (calcedHealth < 0) {
            calcedHealth = 0;
        }
        if (calcedHealth <= 0) {
            if (player instanceof Player) {
                Player p = (Player) player;
                p.setHealth(20);
                p.setGameMode(GameMode.SPECTATOR);
                p.getInventory().clear();
                EntityDamageEvent entityDamageEvent = p.getLastDamageCause();
                if (entityDamageEvent != null) {
                    if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entityDamageEvent;
                        Entity entity = entityDamageByEntityEvent.getEntity();
                        p.sendTitle("§c§o撃破されました", "§7死因 " + entity.getName() + " による " + damageCause, 5, 60, 5);
                        if (entity instanceof Player) {
                            Player damagerPlayer = (Player) entity;
                            damagerPlayer.playSound(damagerPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 2F);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    damagerPlayer.sendTitle("", "§7§o" + p.getName() + " を撃破!", 2, 20, 2);
                                    Bukkit.broadcastMessage("§a" + damagerPlayer.getName() + " §fが §b" + damageCause + " §fを使用して §c" + p.getName() + " §fを撃破!");
                                }
                            }.runTaskLater(BattleRoyale.getPlugin(), 5L);
                        }
                    }
                }
                return;
            }
        }
        player.setHealth(calcedHealth);
    }

    public static void addHealth(LivingEntity player, double health, String damageCause) {
        setHealth(player, getHealth(player) + health, damageCause);
        if (health < 0) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1F, 1F);
            player.getWorld().spawnParticle(Particle.BLOCK_DUST, player.getLocation().clone().add(0, 1, 0), 50, 0, 0, 0, 1, Material.NETHER_WART_BLOCK.createBlockData());
        }
    }

    public static double getHealth(LivingEntity player) {
        return player.getHealth();
    }
}
