package run.tere.plugin.battleroyale.apis;

import com.google.gson.Gson;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import run.tere.plugin.battleroyale.consts.ItemStackWithSlot;
import run.tere.plugin.battleroyale.guns.Gun;

import java.util.UUID;

public class GunUtils {
    public static ItemStack getGunStack(Gun gun) {
        ItemStack itemStack = new ItemStack(Material.SHIELD, 1);
        Gson gson = new Gson();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(gun.getGunName());
        itemMeta.setCustomModelData(gun.getCustomModelData());
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("GENERIC_ATTACK_SPEED", -4, AttributeModifier.Operation.ADD_NUMBER));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        itemStack = NBTEditor.set(itemStack, gson.toJson(gun), "Gun");
        return itemStack;
    }

    public static ItemStackWithSlot getGunFromInventory(Inventory inventory, Gun gun) {
        Gson gson = new Gson();
        for (int i=0; i<inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            if (!NBTEditor.contains(itemStack, "Gun")) continue;
            Gun equalsGun = gson.fromJson(NBTEditor.getString(itemStack, "Gun"), Gun.class);
            if (equalsGun.getUUID().equals(gun.getUUID())) {
                return new ItemStackWithSlot(itemStack, i);
            }
        }
        return null;
    }

    public static Gun getGunFromUUID(Inventory inventory, UUID uuid) {
        Gson gson = new Gson();
        for (int i=0; i<inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            if (!NBTEditor.contains(itemStack, "Gun")) continue;
            Gun equalsGun = gson.fromJson(NBTEditor.getString(itemStack, "Gun"), Gun.class);
            if (equalsGun.getUUID().equals(uuid)) {
                return equalsGun;
            }
        }
        return null;
    }
}
