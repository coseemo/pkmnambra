package com.coseemo.pkmnambra.Saver;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class GameSaver {
    private static final String SAVE_DIRECTORY = "saves/";

    // Salva i dati del gioco
    public static void save(GameData gameData, String fileName) {
        Json json = new Json();
        FileHandle file = Gdx.files.local(SAVE_DIRECTORY + fileName);
        file.writeString(json.toJson(gameData), false); // Sovrascrive il file
        Gdx.app.log("GameSaver", "Game saved to " + file.path());
    }

    // Carica i dati del gioco
    public static GameData load(String fileName) {
        FileHandle file = Gdx.files.local(SAVE_DIRECTORY + fileName);
        if (file.exists()) {
            Json json = new Json();
            GameData gameData = json.fromJson(GameData.class, file.readString());
            Gdx.app.log("GameSaver", "Game loaded from " + file.path());
            return gameData;
        } else {
            Gdx.app.log("GameSaver", "Save file not found: " + fileName);
            return null;
        }
    }
}
