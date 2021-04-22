package run.tere.plugin.battleroyale.areas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import run.tere.plugin.battleroyale.BattleRoyale;

import java.util.Random;
import java.util.UUID;

public class AreaHandler {
    private UUID worldUUID;
    private Location prevLocation;
    private Location nextLocation;
    private double moveX;
    private double moveZ;

    public AreaHandler(World world) {
        this.worldUUID = world.getUID();
    }

    public void move(double nextSize, long seconds) {
        AreaHandler areaHandler = BattleRoyale.getGameHandler().getAreaHandler();
        areaHandler.nextArea(nextSize, seconds, 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (areaHandler.getNextLocation().distance(areaHandler.getWorldBorder().getCenter()) < 0.01) {
                    Bukkit.broadcastMessage("§6収縮が終了しました");
                    cancel();
                    return;
                }
                areaHandler.moveArea();
            }
        }.runTaskTimer(BattleRoyale.getPlugin(), 0L, 1L);
    }

    public void moveArea() {
        WorldBorder worldBorder = getWorldBorder();
        Location worldBorderCenter = worldBorder.getCenter().clone();
        worldBorder.setCenter(worldBorderCenter.getX() + moveX, worldBorderCenter.getZ() + moveZ);
    }

    public void nextArea(double nextSize, long seconds, long interval) {
        WorldBorder worldBorder = getWorldBorder();
        Location centerLocation = worldBorder.getCenter().clone();
        Random random = new Random();
        int calcedSize = (int) (worldBorder.getSize() - nextSize);
        double halfBorderSize = worldBorder.getSize() / 2;
        double halfNextSize = nextSize / 2;
        double centerLocationX = centerLocation.getX();
        double centerLocationZ = centerLocation.getZ();
        double upperLeftX = centerLocationX - (halfBorderSize);
        double upperLeftZ = centerLocationZ - (halfBorderSize);
        int centerX = random.nextInt(calcedSize);
        int centerZ = random.nextInt(calcedSize);
        Location nextCenterLocation = new Location(Bukkit.getWorld(this.worldUUID), upperLeftX + centerX + (halfNextSize), centerLocation.getY(), upperLeftZ + centerZ + (halfNextSize));
        double nextLocationX = nextCenterLocation.getX();
        double nextLocationZ = nextCenterLocation.getZ();
        this.setPrevLocation(centerLocation);
        this.setNextLocation(nextCenterLocation);
        long frequency = seconds * interval;
        this.setMoveX((nextLocationX - centerLocationX) / frequency);
        this.setMoveZ((nextLocationZ - centerLocationZ) / frequency);
        Bukkit.broadcastMessage("§6収縮が始まりました!!");
        Bukkit.broadcastMessage("§a次の中心はここです§f: (" + nextLocationX + ", " + nextCenterLocation.getY() + ", " + nextLocationZ + ")");
        Bukkit.broadcastMessage("§b収縮時間は §f" + seconds + " 秒§bです");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setCompassTarget(nextLocation);
        }
        worldBorder.setSize(nextSize, seconds);
    }

    public WorldBorder getWorldBorder() {
        return Bukkit.getWorld(this.worldUUID).getWorldBorder();
    }

    public Location getPrevLocation() {
        return prevLocation;
    }

    public Location getNextLocation() {
        return nextLocation;
    }

    public double getMoveX() {
        return moveX;
    }

    public double getMoveZ() {
        return moveZ;
    }

    public void setPrevLocation(Location prevLocation) {
        this.prevLocation = prevLocation;
    }

    public void setNextLocation(Location nextLocation) {
        this.nextLocation = nextLocation;
    }

    public void setMoveX(double moveX) {
        this.moveX = moveX;
    }

    public void setMoveZ(double moveZ) {
        this.moveZ = moveZ;
    }
}
