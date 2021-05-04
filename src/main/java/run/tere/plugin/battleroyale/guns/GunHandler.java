package run.tere.plugin.battleroyale.guns;

import com.google.gson.Gson;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.AmmoUtils;
import run.tere.plugin.battleroyale.apis.GunUtils;
import run.tere.plugin.battleroyale.guns.ammos.Ammo;
import run.tere.plugin.battleroyale.guns.ammos.AmmoType;
import run.tere.plugin.battleroyale.guns.reloads.ReloadHandler;
import run.tere.plugin.battleroyale.guns.reloads.ReloadTask;

import java.util.*;

public class GunHandler implements Listener {

    private HashSet<Gun> activeGuns;
    private GunList gunList;
    private ReloadHandler reloadHandler;

    public GunHandler() {
        this.activeGuns = new HashSet<>();
        this.gunList = new GunList();
        this.reloadHandler = new ReloadHandler();
        Bukkit.getServer().getPluginManager().registerEvents(this, BattleRoyale.getPlugin());
    }

    public void addActiveGun(Gun gun) {
        this.activeGuns.add(gun);
    }

    public void removeActiveGun(Gun gun) {
        this.activeGuns.remove(gun);
    }

    public Gun getActiveGunFromUUID(UUID uuid) {
        for (Gun gun : this.activeGuns) {
            if (gun.getUUID().equals(uuid)) return gun;
        }
        return null;
    }

    public boolean containsActiveGunFromUUID(UUID uuid) {
        return (this.getActiveGunFromUUID(uuid) != null);
    }

    public boolean containsActiveGun(Gun gun) {
        return this.activeGuns.contains(gun);
    }

