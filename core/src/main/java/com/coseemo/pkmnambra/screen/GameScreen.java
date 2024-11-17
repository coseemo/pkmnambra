package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.camera.Camera;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.controller.NPCController;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.dialogue.DialogueLoader;
import com.coseemo.pkmnambra.events.EventManager;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.screen.render.PlaceRenderer;
import com.coseemo.pkmnambra.util.*;
import com.coseemo.pkmnambra.controller.DialogueController;
import com.coseemo.pkmnambra.controller.PlayerController;
import com.coseemo.pkmnambra.ui.DialogueBox;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.dialogue.Dialogue;

import static com.coseemo.pkmnambra.items.CaptureItems.CaptureItemFactory.createItem;

public class GameScreen implements Screen {
    private Main game;
    private GameState gameState;
    private DialogueController dialogueController;
    private SpriteBatch batch;
    private Camera camera;
    private Skin skin;
    private Viewport gameViewport;
    private int uiScale = 2;
    private PlaceRenderer placeRenderer;
    private PlayerController playerController;
    private NPCController npcController;
    private InputMultiplexer multiplexer;
    private Stage uiStage;
    private Table root;
    private DialogueBox dialogueBox;
    private OptionBox optionsBox;
    private Dialogue dialogue;
    private EventManager eventManager;

    public GameScreen(GameState gameState) {
        this.game = gameState.getGame();
        this.gameState = GameState.getInstance();
        eventManager = new EventManager(gameState);
        camera = new Camera();
        gameViewport = new ScreenViewport();
        AssetManager assetManager = gameState.getAssetManager();
        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        skin = SkinGenerator.generateSkin(assetManager);
        assetManager.finishLoading();

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

        // Inizializza o recupera lo stato di gioco
        if (gameState.getCurrentPlace() == null) {
            String currentPlace = "assets/maps/beach.txt";
            Place place = MapLoader.loadMapAndObjects(currentPlace, gameState.getAssetManager());
            Player player = new Player(place, 10, 10, animations);
            place.addActor(player);
            gameState.initializeGameState(player, place, game);
        }

        if (gameState.getCurrentPlace() != null && gameState.getCurrentPlace().getEvents() != null) {
            eventManager.registerEvents(gameState.getCurrentPlace().getEvents());
        }

        initUI();
        multiplexer = new InputMultiplexer();
        batch = new SpriteBatch();
        placeRenderer = new PlaceRenderer(assetManager, gameState.getCurrentPlace());

        this.dialogueController = new DialogueController(dialogueBox, optionsBox);
        this.npcController = new NPCController(dialogueController);
        this.playerController = new PlayerController(gameState.getPlayer());

        // Configuriamo il multiplexer nell'ordine corretto
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(dialogueController);
        multiplexer.addProcessor(npcController);
        multiplexer.addProcessor(playerController);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        gameViewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateEvents();
        Player player = gameState.getPlayer();
        player.update(delta);

        dialogueController.update(delta);
        playerController.update(delta);
        camera.update(player.getPlaceX() + 0.5f, player.getPlaceY() + 0.5f);

        eventManager.checkEvents(player);

        if (placeRenderer.getPlace() != gameState.getCurrentPlace()) {
            Place newPlace = gameState.getCurrentPlace();
            if (newPlace != null && newPlace.getEvents() != null) {
                eventManager.clearEvents();
                eventManager.registerEvents(newPlace.getEvents());
            }
            placeRenderer.setPlace(newPlace);
        }

        // Debug keys
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            player.addItem(createItem("masterball"));
            player.addItem(createItem("spicybait"));
            player.addItem(createItem("herbalperfume"));
            player.addItem(createItem("advancedtrap"));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            openInventory();
        }

        batch.begin();
        placeRenderer.render(batch, camera);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            dialogueBox.setVisible(true);  // Rendi visibile il DialogueBox
            dialogueBox.animateText("Questo è un test del DialogueBox!");
        }

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

    private void updateEvents() {
        if (gameState.getCurrentPlace() != placeRenderer.getPlace()) {
            Place newPlace = gameState.getCurrentPlace();
            eventManager.clearEvents();
            eventManager.registerEvents(newPlace.getEvents());
            placeRenderer.setPlace(newPlace);
        }

        // Controlla gli eventi alla posizione del giocatore
        eventManager.checkEvents(gameState.getPlayer());
    }

    private void openInventory() {
        game.setScreen(new InventoryScreen(gameState));
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
