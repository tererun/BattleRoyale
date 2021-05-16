package run.tere.plugin.battleroyale.itemspawns;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class ItemSpawnLocation {
    private String worldUUID;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public ItemSpawnLocation(UUID worldUUID, double x, double y, double z, float yaw, float pitch) {
        this.worldUUID = worldUUID.toString();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ItemSpawnLocation(UUID worldUUID, double x, double y, double z) {
        this.worldUUID = worldUUID.toString();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    public World getWorld() {
        return Bukkit.getWorld(UUID.fromString(this.worldUUID));
    }

    public String getWorldUUID() {
        return worldUUID;
    }

    public void setWorldUUID(String worldUUID) {
        this.worldUUID = worldUUID;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public static Location convertItemSpawnLocationToBukkitLocation(ItemSpawnLocation itemSpawnLocation) {
        return new Location(itemSpawnLocation.getWorld(), itemSpawnLocation.getX(), itemSpawnLocation.getY(), itemSpawnLocation.getZ(), itemSpawnLocation.getYaw(), itemSpawnLocation.getPitch());
    }

    public static ItemSpawnLocation convertBukkitLocationToItemSpawnLocation(Location location) {
        return new ItemSpawnLocation(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
