package run.tere.plugin.battleroyale.games;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.PlayerAPI;

import java.util.UUID;

public class GameCheckListenerClass implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!PlayerAPI.isPlayerWon()) return;
        UUID wonUUID = PlayerAPI.getPlayerWon();
        Player player = Bukkit.getPlayer(wonUUID);
        if (player == null) return;
        Bukkit.broadcastMessage("§e§o今回のWinnerが決まりました!");
        Sound winBGM = Sound.MUSIC_DISC_STAL;
        PlayerAPI.sendTitle("§l§o§6優勝者", "§a" + player.getName(), 10, 160, 30);
        PlayerAPI.playSound(winBGM, SoundCategory.RECORDS, 1F, 1F);
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerAPI.stopSound(winBGM);
                PlayerAPI.teleport(new Location(Bukkit.getWorld("world"), 65, 30, -239));
                PlayerAPI.setGameMode(GameMode.ADVENTURE);
                BattleRoyale.getGameHandler().resetWorldBorder();
                BattleRoyale.setGameHandler(new GameHandler());
            }
        }.runTaskLater(BattleRoyale.getPlugin(), 200L);
    }
}
