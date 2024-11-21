package com.coseemo.pkmnambra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.dialogue.DialogueLoader;
import com.coseemo.pkmnambra.maplogic.World;
import com.coseemo.pkmnambra.screen.GameScreen;
import com.coseemo.pkmnambra.screen.PressXScreen;
import com.coseemo.pkmnambra.util.AnimationSet;
import com.coseemo.pkmnambra.util.MapLoader;
import com.coseemo.pkmnambra.util.states.GameState;

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
