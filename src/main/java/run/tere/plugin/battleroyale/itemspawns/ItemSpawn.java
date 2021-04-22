package run.tere.plugin.battleroyale.itemspawns;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemSpawn {
    private List<ItemStack> spawnItems;

    public ItemSpawn() {
        this.spawnItems = new ArrayList<>();
    }

    public ItemSpawn(ItemStack...spawnItems) {
        this.spawnItems = new ArrayList<>(Arrays.asList(spawnItems));
    }

    public ItemSpawn(List<ItemStack> spawnItems) {
        this.spawnItems = new ArrayList<>(spawnItems);
    }

    public List<ItemStack> getSpawnItems() {
        return spawnItems;
    }

    public void setSpawnItems(List<ItemStack> spawnItems) {
        this.spawnItems = spawnItems;
    }
}
