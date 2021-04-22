package run.tere.plugin.battleroyale.guns;

import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.UUID;

public class Gun {
    private UUID uuid;
    private String gunName;
    private int customModelData;
    private AmmoType ammoType;
    private int maxAmmo;
    private int nowAmmo;
    private double damage;
    private double headShotDamageMagnification;
    private double flyingDistance;
    private long launchRate;
    private long reloadLength;
    private float verticalRecoil;
    private float sideRecoil;
    private long ammoSpeed;
    private boolean launched;
    private boolean reloading;
    private double scopingWalkSpeed;

    public Gun(UUID uuid, String gunName, int customModelData, AmmoType ammoType, int maxAmmo, double damage, double headShotDamageMagnification, double flyingDistance, long launchRate, long reloadLength, float verticalRecoil, float sideRecoil, long ammoSpeed, double scopingWalkSpeed) {
        this.uuid = uuid;
        this.gunName = gunName;
        this.customModelData = customModelData;
        this.ammoType = ammoType;
        this.maxAmmo = maxAmmo;
        this.nowAmmo = maxAmmo;
        this.damage = damage;
        this.headShotDamageMagnification = headShotDamageMagnification;
        this.flyingDistance = flyingDistance;
        this.launchRate = launchRate;
        this.reloadLength = reloadLength;
        this.verticalRecoil = verticalRecoil;
        this.sideRecoil = sideRecoil;
        this.ammoSpeed = ammoSpeed;
        this.launched = false;
        this.reloading = false;
        this.scopingWalkSpeed = scopingWalkSpeed;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getGunName() {
        return gunName;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getNowAmmo() {
        return nowAmmo;
    }

    public void addNowAmmo(int nowAmmo) {
        this.setNowAmmo(this.getNowAmmo() + nowAmmo);
    }

    public void setNowAmmo(int nowAmmo) {
        int caledAmmo = nowAmmo;
        if (nowAmmo < 0) {
            caledAmmo = 0;
        }
        this.nowAmmo = caledAmmo;
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

    public long getLaunchRate() {
        return launchRate;
    }

    public long getReloadLength() {
        return reloadLength;
    }

    public float getVerticalRecoil() {
        return verticalRecoil;
    }

    public void setVerticalRecoil(float verticalRecoil) {
        this.verticalRecoil = verticalRecoil;
    }

    public float getSideRecoil() {
        return sideRecoil;
    }

    public void setSideRecoil(float sideRecoil) {
        this.sideRecoil = sideRecoil;
    }

    public long getAmmoSpeed() {
        return ammoSpeed;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public double getScopingWalkSpeed() {
        return scopingWalkSpeed;
    }

    public void setScopingWalkSpeed(double scopingWalkSpeed) {
        this.scopingWalkSpeed = scopingWalkSpeed;
    }
}
