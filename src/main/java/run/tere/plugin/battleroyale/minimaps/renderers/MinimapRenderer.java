package run.tere.plugin.battleroyale.minimaps.renderers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class MinimapRenderer extends MapRenderer {

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        Location location = player.getLocation();
        MapCursorCollection cursorCollection = new MapCursorCollection();
        if (cursorCollection.size() == 0) {
            cursorCollection.addCursor(0, 0, getRotation(-location.getYaw()));
        }

        MapCursor mapCursor = cursorCollection.getCursor(0);
        mapCursor.setX((byte) 0);
        mapCursor.setY((byte) 0);
        mapCursor.setDirection(getRotation(-location.getYaw()));
        canvas.setCursors(cursorCollection);
    }

    public byte getRotation(float yaw) {
        if ((yaw >= 0) && (yaw < 22.5)) {
            return 15;
        }
        if ((yaw >= 22.5) && (yaw < 45)) {
            return 14;
        }
        if ((yaw >= 45) && (yaw < 67.5)) {
            return 13;
        }
        if ((yaw >= 67.5) && (yaw < 90)) {
            return 12;
        }
        if ((yaw >= 90) && (yaw < 112.5)) {
            return 11;
        }
        if ((yaw >= 112.5) && (yaw < 135)) {
            return 10;
        }
        if ((yaw >= 135) && (yaw < 157.5)) {
            return 9;
        }
        if ((yaw >= 157.5) && (yaw < 190)) {
            return 8;
        }
        if ((yaw >= 190) && (yaw < 212.5)) {
            return 7;
        }
        if ((yaw >= 212.5) && (yaw < 235)) {
            return 6;
        }
        if ((yaw >= 235) && (yaw < 257.5)) {
            return 5;
        }
        if ((yaw >= 257.5) && (yaw < 290)) {
            return 4;
        }
        if ((yaw >= 290) && (yaw < 312.5)) {
            return 3;
        }
        if ((yaw >= 312.5) && (yaw < 335)) {
            return 2;
        }
        if ((yaw >= 335) && (yaw < 357.5)) {
            return 1;
        }
        if ((yaw >= 357.5) && (yaw < 360)) {
            return 0;
        }
        return 0;
    }
}
