package run.tere.plugin.battleroyale.apis;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.ArrayList;
import java.util.List;

public class AmmoUtils {

    public static ItemStack getAmmoStack(AmmoType ammoType, int amount) {
        ItemStack ammoStack = new ItemStack(Material.INK_SAC, amount);
        ItemMeta ammoMeta = ammoStack.getItemMeta();
        ammoMeta.setDisplayName(ammoType.getName());
        ammoMeta.setCustomModelData(ammoType.getCustomModelData());
        ammoStack.setItemMeta(ammoMeta);
        ammoStack = NBTEditor.set(ammoStack, ammoType.toString(), "Ammo");
        return ammoStack;
    }

    public static List<ItemStack> getAmmoFromInventory(Inventory inventory, AmmoType ammoType) {
        List<ItemStack> ammoList = new ArrayList<>();
        for (int i=0; i<inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            if (!NBTEditor.contains(itemStack, "Ammo")) continue;
            if (AmmoType.valueOf(NBTEditor.getString(itemStack, "Ammo")).equals(ammoType)) ammoList.add(itemStack);
        }
        return ammoList;
    }

    public static void setAmmoAmount(Inventory inventory, AmmoType ammoType, int amount, int ignoreIndex) {
        for (int i=0; i<inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            if (!NBTEditor.contains(itemStack, "Ammo")) continue;
            if (AmmoType.valueOf(NBTEditor.getString(itemStack, "Ammo")).equals(ammoType)) itemStack.setAmount(0);
        }
        ItemStack ammoStack = getAmmoStack(ammoType, amount);
        inventory.addItem(ammoStack);
        ItemStack indexStack = inventory.getItem(ignoreIndex);
        /*
        System.out.println(ignoreIndex);
        if (indexStack != null) {
            System.out.println(indexStack);
            inventory.addItem(indexStack.clone());
            inventory.setItem(ignoreIndex, new ItemStack(Material.AIR));
        }
         */
    }

    public static void addAmmoAmount(Inventory inventory, AmmoType ammoType, int amount, int ignoreIndex) {
        setAmmoAmount(inventory, ammoType, getAmmoAmount(inventory, ammoType) + amount, ignoreIndex);
    }

    public static boolean hasAmmoAmount(Inventory inventory, AmmoType ammoType, int amount) {
        return (getAmmoAmount(inventory, ammoType) >= amount);
    }

    public static int getAmmoAmount(Inventory inventory, AmmoType ammoType) {
        List<ItemStack> ammoList = getAmmoFromInventory(inventory, ammoType);
        if (ammoList.isEmpty()) return 0;
        int nowAmmoAmount = 0;
        for (ItemStack ammoStack : ammoList) {
            nowAmmoAmount += ammoStack.getAmount();
        }
        return nowAmmoAmount;
    }
}
