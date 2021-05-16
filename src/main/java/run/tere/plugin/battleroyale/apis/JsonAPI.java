package run.tere.plugin.battleroyale.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.itemspawns.ItemSpawnLocationHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonAPI {
    public static void saveItemSpawnLocationHandler(ItemSpawnLocationHandler itemSpawnLocationHandler) {
        File file = new File(BattleRoyale.getPlugin().getDataFolder(), "itemSpawnLocation.json");
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(itemSpawnLocationHandler, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ItemSpawnLocationHandler loadItemSpawnLocationHandler() {
        File file = new File(BattleRoyale.getPlugin().getDataFolder(), "itemSpawnLocation.json");
        try (Reader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, ItemSpawnLocationHandler.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemSpawnLocationHandler itemSpawnLocationHandler = new ItemSpawnLocationHandler();
        saveItemSpawnLocationHandler(itemSpawnLocationHandler);
        return itemSpawnLocationHandler;
    }
}
