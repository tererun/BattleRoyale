package run.tere.plugin.battleroyale.apis;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerAPI {
    public static UUID getPlayerWon() {
        UUID wonPlayerUUID = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode().equals(GameMode.SPECTATOR)) continue;
            if (wonPlayerUUID != null) return null;
            wonPlayerUUID = player.getUniqueId();
        }
        return wonPlayerUUID;
    }

    public static boolean isPlayerWon() {
        return getPlayerWon() != null;
    }

    public static void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        }
    }

    public static void playSound(Sound sound, float volume, float pitch) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), sound, volume, pitch);
        }
    }

    public static void playSound(Sound sound, SoundCategory soundCategory, float volume, float pitch) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), sound, soundCategory, volume, pitch);
        }
    }

    public static void stopSound(Sound sound) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.stopSound(sound);
        }
    }

    public static void teleport(Location location) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.teleport(location);
        }
    }

    public static void setGameMode(GameMode gameMode) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setGameMode(gameMode);
        }
    }

    public static void clearInventory() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getInventory().clear();
        }
    }

    public static void addPotionEffect(PotionEffect potionEffect) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.addPotionEffect(potionEffect);
        }
    }

    public static void teamAddEntry(Team team) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            team.addEntry(onlinePlayer.getName());
        }
    }
}
