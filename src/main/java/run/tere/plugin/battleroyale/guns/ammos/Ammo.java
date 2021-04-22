package run.tere.plugin.battleroyale.guns.ammos;

import org.bukkit.Location;
import run.tere.plugin.battleroyale.BattleRoyale;

import java.util.UUID;

public class Ammo {
    private UUID uuid;
    private String gunName;
    private AmmoType ammoType;
    private double damage;
    private double headShotDamageMagnification;
    private double flyingDistance;
    private Location spawnLocation;
    private Location fallingStartLocation;
    private Location nowLocation;
    private int nowTurn;
    private long ammoSpeed;

    public Ammo(UUID uuid, String gunName, AmmoType ammoType, double damage, double headShotDamageMagnification, double flyingDistance, Location spawnLocation, long ammoSpeed) {
        this.uuid = uuid;
        this.gunName = gunName;
        this.ammoType = ammoType;
        this.damage = damage;
        this.headShotDamageMagnification = headShotDamageMagnification;
        this.flyingDistance = flyingDistance;
        this.spawnLocation = spawnLocation.clone();
        this.fallingStartLocation = null;
        this.nowLocation = spawnLocation.clone().add(this.spawnLocation.getDirection().getX(), this.spawnLocation.getDirection().getY(), this.spawnLocation.getDirection().getZ());
        this.nowTurn = 0;
        this.ammoSpeed = ammoSpeed;
        BattleRoyale.getGameHandler().getAmmoHandler().addAmmo(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getGunName() {
        return gunName;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getHeadShotDamageMagnification() {
        return headShotDamageMagnification;
    }

    public void setHeadShotDamageMagnification(double headShotDamageMagnification) {
        this.headShotDamageMagnification = headShotDamageMagnification;
    }

    public double getFlyingDistance() {
        return flyingDistance;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getNowLocation() {
        return nowLocation;
    }

    public void setNowLocation(Location nowLocation) {
        this.nowLocation = nowLocation;
    }

    public Location getFallingStartLocation() {
        return fallingStartLocation;
    }

    public void addNowTurn(int nowTurn) {
        setNowTurn(getNowTurn() + nowTurn);
    }

    public void setNowTurn(int nowTurn) {
        this.nowTurn = nowTurn;
    }

    public int getNowTurn() {
        return nowTurn;
    }

    public long getAmmoSpeed() {
        return ammoSpeed;
    }
}
