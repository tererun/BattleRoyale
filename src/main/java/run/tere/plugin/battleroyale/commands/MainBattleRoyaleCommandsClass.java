package run.tere.plugin.battleroyale.commands;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.AmmoUtils;
import run.tere.plugin.battleroyale.apis.GunUtils;
import run.tere.plugin.battleroyale.areas.AreaHandler;
import run.tere.plugin.battleroyale.guns.Gun;
import run.tere.plugin.battleroyale.guns.GunTemplate;
import run.tere.plugin.battleroyale.guns.ammos.Ammo;
import run.tere.plugin.battleroyale.guns.ammos.AmmoType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainBattleRoyaleCommandsClass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;
        if (!command.getName().equalsIgnoreCase("battleroyale")) return true;
        if (args.length == 0) {
            Player player = (Player) sender;
            for (GunTemplate gunTemplate : BattleRoyale.getGameHandler().getGunHandler().getGunList().getGuns()) {
                Gun gun = GunUtils.createGun(gunTemplate);
                ItemStack itemStack = GunUtils.getGunStack(gun);
                player.getInventory().addItem(itemStack);
            }
        } else {
            if (args[0].equalsIgnoreCase("nextarea")) {
                AreaHandler areaHandler = BattleRoyale.getGameHandler().getAreaHandler();
                areaHandler.nextArea(Double.parseDouble(args[1]), Long.parseLong(args[2]), Long.parseLong(args[3]));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (areaHandler.getNextLocation().distance(areaHandler.getWorldBorder().getCenter()) < 0.01) {
                            cancel();
                            return;
                        }
                        areaHandler.moveArea();
                    }
                }.runTaskTimer(BattleRoyale.getPlugin(), 0L, 1L);
            } else if (args[0].equalsIgnoreCase("start")) {
                BattleRoyale.getGameHandler().startGame();
            } else if (args[0].equalsIgnoreCase("ammo")) {
                Player player = (Player) sender;
                player.getInventory().addItem(AmmoUtils.getAmmoStack(AmmoType.IRON_AMMO, 64),
                        AmmoUtils.getAmmoStack(AmmoType.DIAMOND_AMMO, 64),
                        AmmoUtils.getAmmoStack(AmmoType.GOLD_AMMO, 64),
                        AmmoUtils.getAmmoStack(AmmoType.NETHER_BRICK_AMMO, 64));
            } else if (args[0].equalsIgnoreCase("spawnitem")) {
                Player player = (Player) sender;
                ItemStack itemStack = new ItemStack(Material.GOLD_BLOCK, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§6§lスポーンアイテム");
                itemMeta.setLore(new ArrayList<>(Arrays.asList("§fこれを置いたところにアイテムをスポーンさせます")));
                itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                itemStack.setItemMeta(itemMeta);
                itemStack = NBTEditor.set(itemStack, "Item", "SpawnTriggerItem");
                player.getInventory().addItem(itemStack);
            } else if (args[0].equalsIgnoreCase("removeammo")) {
                List<Ammo> ammoList = BattleRoyale.getGameHandler().getAmmoHandler().getAmmos();
                for (Ammo ammo : ammoList) {
                    ammoList.remove(ammo);
                }
            }
        }
        return false;
    }
}
