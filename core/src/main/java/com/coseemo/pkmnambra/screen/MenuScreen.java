package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private BitmapFont font;
    private final String[] options = {"New Game", "Load Game"};
    private int selectedIndex = 0;
    private Texture background;
    private Viewport viewport;

    public MenuScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.assetManager = ((Main) game).getAssetManager();

        // Carico lo sfondo
        this.background = new Texture(Gdx.files.internal("assets/background/amber.png"));

        // Inizializzo il viewport per il ridimensionamento
        this.viewport = new FitViewport(800, 600);

        // Creazione e scalatura del font
        this.font = new BitmapFont();
        this.font.getData().setScale(2f); // Scritte più grandi
    }

    @Override
    public void show() {
        // Nessuna azione necessaria quando lo schermo viene mostrato
    }

    @Override
    public void render(float delta) {
        // Pulizia dello schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Aggiorno il viewport
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Disegno lo sfondo
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Gestione input
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (selectedIndex == 0) {
                game.setScreen(new TransitionScreen(game, this, new GameScreen(loadAllAssets())));
            } else {
                SaveData saveData;
                try {
                    saveData = SaveManager.loadGame();
                } catch (IOException e) {
                    throw new RuntimeException("Errore durante il caricamento del salvataggio", e);
                }

                GameState gameState = loadAllAssets();
                World world = MapLoader.loadMapFromSave(saveData, assetManager);

                gameState.initialize((Main) game, world.getPlayer(), world);
                game.setScreen(new TransitionScreen(game, this, new GameScreen(gameState)));
            }
        }

        // Disegno le opzioni del menu
        batch.begin();
        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                font.setColor(0, 0, 1, 1); // Evidenzio l'opzione selezionata
            } else {
                font.setColor(1, 1, 1, 1);
            }
            font.draw(batch, options[i], viewport.getWorldWidth() / 2f + 100, viewport.getWorldHeight() / 2f - i * 50);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Aggiorno il viewport con le nuove dimensioni
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Nessuna azione necessaria in pausa
    }

    @Override
    public void resume() {
        // Nessuna azione necessaria al ripristino
    }

    @Override
    public void hide() {
        // Nessuna azione necessaria quando lo schermo viene nascosto
    }

    @Override
    public void dispose() {
        // Rilascio le risorse
        batch.dispose();
        font.dispose();
        background.dispose();
    }

    /**
     * Carico tutte le risorse necessarie.
     */
    public GameState loadAllAssets() {
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

        GameState gameState = GameState.getInstance();
        gameState.initialize((Main) game, player, start);
        return gameState;
    }
}
