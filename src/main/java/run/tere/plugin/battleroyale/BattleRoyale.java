package run.tere.plugin.battleroyale;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import run.tere.plugin.battleroyale.commands.MainBattleRoyaleCommandsClass;
import run.tere.plugin.battleroyale.games.GameHandler;
import run.tere.plugin.battleroyale.itemspawns.ItemSpawnHandler;
import run.tere.plugin.battleroyale.listeners.MainBattleRoyaleListenerClass;

public final class BattleRoyale extends JavaPlugin {

    private static Plugin plugin;
    private static GameHandler gameHandler;

    @Override
    public void onEnable() {
        plugin = this;
        gameHandler = new GameHandler();
        getServer().getPluginManager().registerEvents(new MainBattleRoyaleListenerClass(), this);
        getCommand("battleroyale").setExecutor(new MainBattleRoyaleCommandsClass());
        gameHandler.setItemSpawnHandler(new ItemSpawnHandler());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static GameHandler getGameHandler() {
        return gameHandler;
    }

    public static void setGameHandler(GameHandler gameHandler) {
        BattleRoyale.gameHandler = gameHandler;
    }
}
