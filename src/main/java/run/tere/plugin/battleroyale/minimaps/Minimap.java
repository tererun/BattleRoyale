package run.tere.plugin.battleroyale.minimaps;

import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.WorldMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.map.CraftMapRenderer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import run.tere.plugin.battleroyale.minimaps.renderers.MinimapRenderer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Minimap {
    private UUID playerUUID;
    private MapView mapView;

    public Minimap(UUID uuid) {
        this.playerUUID = uuid;
    }

    public void createMinimap() {
        Player player = Bukkit.getPlayer(this.playerUUID);
        if (player == null) return;
        Location location = player.getLocation();
        this.mapView = Bukkit.createMap(player.getWorld());
        mapView.setCenterX(location.getBlockX());
        mapView.setCenterZ(location.getBlockZ());
        mapView.setScale(MapView.Scale.CLOSEST);
    }

    public void updateMinimap() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;
        Location location = player.getLocation();
        mapView.setCenterX(location.getBlockX());
        mapView.setCenterZ(location.getBlockZ());
        List<MapRenderer> removeMapRendererChecker = new ArrayList<>();
        for (MapRenderer mapRenderer : mapView.getRenderers()) {
            if (mapRenderer instanceof MinimapRenderer) {
                removeMapRendererChecker.add(mapRenderer);
            }
        }
        for (MapRenderer mapRenderer : removeMapRendererChecker) {
            mapView.getRenderers().remove(mapRenderer);
        }
        mapView.addRenderer(new MinimapRenderer());
    }

    public MapView getMapView() {
        return mapView;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
