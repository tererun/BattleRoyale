package run.tere.plugin.battleroyale.apis;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.consts.ItemStackWithSlot;
import run.tere.plugin.battleroyale.guns.Gun;
import run.tere.plugin.battleroyale.guns.GunTemplate;

import java.util.UUID;

public class GunUtils {
    public static Gun createGun(GunTemplate gunTemplate) {
        UUID uuid = UUID.randomUUID();
        Gun gun = new Gun(uuid, gunTemplate.getGunName(), gunTemplate.getCustomModelData(), gunTemplate.getAmmoType(), gunTemplate.getMaxAmmo(), gunTemplate.getDamage(), gunTemplate.getHeadShotDamageMagnification(), gunTemplate.getFlyingDistance(), gunTemplate.getLaunchRate(), gunTemplate.getReloadLength(), gunTemplate.getVerticalRecoil(), gunTemplate.getSideRecoil(), gunTemplate.getAmmoSpeed(), gunTemplate.getScopingWalkSpeed());
        BattleRoyale.getGameHandler().getGunHandler().addActiveGun(gun);
        return gun;
    }

    public static ItemStack getGunStack(Gun gun) {
        ItemStack itemStack = new ItemStack(Material.SHIELD, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(gun.getGunName());
        itemMeta.setCustomModelData(gun.getCustomModelData());
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("GENERIC_ATTACK_SPEED", -4, AttributeModifier.Operation.ADD_NUMBER));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        itemStack = NBTEditor.set(itemStack, gun.getUUID().toString(), "GunId");
        return itemStack;
    }

    public static ItemStackWithSlot getGunFromInventory(Inventory inventory, Gun gun) {
        for (int i=0; i<inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            if (!NBTEditor.contains(itemStack, "GunId")) continue;
            UUID gunUUID = UUID.fromString(NBTEditor.getString(itemStack, "GunId"));
            if (gunUUID.equals(gun.getUUID())) {
                return new ItemStackWithSlot(itemStack, i);
            }
        }
        return null;
    }

    public static int getGunSize(Inventory inventory) {
        int size = 0;
        for (int i=0; i<inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            if (!NBTEditor.contains(itemStack, "GunId")) continue;
            size++;
        }
        return size;
    }
}
