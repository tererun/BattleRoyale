package run.tere.plugin.battleroyale.guns;

import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GunList {
    private List<Gun> guns;

    public GunList() {
        guns = new ArrayList<>();
        guns.add(new Gun(UUID.randomUUID(), "LongBow", 21, AmmoType.DIAMOND_AMMO, 5, 3, 2, 150, -1000L, 80L, 3F, 2F, 1L, -0.08));
        guns.add(new Gun(UUID.randomUUID(), "Wingman", 41, AmmoType.GOLD_AMMO, 5, 4, 1.2, 25, -430L, 40L, 0F, 0F, 2L, -0.028));
        guns.add(new Gun(UUID.randomUUID(), "AL-89N", 1, AmmoType.IRON_AMMO, 32, 1.2, 2, 50, 75L, 50L, 0.4F, 0.2F, 3L, -0.05));
        guns.add(new Gun(UUID.randomUUID(), "SpitFire", 61, AmmoType.GOLD_AMMO, 55, 2, 2, 70, 120L, 55L, 0.8F, 0.45F, 4L, -0.06));
        guns.add(new Gun(UUID.randomUUID(), "R-99", 81, AmmoType.IRON_AMMO, 27, 1.4, 2, 40, 50L, 20L, 0.53F, 0.3F, 5L, -0.03));
        guns.add(new Gun(UUID.randomUUID(), "Mastif", 101, AmmoType.NETHER_BRICK_AMMO, 6, 5, 2, 10, -800L, 60L, 5F, 1.75F, 7L, -0.03));

    }

    public List<Gun> getGuns() {
        return guns;
    }
}
