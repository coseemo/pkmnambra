package com.coseemo.pkmnambra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.coseemo.pkmnambra.screen.PressXScreen;

public class Main extends Game {

    private AssetManager assetManager;

    @Override
    public void create() {
        Gdx.graphics.setVSync(false); // Abilita VSync
        Gdx.graphics.setForegroundFPS(60); // Imposta il frame rate a 60 FPS
        assetManager = new AssetManager();
        // Avvia la schermata di gioco
        setScreen(new PressXScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
