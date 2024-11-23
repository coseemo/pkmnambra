package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.savemanager.SaveData;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.dialogue.DialogueLoader;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.savemanager.SaveManager;
import com.coseemo.pkmnambra.util.AnimationSet;
import com.coseemo.pkmnambra.savemanager.MapLoader;
import com.coseemo.pkmnambra.singleton.GameState;

import java.io.IOException;

public class MenuScreen implements Screen {
    private final Game game;
    AssetManager assetManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private String[] options = {"New Game", "Load Game"};
    private int selectedIndex = 0;

    public MenuScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.assetManager = ((Main) game).getAssetManager();

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Clear dello schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Input per spostarsi tra le opzioni
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (selectedIndex == 0) {
                game.setScreen(new TransitionScreen(game, this, new GameScreen(loadall())));
            } else {
                SaveData saveData = null;
                try {
                    saveData = SaveManager.loadGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                GameState gameState = loadall();
                World world = MapLoader.loadMapFromSave(saveData, assetManager);

                gameState.initialize((Main) game, world.getPlayer(), world);
                game.setScreen(new TransitionScreen(game, this, new GameScreen(gameState)));
            }
        }

        // Disegno delle opzioni
        batch.begin();
        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                font.setColor(1, 1, 0, 1); // Evidenziato
            } else {
                font.setColor(1, 1, 1, 1);
            }
            font.draw(batch, options[i], Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - i * 30);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public GameState loadall(){
        // Inizializza AssetManager
        AssetManager assetManager = new AssetManager();
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        assetManager.load("assets/sprites/professorpacked/professorpacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        AnimationSet animations = new AnimationSet(
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_east"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_west"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_north"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
            atlas.findRegion("mimi_standing_east"),
            atlas.findRegion("mimi_standing_west"),
            atlas.findRegion("mimi_standing_north"),
            atlas.findRegion("mimi_standing_south")
        );

        World start = MapLoader.loadMapAndObjects("assets/maps/beach.txt", assetManager);
        Player player = new Player(start, 10, 10, animations);
        start.addPlayer(player);
        // Inizializza GameState
        GameState gameState = GameState.getInstance();
        gameState.initialize((Main) game, player, start);
        return gameState;
    }
}
