package run.tere.plugin.battleroyale.consts;

import org.bukkit.inventory.ItemStack;

public class ItemStackWithSlot {
    private ItemStack itemStack;
    private int slot;

    public ItemStackWithSlot(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
