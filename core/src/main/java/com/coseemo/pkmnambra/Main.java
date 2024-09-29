package com.coseemo.pkmnambra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.screen.GameScreen;
import com.coseemo.pkmnambra.screen.ServiceLocator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    @Override
    public void create() {
        Gdx.graphics.setVSync(false); // Abilita VSync
        Gdx.graphics.setForegroundFPS(60); // Imposta il frame rate a 60 FPS

        AssetManager assetManager = new AssetManager();
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        ServiceLocator.provideAssetManager(assetManager);
        setScreen(new GameScreen());
    }

}