    public HashSet<Gun> getActiveGuns() {
        return activeGuns;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerInventory inventory = player.getInventory();
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack itemStack = inventory.getItem(slot);
        if (itemStack == null) return;
        if (!NBTEditor.contains(itemStack, "GunId")) return;
        Gun gun = getActiveGunFromUUID(UUID.fromString(NBTEditor.getString(itemStack, "GunId")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        Action action = e.getAction();
        if ((action.equals(Action.LEFT_CLICK_AIR)) || (action.equals(Action.LEFT_CLICK_BLOCK))) {
            if (reloadingCancelInteractSet.contains(uuid)) {
                reloadingCancelInteractSet.remove(uuid);
                return;
            }
            if (gun.isReloading()) return;
            if (itemMeta.hasCustomModelData()) {
                int nowCustomModelData = itemMeta.getCustomModelData();
                int defaultCustomModelData = gun.getCustomModelData();
                int nextCustomModelData;
                if (nowCustomModelData == defaultCustomModelData) {
                    nextCustomModelData = defaultCustomModelData + 1;
                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("GENERIC_MOVEMENT_SPEED", gun.getScopingWalkSpeed(), AttributeModifier.Operation.ADD_NUMBER));
                } else {
                    nextCustomModelData = defaultCustomModelData;
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
                }
                itemMeta.setCustomModelData(nextCustomModelData);
                itemStack.setItemMeta(itemMeta);
            }
        } else if ((action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK))) {
            if (gun.isLaunched()) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                return;
            }
            if (gun.isReloading()) return;
            gun.setLaunched(true);
            inventory.setItem(slot, itemStack);
            Timer timer = new Timer();
            if (gun.getLaunchRate() < 0) {
                TimerTask timerTask = new SingleGunLaunch(player, gun, inventory, slot, itemStack);
                timer.schedule(timerTask, -gun.getLaunchRate());
                launch(player, gun, itemStack, inventory, timerTask, slot);
            } else {
                TimerTask timerTask = new GunLaunch(player, gun, inventory, slot, itemStack);
                timer.schedule(timerTask, 0L, gun.getLaunchRate());
            }
        }
    }

    private void autoLaunch(Player player, Gun gun, ItemStack itemStack, Inventory inventory, TimerTask timerTask, int slot) {
        if ((!player.isHandRaised())) {
            gun.setLaunched(false);
            timerTask.cancel();
            return;
        }
        launch(player, gun, itemStack, inventory, timerTask, slot);
    }

    private void launch(Player player, Gun gun, ItemStack itemStack, Inventory inventory, TimerTask timerTask, int slot) {
        if (gun.isReloading()) {
            gun.setLaunched(false);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(gun.getCustomModelData());
            itemMeta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot, itemStack);
            timerTask.cancel();
            return;
        }
        if (gun.getNowAmmo() <= 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cマガジン残弾がありません"));
            player.getWorld().playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.75F, 1F);
            return;
        }
        new Ammo(player.getUniqueId(), gun.getGunName(), gun.getAmmoType(), gun.getDamage(), gun.getHeadShotDamageMagnification(), gun.getFlyingDistance(), player.getEyeLocation().clone(), gun.getAmmoSpeed());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 4F, 1F);
        float sideRecoil = gun.getSideRecoil();
        float verticalRecoil = -gun.getVerticalRecoil();
        if (itemStack.getItemMeta().getCustomModelData() == gun.getCustomModelData()) {
            sideRecoil *= 2;
            verticalRecoil *= 2;
        }
        sendRecoilPacket(player, sideRecoil, verticalRecoil);
        gun.addNowAmmo(-1);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(gun.getNowAmmo() + "/" + gun.getMaxAmmo()));
        inventory.setItem(slot, itemStack);
    }

    public class SingleGunLaunch extends TimerTask {
        private Player player;
        private Gun gun;
        private Inventory inventory;
        private int slot;
        private ItemStack itemStack;

        public SingleGunLaunch(Player player, Gun gun, Inventory inventory, int slot, ItemStack itemStack) {
            this.player = player;
            this.gun = gun;
            this.inventory = inventory;
            this.slot = slot;
            this.itemStack = itemStack;
        }

        @Override
        public void run() {
            gun.setLaunched(false);
        }
    }

    public class GunLaunch extends TimerTask {
        private Player player;
        private Gun gun;
        private Inventory inventory;
        private int slot;
        private ItemStack itemStack;

        public GunLaunch(Player player, Gun gun, Inventory inventory, int slot, ItemStack itemStack) {
            this.player = player;
            this.gun = gun;
            this.inventory = inventory;
            this.slot = slot;
            this.itemStack = itemStack;
        }

        @Override
        public void run() {
            autoLaunch(player, gun, itemStack, inventory, this, slot);
        }
    }

    private static HashSet<UUID> reloadingCancelInteractSet = new HashSet<>();

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack dropItem = e.getItemDrop().getItemStack();
        if (!NBTEditor.contains(dropItem, "GunId")) return;
        e.setCancelled(true);
        reloadingCancelInteractSet.add(player.getUniqueId());
        Gun gun = getActiveGunFromUUID(UUID.fromString(NBTEditor.getString(dropItem, "GunId")));
        ItemMeta dropMeta = dropItem.getItemMeta();
        int gunCustomModelData = gun.getCustomModelData();
        if (dropMeta.getCustomModelData() != gunCustomModelData) {
            dropMeta.setCustomModelData(gunCustomModelData);
            dropMeta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
            dropItem.setItemMeta(dropMeta);
        }
        gun.setLaunched(false);
        AmmoType ammoType = gun.getAmmoType();
        int inventoryAmmoAmount = AmmoUtils.getAmmoAmount(inventory, ammoType);
        if (inventoryAmmoAmount <= 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cインベントリに弾薬がありません"));
            return;
        }
        if (gun.isReloading()) {
            player.sendMessage("§c§oリロード中です");
            return;
        }
        if (gun.getNowAmmo() == gun.getMaxAmmo()) {
            player.sendMessage("§c§oマガジンが満タンです");
            return;
        }
        player.sendMessage("§7§oリロードします...");
        player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1F, 0.5F);
        gun.setReloading(true);
        reloadHandler.getReloadTasks().add(new ReloadTask(player.getUniqueId(), gun));
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        ItemStack fromItem = player.getInventory().getItem(e.getPreviousSlot());
        if (fromItem == null) return;
        if (!NBTEditor.contains(fromItem, "GunId")) return;
        Gun gun = getActiveGunFromUUID(UUID.fromString(NBTEditor.getString(fromItem, "GunId")));
        ItemMeta fromMeta = fromItem.getItemMeta();
        int customModelData = gun.getCustomModelData();
        if (gun.isReloading()) {
            ReloadTask reloadTask = reloadHandler.getReloadTaskByUUID(player.getUniqueId());
            if (reloadTask != null) {
                reloadTask.cancel();
            }
        }
        if (fromMeta.getCustomModelData() != customModelData) {
            fromMeta.setCustomModelData(customModelData);
            fromItem.setItemMeta(fromMeta);
        }
    }

    public GunList getGunList() {
        return gunList;
    }

    public ReloadHandler getReloadHandler() {
        return reloadHandler;
    }

    private Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> teleportFlags = new HashSet<>(
            Arrays.asList(PacketPlayOutPosition.EnumPlayerTeleportFlags.X, PacketPlayOutPosition.EnumPlayerTeleportFlags.Y, PacketPlayOutPosition.EnumPlayerTeleportFlags.Z,
                    PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT, PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT));

    public void sendRecoilPacket(Player p, float yaw, float pitch) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        ep.lastYaw = ep.yaw;
        ep.yaw += yaw;
        ep.lastPitch = ep.pitch;
        ep.pitch += pitch;
        PacketPlayOutPosition packet = new PacketPlayOutPosition(0, 0, 0, yaw, pitch, teleportFlags, 0);
        ep.playerConnection.sendPacket(packet);
    }
}
