package run.tere.plugin.battleroyale.guns.reloads;

import com.google.gson.Gson;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.AmmoUtils;
import run.tere.plugin.battleroyale.apis.GunUtils;
import run.tere.plugin.battleroyale.consts.ItemStackWithSlot;
import run.tere.plugin.battleroyale.guns.Gun;
import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.UUID;

public class ReloadTask {
    private UUID uuid;
    private Gun gun;
    private BukkitRunnable bukkitRunnable;

    public ReloadTask(UUID uuid, Gun gun) {
        this.uuid = uuid;
        this.gun = gun;
        this.bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                Inventory inventory = getPlayerByUUID().getInventory();
                AmmoType ammoType = gun.getAmmoType();
                ItemStackWithSlot itemStackWithSlot = GunUtils.getGunFromInventory(getPlayerByUUID().getInventory(), gun);
                int inventoryAmmoAmount = AmmoUtils.getAmmoAmount(inventory, ammoType);
                int maxAmmo = gun.getMaxAmmo();
                int ignoreIndex = itemStackWithSlot.getSlot();
                if (gun.getNowAmmo() == 0) {
                    if (AmmoUtils.hasAmmoAmount(inventory, ammoType, maxAmmo)) {
                        AmmoUtils.addAmmoAmount(inventory, ammoType, -maxAmmo, ignoreIndex);
                        gun.setNowAmmo(maxAmmo);
                    } else {
                        AmmoUtils.addAmmoAmount(inventory, ammoType, -inventoryAmmoAmount, ignoreIndex);
                        gun.setNowAmmo(inventoryAmmoAmount);
                    }
                } else {
                    int calcedAmmo = gun.getMaxAmmo() - gun.getNowAmmo();
                    if (AmmoUtils.hasAmmoAmount(inventory, ammoType, calcedAmmo)) {
                        AmmoUtils.addAmmoAmount(inventory, ammoType, -calcedAmmo, ignoreIndex);
                        gun.setNowAmmo(maxAmmo);
                    } else {
                        AmmoUtils.addAmmoAmount(inventory, ammoType, -inventoryAmmoAmount, ignoreIndex);
                        gun.setNowAmmo(inventoryAmmoAmount);
                    }
                }
                gun.setReloading(false);
                Gson gson = new Gson();
                ItemStack itemStack = itemStackWithSlot.getItemStack();
                itemStack = NBTEditor.set(itemStack, gson.toJson(gun), "Gun");

                inventory.setItem(ignoreIndex, itemStack);
                getPlayerByUUID().getWorld().playSound(getPlayerByUUID().getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1F, 0.75F);
                getPlayerByUUID().sendMessage("§a§oリロードが完了しました");
            }
        };
        this.bukkitRunnable.runTaskLater(BattleRoyale.getPlugin(), gun.getReloadLength());
    }

    public UUID getUUID() {
        return uuid;
    }

    public BukkitRunnable getBukkitRunnable() {
        return bukkitRunnable;
    }

    public void cancel() {
        this.bukkitRunnable.cancel();
        Player player = getPlayerByUUID();
        Gson gson = new Gson();
        ItemStackWithSlot itemStackWithSlot = GunUtils.getGunFromInventory(player.getInventory(), gun);
        ItemStack itemStack = itemStackWithSlot.getItemStack();
        int slot = itemStackWithSlot.getSlot();
        gun.setReloading(false);
        itemStack = NBTEditor.set(itemStack, gson.toJson(gun), "Gun");
        player.sendMessage("§c§oリロードがキャンセルされました");
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1F, 1F);
        player.getInventory().setItem(slot, itemStack);
        BattleRoyale.getGameHandler().getGunHandler().getReloadHandler().getReloadTasks().remove(this);
    }

    private Player getPlayerByUUID() {
        Player player = Bukkit.getPlayer(this.uuid);
        return player;
    }
}
