package com.coseemo.pkmnambra.savemanager;

import java.io.*;

import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.actorobserver.World;
import com.google.gson.*;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.json"; // Nome del file di salvataggio

    public static void saveGame(World world, Player player) throws IOException {
        Gson gson = new Gson();

        // Creo un oggetto SaveData con i dati attuali
        SaveData saveData = new SaveData();
        saveData.mapFileName = world.getName();
        saveData.playerData = PlayerData.fromPlayer(player);

        // Converto i dati in formato JSON
        String json = gson.toJson(saveData);

        // Scrivo i dati nel file di salvataggio
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            writer.write(json);
        }
    }

    public static SaveData loadGame() throws IOException {
        Gson gson = new Gson();

        // Leggo i dati dal file di salvataggio e li converto in oggetto SaveData
        try (FileReader reader = new FileReader(SAVE_FILE)) {
            return gson.fromJson(reader, SaveData.class);
        }
    }
}
