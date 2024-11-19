package com.coseemo.pkmnambra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.screen.GameScreen;
import com.coseemo.pkmnambra.util.states.GameState;

public class Main extends Game {

    private GameState gameState; // GameState sostituisce ServiceLocator
    private AssetManager assetManager;

    @Override
    public void create() {
        Gdx.graphics.setVSync(false); // Abilita VSync
        Gdx.graphics.setForegroundFPS(60); // Imposta il frame rate a 60 FPS

        // Inizializza AssetManager
        assetManager = new AssetManager();
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.finishLoading();

        // Inizializza GameState
        gameState = GameState.getInstance();
        gameState.initializeGameState(null, null, this); // Puoi aggiungere Player e Place

        // Imposta l'AssetManager nel GameState
        gameState.setAssetManager(assetManager);

        // Avvia la schermata di gioco
        setScreen(new GameScreen(gameState)); // Passa il GameState al GameScreen
    }
}
