package run.tere.plugin.battleroyale.guns;

import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.ArrayList;
import java.util.List;

public class GunList {
    private List<GunTemplate> guns;

    public GunList() {
        guns = new ArrayList<>();
        guns.add(new GunTemplate("SeaLion", 21, AmmoType.DIAMOND_AMMO, 5, 6, 2, 150, -1000L, 80L, 3F, 2F, 2L, -0.08));
        guns.add(new GunTemplate("FriendlyGun", 41, AmmoType.GOLD_AMMO, 5, 4, 1.2, 25, -430L, 40L, 0F, 0F, 3L, -0.028));
        guns.add(new GunTemplate("AL-89N", 1, AmmoType.IRON_AMMO, 32, 1.2, 2, 50, 75L, 50L, 0.4F, 0.2F, 4L, -0.05));
        guns.add(new GunTemplate("N-G057", 61, AmmoType.GOLD_AMMO, 55, 2, 2, 70, 120L, 55L, 0.8F, 0.45F, 5L, -0.06));
        guns.add(new GunTemplate("TrR-N27", 81, AmmoType.IRON_AMMO, 27, 1, 2, 40, 50L, 20L, 0.53F, 0.3F, 6L, -0.03));
        guns.add(new GunTemplate("Gunmagnum", 101, AmmoType.NETHER_BRICK_AMMO, 6, 5, 2, 10, -800L, 60L, 5F, 1.75F, 8L, -0.03));
    }

    public List<GunTemplate> getGuns() {
        return guns;
    }
}
