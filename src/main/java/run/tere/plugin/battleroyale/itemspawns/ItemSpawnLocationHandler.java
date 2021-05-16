package run.tere.plugin.battleroyale.itemspawns;

import org.bukkit.Location;

import java.util.HashSet;

public class ItemSpawnLocationHandler {
    private HashSet<ItemSpawnLocation> itemSpawnLocations;

    public ItemSpawnLocationHandler() {
        itemSpawnLocations = new HashSet<>();
    }

    public HashSet<Location> getItemSpawnBukkitLocations() {
        HashSet<Location> locations = new HashSet<>();
        for (ItemSpawnLocation itemSpawnLocation : this.itemSpawnLocations) {
            locations.add(ItemSpawnLocation.convertItemSpawnLocationToBukkitLocation(itemSpawnLocation));
        }
        return locations;
    }

    public HashSet<ItemSpawnLocation> getItemSpawnLocations() {
        return itemSpawnLocations;
    }

    public void setItemSpawnLocations(HashSet<ItemSpawnLocation> itemSpawnLocations) {
        this.itemSpawnLocations = itemSpawnLocations;
    }
}
