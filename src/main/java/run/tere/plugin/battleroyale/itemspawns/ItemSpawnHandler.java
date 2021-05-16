package run.tere.plugin.battleroyale.itemspawns;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.AmmoUtils;
import run.tere.plugin.battleroyale.apis.GunUtils;
import run.tere.plugin.battleroyale.apis.JsonAPI;
import run.tere.plugin.battleroyale.guns.Gun;
import run.tere.plugin.battleroyale.guns.GunTemplate;
import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemSpawnHandler {
    private List<ItemSpawn> itemSpawnList;
    private ItemSpawnLocationHandler itemSpawnLocationHandler;

    public ItemSpawnHandler() {
        this.itemSpawnLocationHandler = JsonAPI.loadItemSpawnLocationHandler();
        this.itemSpawnList = new ArrayList<>();
        this.registerItemSpawns();
    }

    private void registerItemSpawns() {
        for (GunTemplate gunTemplate : BattleRoyale.getGameHandler().getGunHandler().getGunList().getGuns()) {
            Gun gun = GunUtils.createGun(gunTemplate);
            ItemStack gunStack = GunUtils.getGunStack(gun);
            ItemStack ammoOne = AmmoUtils.getAmmoStack(gun.getAmmoType(), gun.getAmmoType().getDefaultGiveAmount());
            ItemStack ammoTwo = AmmoUtils.getAmmoStack(gun.getAmmoType(), gun.getAmmoType().getDefaultGiveAmount());
            ItemStack ammoThree = AmmoUtils.getAmmoStack(gun.getAmmoType(), gun.getAmmoType().getDefaultGiveAmount());
            itemSpawnList.add(new ItemSpawn(gunStack, ammoOne, ammoTwo, ammoThree));
        }
        itemSpawnList.add(new ItemSpawn(AmmoUtils.getAmmoStack(AmmoType.IRON_AMMO, AmmoType.IRON_AMMO.getDefaultGiveAmount())));
        itemSpawnList.add(new ItemSpawn(AmmoUtils.getAmmoStack(AmmoType.NETHER_BRICK_AMMO, AmmoType.NETHER_BRICK_AMMO.getDefaultGiveAmount())));
        itemSpawnList.add(new ItemSpawn(AmmoUtils.getAmmoStack(AmmoType.GOLD_AMMO, AmmoType.GOLD_AMMO.getDefaultGiveAmount())));
        itemSpawnList.add(new ItemSpawn(AmmoUtils.getAmmoStack(AmmoType.DIAMOND_AMMO, AmmoType.DIAMOND_AMMO.getDefaultGiveAmount())));
    }

    public ItemSpawn getRandomItemSpawn() {
        Random random = new Random();
        int spawnIndex = random.nextInt(itemSpawnList.size());
        return itemSpawnList.get(spawnIndex);
    }

    public void spawnItems(Location location, ItemSpawn itemSpawn) {
        Random random = new Random();
        World world = location.getWorld();
        for (ItemStack itemStack : itemSpawn.getSpawnItems()) {
            double relativeSpawnX = ((double) random.nextInt(20) + 1) / 20 - 1;
            double relativeSpawnZ = ((double) random.nextInt(20) + 1) / 20 - 1;
            world.spawn(location.add(relativeSpawnX, 0, relativeSpawnZ), ArmorStand.class, as -> {
                as.setSilent(true);
                as.setInvisible(true);
                as.addScoreboardTag("selectitems");
                as.setInvulnerable(true);
                as.getEquipment().setHelmet(itemStack.clone());
            });
        }
    }

    public ItemSpawnLocationHandler getItemSpawnLocationHandler() {
        return itemSpawnLocationHandler;
    }
}
