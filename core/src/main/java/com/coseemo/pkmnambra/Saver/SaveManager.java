package com.coseemo.pkmnambra.Saver;

import java.io.*;

import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.World;
import com.google.gson.*;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.json";

    public static void saveGame(World world, Player player) throws IOException {
        Gson gson = new Gson();

        SaveData saveData = new SaveData();
        saveData.mapFileName = world.getName();
        saveData.playerData = PlayerData.fromPlayer(player);

        String json = gson.toJson(saveData);

        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            writer.write(json);
        }
    }

    public static SaveData loadGame() throws IOException {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(SAVE_FILE)) {
            return gson.fromJson(reader, SaveData.class);
        }
    }
}
