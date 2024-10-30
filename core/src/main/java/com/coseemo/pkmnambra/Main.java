package com.coseemo.pkmnambra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.screen.GameScreen;
import com.coseemo.pkmnambra.util.EventNotifier; // Importa EventNotifier
import com.coseemo.pkmnambra.util.ServiceLocator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private EventNotifier eventNotifier; // Aggiungi un'istanza di EventNotifier

    @Override
    public void create() {
        Gdx.graphics.setVSync(false); // Abilita VSync
        Gdx.graphics.setForegroundFPS(60); // Imposta il frame rate a 60 FPS

        AssetManager assetManager = new AssetManager();
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.finishLoading();

        // Imposta il gestore degli asset
        ServiceLocator.provideAssetManager(assetManager);

        // Inizializza l'EventNotifier
        eventNotifier = new EventNotifier();

        // Avvia la schermata di gioco
        setScreen(new GameScreen(this, eventNotifier)); // Passa l'EventNotifier al GameScreen
    }
}
