package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
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
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.screen.render.PlaceRenderer;
import com.coseemo.pkmnambra.util.MapUtil;
import com.coseemo.pkmnambra.controller.DialogueController;
import com.coseemo.pkmnambra.controller.PlayerController;
import com.coseemo.pkmnambra.ui.DialogueBox;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.util.AnimationSet;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.util.ServiceLocator;
import com.coseemo.pkmnambra.util.SkinGenerator;
import com.coseemo.pkmnambra.util.GameState;

import static com.coseemo.pkmnambra.items.CaptureItems.CaptureItemFactory.createItem;

public class GameScreen implements Screen {
    private Main game;
    private GameState gameState;
    private DialogueController dialogueController;
    private MapUtil mapUtil;
    private SpriteBatch batch;
    private Camera camera;
    private Skin skin;
    private Viewport gameViewport;
    private int uiScale = 2;
    private PlaceRenderer placeRenderer;
    private PlayerController controller;
    private Stage uiStage;
    private Table root;
    private DialogueBox dialogueBox;
    private OptionBox optionsBox;
    private Dialogue dialogue;

    public GameScreen(Main game) {
        this.game = game;
        this.gameState = GameState.getInstance();
    }

    @Override
    public void show() {
        gameViewport = new ScreenViewport();
        AssetManager assetManager = ServiceLocator.getAssetManager();
        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
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
            Place place = new Place(20, 20);
            Player player = new Player(place.getMap(), 10, 10, animations);
            place.addActor(player);
            gameState.initializeGameState(player, place);
        }

        camera = new Camera();
        mapUtil = new MapUtil();
        initUI();
        batch = new SpriteBatch();
        placeRenderer = new PlaceRenderer(assetManager, gameState.getCurrentPlace());
        controller = new PlayerController(gameState.getPlayer());
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        gameViewport.apply();
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = gameState.getPlayer();
        controller.update(delta);
        player.update(delta);
        camera.update(player.getPlaceX() + 0.5f, player.getPlaceY() + 0.5f);

        if(player.getPlaceX() == 11 && player.getPlaceY() == 10) {
            mapUtil.addHouse(gameState.getCurrentPlace(), 1, 1, ServiceLocator.getAssetManager());
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            player.setJustCapt(false);
        }

        // Check for Pokemon encounter
        if (player.getPlaceX() == 9 && player.getPlaceY() == 9 && !player.isJustCapt()) {
            Pokemon parasect = new Pokemon("Parasect", 50, 20);
            game.setScreen(new CaptureScreen(game, parasect));
            player.setJustCapt(true);
        }

        batch.begin();
        placeRenderer.render(batch, camera);
        batch.end();

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
        game.setScreen(new InventoryScreen(game));
    }

    private void initUI() {
        uiStage = new Stage(new ScreenViewport());
        uiStage.getViewport().update(Gdx.graphics.getWidth() / uiScale, Gdx.graphics.getHeight() / uiScale, true);

        root = new Table();
        root.setFillParent(true);
        uiStage.addActor(root);

        dialogueBox = new DialogueBox(skin);
        dialogueBox.setVisible(false);

        optionsBox = new OptionBox(skin);
        optionsBox.setVisible(false);

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
