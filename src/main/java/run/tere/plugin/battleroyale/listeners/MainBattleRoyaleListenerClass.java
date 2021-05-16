package run.tere.plugin.battleroyale.listeners;

import com.google.gson.Gson;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.GlowAPI;
import run.tere.plugin.battleroyale.apis.GunUtils;
import run.tere.plugin.battleroyale.apis.JsonAPI;
import run.tere.plugin.battleroyale.consts.SelectEntity;
import run.tere.plugin.battleroyale.guns.Gun;
import run.tere.plugin.battleroyale.itemspawns.ItemSpawnLocationHandler;

import java.util.HashSet;
import java.util.UUID;

public class MainBattleRoyaleListenerClass implements Listener {

    private static HashSet<UUID> movingChecker = new HashSet<>();
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (!e.isSneaking()) return;
        if (!player.isOnGround()) return;
        if (!player.isSprinting()) return;
        if (movingChecker.contains(player.getUniqueId())) return;
        player.setVelocity(player.getVelocity().clone().add(player.getLocation().getDirection().clone()).setY(0));
        movingChecker.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                movingChecker.remove(player.getUniqueId());
            }
        }.runTaskLater(BattleRoyale.getPlugin(), 20L);
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i[0] >= 8) {
                    cancel();
                    return;
                }
                Location location = player.getLocation();
                Material underMaterial = location.clone().add(0, -1, 0).getBlock().getType();
                if (!underMaterial.equals(Material.AIR)) {
                    location.getWorld().spawnParticle(Particle.BLOCK_DUST, location, 20, 0, 0, 0, 0, underMaterial.createBlockData());
                    location.getWorld().playSound(location, underMaterial.createBlockData().getSoundGroup().getStepSound(), 1F, 1F);
                }
                i[0]++;
            }
        }.runTaskTimer(BattleRoyale.getPlugin(), 0L, 1L);
    }

    private static HashSet<UUID> moveSoundUUIDs = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        Material underMaterial = location.clone().add(0, -1, 0).getBlock().getType();
        if (e.getTo() == null) return;
        if ((e.getFrom().getX() == e.getTo().getX()) && (e.getFrom().getY() == e.getTo().getY()) && (e.getFrom().getZ() == e.getTo().getZ())) return;
        if (player.isSprinting()) {
            if (!underMaterial.equals(Material.AIR)) {
                if (moveSoundUUIDs.contains(uuid)) return;
                if (underMaterial.equals(Material.WATER)) return;
                if (player.getGameMode().equals(GameMode.SPECTATOR)) return;
                location.getWorld().playSound(location, underMaterial.createBlockData().getSoundGroup().getStepSound(), 1F, 1F);
                moveSoundUUIDs.add(uuid);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        moveSoundUUIDs.remove(uuid);
                    }
                }.runTaskLater(BattleRoyale.getPlugin(), 5L);
            }
        }
    }

    @EventHandler
    public void onGlindMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack chestPlate = player.getInventory().getChestplate();
        if (e.getTo() == null) return;
        if ((player.isOnGround()) && (chestPlate != null) && (chestPlate.getType().equals(Material.ELYTRA))) {
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 64));
            player.getInventory().setItem(7, new ItemStack(Material.COMPASS, 1));
            ItemStack healItem = new ItemStack(Material.SPLASH_POTION, 1);
            PotionMeta healMeta = (PotionMeta) healItem.getItemMeta();
            healMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 10, 3, false, false, false), true);
            healItem.setItemMeta(healMeta);
            player.getInventory().setItem(6, healItem);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
    }

    private static HashSet<SelectEntity> selectEntities = new HashSet<>();

    private SelectEntity getSelectEntityFromUUID(UUID playerUUID) {
        for (SelectEntity selectEntity : selectEntities) {
            if (selectEntity.getPlayer().getUniqueId().equals(playerUUID)) {
                return selectEntity;
            }
        }
        return null;
    }

    @EventHandler
    public void onSelectItemPickup(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        Entity lookingEntity = getLookingEntity(player);
        e.setCancelled(true);
        if ((lookingEntity == null) || (!lookingEntity.getType().equals(EntityType.ARMOR_STAND)) || (!lookingEntity.getScoreboardTags().contains("selectitems"))) return;
        ArmorStand armorStand = (ArmorStand) lookingEntity;
        EntityEquipment equipmentSlot = armorStand.getEquipment();
        if (equipmentSlot == null) return;
        ItemStack helmet = equipmentSlot.getHelmet();
        if (helmet == null) return;
        Inventory inventory = player.getInventory();
        if (NBTEditor.contains(helmet, "GunId")) {
            if (GunUtils.getGunSize(player.getInventory()) >= 2) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                player.sendMessage("§c銃は二個以上持てません");
                return;
            }
            if (inventory.getItem(0) == null) {
                inventory.setItem(0, helmet);
            } else if (inventory.getItem(1) == null) {
                inventory.setItem(1, helmet);
            }
            equipmentSlot.setHelmet(new ItemStack(Material.AIR));
            armorStand.remove();
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1F, 1F);
        } else {
            if (inventory.firstEmpty() <= 1) {
                for (int i=2; i<inventory.getSize(); i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, helmet);
                        break;
                    }
                }
            } else {
                inventory.addItem(helmet);
            }
            equipmentSlot.setHelmet(new ItemStack(Material.AIR));
            armorStand.remove();
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1F, 1F);
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onSelectEntity(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Entity lookingEntity = getLookingEntity(player);
        if ((lookingEntity == null) || (!lookingEntity.getType().equals(EntityType.ARMOR_STAND)) || (!lookingEntity.getScoreboardTags().contains("selectitems"))) {
            removeSelectEntity(player);
            return;
        }
        ArmorStand armorStand = (ArmorStand) lookingEntity;
        SelectEntity selectEntity = getSelectEntityFromUUID(playerUUID);
        if (selectEntity != null) {
            if (selectEntity.getEntity() != null) {
                if (!selectEntity.getEntity().getUniqueId().equals(lookingEntity.getUniqueId())) {
                    ArmorStand prevSelectedEntity = (ArmorStand) selectEntity.getEntity();
                    GlowAPI.setGlowing(prevSelectedEntity, player, false);
                }
            }
        }
        GlowAPI.setGlowing(armorStand, player, true);
        selectEntities.add(new SelectEntity(player, armorStand));
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }

    private void removeSelectEntity(Player player) {
        SelectEntity selectEntity = getSelectEntityFromUUID(player.getUniqueId());
        if (selectEntity != null) {
            ArmorStand armorStand = (ArmorStand) selectEntity.getEntity();
            if (armorStand != null) {
                if (!armorStand.isDead()) {
                    GlowAPI.setGlowing(armorStand, player, false);
                }
            }
            selectEntities.remove(selectEntity);
        }
    }

    public Entity getLookingEntity(Player player) {
        Vector playerLookDir = player.getEyeLocation().getDirection();
        Vector playerEyeLoc = player.getEyeLocation().toVector();
        for (Entity entity : player.getNearbyEntities(2, 2, 2)) {
            Vector entityLoc = entity.getLocation().toVector();
            Vector playerEntityVec = entityLoc.subtract(playerEyeLoc);
            float angle = playerLookDir.angle(playerEntityVec);
            if (angle < 0.2F) {
                return entity;
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if (e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.EATING)) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (clickedInventory == null) return;
        ItemStack clickedItem = clickedInventory.getItem(e.getSlot());
        if (clickedItem == null) return;
        e.setCancelled(true);
        if (clickedItem.getType().equals(Material.BARRIER)) return;
        if (NBTEditor.contains(clickedItem, "GunId")) {
            Gun gun = BattleRoyale.getGameHandler().getGunHandler().getActiveGunFromUUID(UUID.fromString(NBTEditor.getString(clickedItem, "GunId")));
            if (gun.isReloading()) {
                player.sendMessage("§cリロード中は銃を投げることができません!");
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                return;
            }
        }
        if (player.getInventory().equals(clickedInventory)) {
            if (e.isShiftClick()) {
                int amount = clickedItem.getAmount() / 2;
                ItemStack dropItem = clickedItem.clone();
                if (amount == 0) {
                    amount = 1;
                }
                dropItem.setAmount(amount);
                dropItem(player, dropItem);
                clickedItem.setAmount(clickedItem.getAmount() - amount);
            } else {
                dropItem(player, clickedItem);
                clickedItem.setAmount(0);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Location blockLocation = e.getBlock().getLocation();
        Location spawnLocation = new Location(blockLocation.getWorld(), blockLocation.getBlockX() + 0.5, blockLocation.getBlockY(), blockLocation.getBlockZ() + 0.5);
        ItemStack itemStack = e.getItemInHand();
        if (!NBTEditor.contains(itemStack, "SpawnTriggerItem")) return;
        e.setCancelled(true);
        ItemSpawnLocationHandler itemSpawnLocationHandler = BattleRoyale.getGameHandler().getItemSpawnHandler().getItemSpawnLocationHandler();
        itemSpawnLocationHandler.getItemSpawnBukkitLocations().add(spawnLocation);
        JsonAPI.saveItemSpawnLocationHandler(itemSpawnLocationHandler);
    }

    @EventHandler
    public void onPlayerMoveDebug(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!NBTEditor.contains(itemStack, "SpawnTriggerItem")) return;
        for (Location location : BattleRoyale.getGameHandler().getItemSpawnHandler().getItemSpawnLocationHandler().getItemSpawnBukkitLocations()) {
            if (location.distance(player.getLocation()) <= 10) e.getPlayer().spawnParticle(Particle.END_ROD, location.clone().add(0, 0.5, 0), 20, 0, 0.5, 0, 0);
        }
    }

    private void dropItem(Player player, ItemStack itemStack) {
        Vector playerLookVector = player.getLocation().getDirection().clone();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1F, 2F);
        ArmorStand armorStand = player.getWorld().spawn(player.getEyeLocation(), ArmorStand.class, as -> {
            as.setSilent(true);
            as.setInvisible(true);
            as.addScoreboardTag("selectitems");
            as.getEquipment().setHelmet(itemStack.clone());
            as.setVelocity(playerLookVector);
        });
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStand.isDead()) {
                    cancel();
                    return;
                }
                if (i[0] >= 6000) {
                    armorStand.setMarker(true);
                    cancel();
                    return;
                }
                if (armorStand.isOnGround()) {
                    armorStand.getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NETHER_BRICKS_BREAK, 1F, 1F);
                    armorStand.setMarker(true);
                    cancel();
                    return;
                }
                i[0]++;
            }
        }.runTaskTimer(BattleRoyale.getPlugin(), 0L, 1L);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        Player player = (Player) e.getEntity();
        player.setNoDamageTicks(20);
        player.setMaximumNoDamageTicks(20);
        if ((damageCause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) || (damageCause.equals(EntityDamageEvent.DamageCause.FALL))) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        player.setHealth(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        EntityDamageEvent entityDamageEvent = player.getLastDamageCause();
        if (entityDamageEvent != null) {
            if (entityDamageEvent.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                e.setDeathMessage("§c" + player.getName() + " §fは §aエリア収縮 §fで撃破されました!");
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!e.getDismounted().getScoreboardTags().contains("pigtrain")) return;
        Player player = (Player) e.getEntity();
        Pig pig = (Pig) e.getDismounted();
        player.getInventory().setChestplate(new ItemStack(Material.ELYTRA, 1));
        player.setGliding(true);
        pig.setSaddle(false);
    }
}
