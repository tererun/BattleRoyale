package run.tere.plugin.battleroyale.guns;

import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

public class GunTemplate {
    private String gunName;
    private int customModelData;
    private AmmoType ammoType;
    private int maxAmmo;
    private double damage;
    private double headShotDamageMagnification;
    private double flyingDistance;
    private long launchRate;
    private long reloadLength;
    private float verticalRecoil;
    private float sideRecoil;
    private long ammoSpeed;
    private double scopingWalkSpeed;

    public GunTemplate(String gunName, int customModelData, AmmoType ammoType, int maxAmmo, double damage, double headShotDamageMagnification, double flyingDistance, long launchRate, long reloadLength, float verticalRecoil, float sideRecoil, long ammoSpeed, double scopingWalkSpeed) {
        this.gunName = gunName;
        this.customModelData = customModelData;
        this.ammoType = ammoType;
        this.maxAmmo = maxAmmo;
        this.damage = damage;
        this.headShotDamageMagnification = headShotDamageMagnification;
        this.flyingDistance = flyingDistance;
        this.launchRate = launchRate;
        this.reloadLength = reloadLength;
        this.verticalRecoil = verticalRecoil;
        this.sideRecoil = sideRecoil;
        this.ammoSpeed = ammoSpeed;
        this.scopingWalkSpeed = scopingWalkSpeed;
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

    public double getScopingWalkSpeed() {
        return scopingWalkSpeed;
    }

    public void setScopingWalkSpeed(double scopingWalkSpeed) {
        this.scopingWalkSpeed = scopingWalkSpeed;
    }
}
