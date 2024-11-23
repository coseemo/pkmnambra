package com.coseemo.pkmnambra.capture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coseemo.pkmnambra.itemfactory.Item;
import com.coseemo.pkmnambra.pokemonfactory.Pokemon;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.captureobserver.CaptureEventNotifier;
import com.coseemo.pkmnambra.singleton.GameState;
import com.coseemo.pkmnambra.util.SkinGenerator;

public class CaptureRenderer {
    private final Stage stage;
    private final Table root;
    private final Skin skin;
    private final GameState gameState;
    private OptionBox optionBox;
    private ProgressBar captureProbabilityBar;
    private ProgressBar angerLevelBar;
    private Label statusMessageLabel;
    private final SpriteBatch batch;
    private final Texture background;
    private final Texture pokemonSprite;
    private final Texture mimiSprite;

    // Costruttore che inizializza gli oggetti e carica le risorse
    public CaptureRenderer(Pokemon pokemon, GameState gameState) {
        this.gameState = gameState;
        this.stage = new Stage(new ScreenViewport());
        this.batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal("assets/background/beach.png"));
        this.pokemonSprite = new Texture(Gdx.files.internal("assets/sprites/pokemonbattle/" + pokemon.getName() + ".png"));
        this.mimiSprite = new Texture(Gdx.files.internal("assets/sprites/pokemonbattle/mimi.png"));

        AssetManager assetManager = gameState.getResourceManager().getAssetManager();
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.finishLoading();

        skin = SkinGenerator.generateSkin(gameState.getResourceManager().getAssetManager());

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Creo una tabella contenitore per una migliore disposizione
        Table container = new Table();
        container.setFillParent(true);
        stage.addActor(container);

        // Aggiungo la sezione Pokémon a sinistra in alto
        container.add(createLeftPokemonSection()).left().top().padLeft(10f).padTop(10f);
        container.add().expandX();

        // Creo una nuova riga per il menu a destra
        container.row();
        container.add().expandY();
        container.add(createRightMainMenu(gameState.getEventManager().getEventNotifier())).right().bottom().padRight(10f).padBottom(10f);
    }

    // Creo il menu principale a destra con le opzioni
    private Table createRightMainMenu(CaptureEventNotifier eventNotifier) {
        Table rightMainMenuTable = new Table();
        optionBox = new OptionBox(skin);
        optionBox.addOption("Throw Bait");
        optionBox.addOption("Use Perfume");
        optionBox.addOption("Set Trap");
        optionBox.addOption("Use PokeBall");
        optionBox.addOption("Run Away");

        rightMainMenuTable.add(optionBox).width(150).height(200);
        return rightMainMenuTable;
    }

    // Crea il menu secondario per aggiornare le opzioni dell'inventario
    private Table createSecondaryRightMenu() {
        Table rightSecondaryMenuTable = new Table();
        if (optionBox == null) {
            optionBox = new OptionBox(skin);
        } else {
            optionBox.clearChoices();
        }

        rightSecondaryMenuTable.add(optionBox).width(150).height(200);
        return rightSecondaryMenuTable;
    }

    // Aggiorno le opzioni dell'optionBox in base alla categoria di oggetti
    public void updateInventoryOptions(String category) {
        if (optionBox == null) {
            optionBox = new OptionBox(skin);
        }
        optionBox.clearChoices();

        for (Item item : gameState.getPlayerState().getPlayer().getInventory().getItems().keySet()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                optionBox.addOption(item.getName() + " " + gameState.getPlayerState().getPlayer().getInventory().getItems().get(item));
            }
        }

        optionBox.setVisible(!optionBox.getChoices().isEmpty());
    }

    // Crea la sezione sinistra con le barre di stato e messaggi
    private Table createLeftPokemonSection() {
        Table leftPokemonTable = new Table();
        Table statusBarBox = new Table();
        statusBarBox.setBackground(skin.getDrawable("battleinfobox"));
        statusBarBox.pad(10f);

        createCaptureProbabilityBar(statusBarBox);
        createAngerLevelBar(statusBarBox);
        createStatusMessageLabel(statusBarBox);

        leftPokemonTable.add(statusBarBox).width(180).height(120);
        return leftPokemonTable;
    }

    // Crea la barra di probabilità di cattura
    private void createCaptureProbabilityBar(Table statusBarBox) {
        captureProbabilityBar = new ProgressBar(0, 100, 1, false, createProgressBarStyle("green"));
        statusBarBox.add(new Label("Capture:", skin)).padBottom(2f).left();
        statusBarBox.row();
        statusBarBox.add(captureProbabilityBar).width(150).height(15).pad(2f).left().row();
    }

    // Crea la barra del livello di rabbia
    private void createAngerLevelBar(Table statusBarBox) {
        angerLevelBar = new ProgressBar(0, 100, 1, false, createProgressBarStyle("red"));
        statusBarBox.add(new Label("Anger:", skin)).padBottom(2f).left();
        statusBarBox.row();
        statusBarBox.add(angerLevelBar).width(150).height(15).pad(2f).left().row();
    }

    // Crea l'etichetta per i messaggi di stato
    private void createStatusMessageLabel(Table statusBarBox) {
        statusMessageLabel = new Label("", skin);
        statusMessageLabel.setWrap(false);
        statusMessageLabel.setWidth(160);
        statusMessageLabel.setHeight(40);
        statusBarBox.row();
        statusBarBox.add(statusMessageLabel).padTop(5f).left();
    }

    // Crea lo stile per la barra di progresso
    private ProgressBar.ProgressBarStyle createProgressBarStyle(String color) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = skin.getDrawable("hpbar_bar");
        style.knobBefore = skin.getDrawable(color);
        style.knobBefore.setMinHeight(3);
        return style;
    }

    // Metodo per renderizzare la schermata
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(mimiSprite, -150, -80, mimiSprite.getWidth() * 0.5f, mimiSprite.getHeight() * 0.5f);

        float shadowX = 250;
        float shadowY = 180;
        batch.setColor(0, 0, 0, 0.5f);
        batch.draw(pokemonSprite, shadowX - pokemonSprite.getWidth() * 3f / 6, shadowY - pokemonSprite.getHeight() * 3f / 10, pokemonSprite.getWidth() * 3f, pokemonSprite.getHeight() * 3f / 3);

        batch.setColor(1, 1, 1, 1);
        batch.draw(pokemonSprite, 200, 100, pokemonSprite.getWidth() * 3f, pokemonSprite.getHeight() * 3f);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    // Pulisce le risorse
    public void dispose() {
        stage.dispose();
        batch.dispose();
        background.dispose();
        pokemonSprite.dispose();
    }

    // Metodo per aggiornare la probabilità di cattura
    public void updateCaptureProbability(float value) {
        captureProbabilityBar.setValue(value);
    }

    // Metodo per aggiornare il livello di rabbia
    public void updateAngerLevel(float value) {
        angerLevelBar.setValue(value);
    }

    // Metodo per aggiornare il messaggio di stato
    public void updateStatusMessage(String message) {
        statusMessageLabel.setText(message);
    }

    public OptionBox getOptionBox() {
        return optionBox;
    }
}
