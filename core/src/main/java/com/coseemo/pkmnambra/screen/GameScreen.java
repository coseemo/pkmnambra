package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.camera.Camera;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.controller.*;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.dialogue.DialogueLoader;
import com.coseemo.pkmnambra.maplogic.World;
import com.coseemo.pkmnambra.screen.render.PlaceRenderer;
import com.coseemo.pkmnambra.util.*;
import com.coseemo.pkmnambra.ui.DialogueBox;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.util.states.GameState;

import static com.coseemo.pkmnambra.items.CaptureItems.CaptureItemFactory.createItem;

public class GameScreen implements Screen {
    private GameState gameState;
    private DialogueController dialogueController;
    private InteractionController interactionController;
    private SpriteBatch batch;
    private Camera camera;
    private Skin skin;
    private Viewport gameViewport;
    private int uiScale = 2;
    private PlaceRenderer placeRenderer;
    private PlayerController playerController;
    private InputMultiplexer multiplexer;

    private Player player;
    private Stage uiStage;
    private Table root;
    private DialogueBox dialogueBox;
    private OptionBox optionsBox;

    public GameScreen(GameState gameState) {

        this.gameState = gameState;
        this.player = gameState.getPlayerState().getPlayer();

        gameState.getScreenManager().setCurrentScreen(this);


        camera = new Camera();
        gameViewport = new ScreenViewport();

        AssetManager assetManager = gameState.getResourceManager().getAssetManager();
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        assetManager.finishLoading();
        skin = SkinGenerator.generateSkin(assetManager);

        initUI();
        multiplexer = new InputMultiplexer();
        batch = new SpriteBatch();
        placeRenderer = new PlaceRenderer(gameState);

        this.dialogueController = new DialogueController(dialogueBox, optionsBox);
        this.interactionController = new InteractionController(player, dialogueController);
        this.playerController = new PlayerController(player);


        // Configuriamo il multiplexer nell'ordine corretto
        multiplexer = new InputMultiplexer();

        multiplexer.addProcessor(0, dialogueController);
        multiplexer.addProcessor(1, playerController);
        multiplexer.addProcessor(2, interactionController);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        gameState.saveToFile();
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        gameViewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        placeRenderer.getWorld().update(delta);
        dialogueController.update(delta);
        playerController.update(delta);


        camera.update(player.getWorldX() + 0.5f, player.getWorldY() + 0.5f);


        if (placeRenderer.getWorld() != gameState.getMapState().getCurrentPlace()) {
            World newPlace = gameState.getMapState().getCurrentPlace();
            placeRenderer.setWorld(newPlace);
        }

        // Debug keys
        if (player.isHasInventory() && Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            player.addItem(createItem("masterball"));
            player.addItem(createItem("spicybait"));
            player.addItem(createItem("herbalperfume"));
            player.addItem(createItem("advancedtrap"));
        }

        if (player.isHasInventory() && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            openInventory();
        }

        batch.begin();
        placeRenderer.render(batch, camera);
        batch.end();

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        uiStage.getViewport().update(width / uiScale, height / uiScale, true);
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        uiStage.dispose();
    }

    private void openInventory() {
        gameState.getGame().setScreen(new InventoryScreen(gameState));
    }

    private void initUI() {
        uiStage = new Stage(new ScreenViewport());
        uiStage.getViewport().update(Gdx.graphics.getWidth() / uiScale, Gdx.graphics.getHeight() / uiScale, true);

        root = new Table();
        root.setFillParent(true);
        uiStage.addActor(root);

        dialogueBox = new DialogueBox(skin);
        dialogueBox.setVisible(false);
        dialogueBox.setFillParent(false);

        optionsBox = new OptionBox(skin);
        optionsBox.setVisible(false);

        // Creiamo la tabella per i dialoghi
        Table dialogTable = new Table();
        dialogTable.add(optionsBox)
            .expand()
            .align(Align.right)
            .space(8f)
            .row();
        dialogTable.add(dialogueBox)
            .expand()
            .align(Align.bottom)
            .space(8f)
            .row();

        root.add(dialogTable).expand().align(Align.bottom);
    }

}
