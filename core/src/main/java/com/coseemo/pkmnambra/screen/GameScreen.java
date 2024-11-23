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
import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.camera.Camera;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.controller.*;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.dialogue.DialogueLoader;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.savemanager.SaveManager;
import com.coseemo.pkmnambra.render.PlaceRenderer;
import com.coseemo.pkmnambra.util.*;
import com.coseemo.pkmnambra.ui.DialogueBox;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.singleton.GameState;

import java.io.IOException;

public class GameScreen implements Screen {
    private GameState gameState;
    private DialogueController dialogueController;
    private InteractionController interactionController;
    private SpriteBatch batch;
    private Camera camera;
    private Skin skin;
    private Viewport gameViewport;
    private int uiScale = 2;  // Scala dell'interfaccia utente
    private PlaceRenderer placeRenderer;
    private PlayerController playerController;
    private InputMultiplexer multiplexer;

    private Player player;
    private Stage uiStage;
    private Table root;
    private DialogueBox dialogueBox;
    private OptionBox optionsBox;

    // Costruttore della schermata di gioco
    public GameScreen(GameState gameState) {
        this.gameState = gameState;
        this.player = gameState.getPlayerState().getPlayer();

        gameState.getScreenManager().setCurrentScreen(this);  // Imposta la schermata attuale

        camera = new Camera();  // Crea una nuova telecamera
        gameViewport = new ScreenViewport();  // Crea il viewport per la schermata di gioco

        // Carica tutte le risorse necessarie
        AssetManager assetManager = gameState.getResourceManager().getAssetManager();
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/sprites/professorpacked/professorpacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        assetManager.finishLoading();
        skin = SkinGenerator.generateSkin(assetManager);  // Crea la skin per l'interfaccia utente

        initUI();  // Inizializza l'interfaccia utente
        multiplexer = new InputMultiplexer();  // Crea un multiplexer per gestire più input contemporaneamente
        batch = new SpriteBatch();  // Crea un batch per il rendering

        placeRenderer = new PlaceRenderer(gameState);  // Renderer per il mondo di gioco

        // Crea i controller per il dialogo, l'interazione e il giocatore
        this.dialogueController = new DialogueController(dialogueBox, optionsBox);
        this.interactionController = new InteractionController(player, dialogueController);
        this.playerController = new PlayerController(player);

        // Configura il multiplexer per gestire l'input in ordine
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(0, dialogueController);  // Gestisce il dialogo
        multiplexer.addProcessor(1, playerController);  // Gestisce il movimento del giocatore
        multiplexer.addProcessor(2, interactionController);  // Gestisce le interazioni

        Gdx.input.setInputProcessor(multiplexer);  // Imposta il processore di input
    }

    @Override
    public void show() {
        // Resetta i tasti e i timer per il controller del giocatore
        playerController.resetButtonsAndTimers();
        player.setState(Actor.ACTOR_STATE.STANDING);  // Imposta lo stato del giocatore a STANDING
        Gdx.input.setInputProcessor(multiplexer);  // Imposta di nuovo il processore di input
    }

    @Override
    public void render(float delta) {
        gameViewport.apply();  // Applica il viewport
        Gdx.gl.glClearColor(0, 0, 0, 0);  // Imposta il colore di sfondo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Pulisce il buffer dello schermo

        placeRenderer.getWorld().update(delta);  // Aggiorna il mondo di gioco
        dialogueController.update(delta);  // Aggiorna il controller del dialogo
        playerController.update(delta);  // Aggiorna il movimento del giocatore

        // Aggiorna la telecamera per seguire il giocatore
        camera.update(player.getWorldX() + 0.5f, player.getWorldY() + 0.5f);

        // Cambia il mondo di gioco se necessario
        if (placeRenderer.getWorld() != gameState.getMapState().getCurrentPlace()) {
            World newPlace = gameState.getMapState().getCurrentPlace();
            placeRenderer.setWorld(newPlace);  // Imposta il nuovo posto
        }

        // Debug: Aggiunge oggetti all'inventario premendo il tasto NUM_5
        if (player.hasInventory() && Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            player.addItem("masterball");
            player.addItem("spicybait");
            player.addItem("herbalperfume");
            player.addItem("advancedtrap");
        }

        // Apre l'inventario quando si preme Z
        if (player.hasInventory() && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            openInventory();
        }

        // Salva il gioco quando si preme S
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            try {
                SaveManager.saveGame(gameState.getMapState().getCurrentPlace(), player);
            } catch (IOException e) {
                throw new RuntimeException(e);  // Gestisce errori di salvataggio
            }
        }

        batch.begin();  // Inizia il rendering
        placeRenderer.render(batch, camera);  // Rende il mondo di gioco
        batch.end();  // Termina il rendering

        uiStage.act(delta);  // Aggiorna la scena dell'interfaccia utente
        uiStage.draw();  // Disegna l'interfaccia utente
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);  // Imposta la matrice di proiezione per il batch
        uiStage.getViewport().update(width / uiScale, height / uiScale, true);  // Ridimensiona l'interfaccia utente
        gameViewport.update(width, height);  // Ridimensiona il viewport del gioco
    }

    @Override
    public void pause() {
        // Gestisce la pausa del gioco (non implementato in questo caso)
    }

    @Override
    public void resume() {
        // Gestisce il riavvio del gioco (non implementato in questo caso)
    }

    @Override
    public void hide() {
        // Salva la posizione sicura del giocatore quando la schermata è nascosta
        gameState.getPlayerState().setSafeCoords(player.getX(), player.getY());
        playerController.resetButtonsAndTimers();  // Resetta i tasti e i timer
        player.setState(Actor.ACTOR_STATE.STILL);  // Imposta lo stato del giocatore a STILL
        Gdx.input.setInputProcessor(null);  // Rimuove il processore di input
    }

    @Override
    public void dispose() {
        batch.dispose();  // Rilascia il batch per il rendering
        uiStage.dispose();  // Rilascia la scena dell'interfaccia utente
    }

    private void openInventory() {
        // Cambia la schermata e apre l'inventario
        gameState.getGame().setScreen(new InventoryScreen(gameState));
    }

    private void initUI() {
        // Inizializza la scena dell'interfaccia utente
        uiStage = new Stage(new ScreenViewport());
        uiStage.getViewport().update(Gdx.graphics.getWidth() / uiScale, Gdx.graphics.getHeight() / uiScale, true);

        root = new Table();  // Crea una tabella radice per l'interfaccia
        root.setFillParent(true);
        uiStage.addActor(root);

        dialogueBox = new DialogueBox(skin);  // Crea la casella di dialogo
        dialogueBox.setVisible(false);  // Nasconde la casella di dialogo inizialmente
        dialogueBox.setFillParent(false);

        optionsBox = new OptionBox(skin);  // Crea la casella delle opzioni
        optionsBox.setVisible(false);  // Nasconde le opzioni inizialmente

        // Crea la tabella per i dialoghi
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

        root.add(dialogTable).expand().align(Align.bottom);  // Aggiunge la tabella alla scena
    }
}
