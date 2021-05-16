package run.tere.plugin.battleroyale.minimaps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;
import run.tere.plugin.battleroyale.BattleRoyale;

import java.util.HashSet;
import java.util.UUID;

public class MinimapHandler implements Listener {

    private HashSet<Minimap> minimaps;

    public MinimapHandler() {
        this.minimaps = new HashSet<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, BattleRoyale.getPlugin());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack offHandItem = inventory.getItem(40);
        if (offHandItem == null) {
            inventory.setItemInOffHand(new ItemStack(Material.FILLED_MAP, 1));
            return;
        }
        if (!offHandItem.getType().equals(Material.FILLED_MAP)) return;
        MapMeta mapMeta = (MapMeta) offHandItem.getItemMeta();
        Minimap minimap = getMinimapFromUUID(player.getUniqueId());
        minimap.updateMinimap();
        mapMeta.setScaling(true);
        mapMeta.setMapView(minimap.getMapView());
        offHandItem.setItemMeta(mapMeta);
    }

    public Minimap getMinimapFromUUID(UUID uuid) {
        for (Minimap minimap : minimaps) {
            if (minimap.getPlayerUUID().equals(uuid)) {
                return minimap;
            }
        }
        Minimap minimap = new Minimap(uuid);
        minimap.createMinimap();
        this.minimaps.add(minimap);
        return minimap;
    }
}