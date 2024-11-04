package com.coseemo.pkmnambra.screen.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.util.EventNotifier;
import com.coseemo.pkmnambra.util.GameState;
import com.coseemo.pkmnambra.util.ServiceLocator;
import com.coseemo.pkmnambra.util.SkinGenerator;

public class CaptureRenderer {
    private Stage stage;
    private Table root;
    private Skin skin;
    private GameState gameState;
    private OptionBox optionBox;
    private ProgressBar captureProbabilityBar;
    private ProgressBar angerLevelBar;
    private Label statusMessageLabel;
    private SpriteBatch batch;
    private Texture background;
    private Texture pokemonSprite;
    private Texture mimiSprite;

    public CaptureRenderer() {

        this.gameState = GameState.getInstance();
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("assets/background/beach.png"));
        pokemonSprite = new Texture(Gdx.files.internal("assets/sprites/pokemonbattle/parasect.png"));
        mimiSprite = new Texture(Gdx.files.internal("assets/sprites/pokemonbattle/mimi.png"));

        skin = SkinGenerator.generateSkin(ServiceLocator.getAssetManager());

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Create a container table for better positioning
        Table container = new Table();
        container.setFillParent(true);
        stage.addActor(container);

        // Add the left section to the top-left
        container.add(createLeftPokemonSection()).left().top().padLeft(10f).padTop(10f);
        container.add().expandX();

        // Create a new row for the right menu
        container.row();
        // Expand the space above the right menu
        container.add().expandY();
        // Add the right menu to the bottom-right
        container.add(createRightMainMenu(gameState.getEventNotifier())).right().bottom().padRight(10f).padBottom(10f);
    }

    private Table createRightMainMenu(EventNotifier eventNotifier) {
        Table rightMainMenuTable = new Table();


        optionBox = new OptionBox(skin);
        optionBox.addOption("Throw Bait");
        optionBox.addOption("Use Perfume");
        optionBox.addOption("Set Trap");
        optionBox.addOption("Use PokeBall");
        optionBox.addOption("Run Away");

        // Reduced width and height for the option box
        rightMainMenuTable.add(optionBox).width(150).height(200);
        return rightMainMenuTable;
    }

    private Table createSecondaryRightMenu() {
        Table rightSecondaryMenuTable = new Table();

        // Controllo per evitare duplicati di opzioni
        if (optionBox == null) {
            optionBox = new OptionBox(skin);
        } else {
            optionBox.clearChoices();
        }

        // Aggiungiamo qui le opzioni dinamicamente tramite `updateInventoryOptions`
        rightSecondaryMenuTable.add(optionBox).width(150).height(200);
        return rightSecondaryMenuTable;
    }

    // Metodo per aggiornare l'optionBox con gli oggetti dell'inventario
    public void updateInventoryOptions(String category) {
        if (optionBox == null) {
            optionBox = new OptionBox(skin);
        }

        optionBox.clearChoices();

        // Aggiungi solo gli oggetti della categoria specificata
        for (Item item : gameState.getPlayer().getInventory().getItemList()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                optionBox.addOption(item.getName() + " " + gameState.getPlayer().getInventory().getItemQuantity(item));
            }
        }

        optionBox.setVisible(!optionBox.getChoices().isEmpty()); // Mostra il menu solo se ci sono elementi
    }

    private Table createLeftPokemonSection() {
        Table leftPokemonTable = new Table();

        // Box for the status bars and messages - reduced size
        Table statusBarBox = new Table();
        statusBarBox.setBackground(skin.getDrawable("battleinfobox"));
        statusBarBox.pad(10f); // Reduced padding

        // Add the bars and status message
        createCaptureProbabilityBar(statusBarBox);
        createAngerLevelBar(statusBarBox);
        createStatusMessageLabel(statusBarBox);

        // Reduced width and height for the status box
        leftPokemonTable.add(statusBarBox).width(180).height(120);
        return leftPokemonTable;
    }

    private void createCaptureProbabilityBar(Table statusBarBox) {
        captureProbabilityBar = new ProgressBar(0, 100, 1, false, createProgressBarStyle("green"));
        statusBarBox.add(new Label("Capture:", skin)).padBottom(2f).left(); // Shortened label
        statusBarBox.row();
        statusBarBox.add(captureProbabilityBar).width(150).height(15).pad(2f).left().row(); // Reduced dimensions
    }

    private void createAngerLevelBar(Table statusBarBox) {
        angerLevelBar = new ProgressBar(0, 100, 1, false, createProgressBarStyle("red"));
        statusBarBox.add(new Label("Anger:", skin)).padBottom(2f).left(); // Shortened label
        statusBarBox.row();
        statusBarBox.add(angerLevelBar).width(150).height(15).pad(2f).left().row(); // Reduced dimensions
    }

    private void createStatusMessageLabel(Table statusBarBox) {
        statusMessageLabel = new Label("", skin);
        statusMessageLabel.setWrap(false); // Impedisce il wrapping del testo
        statusMessageLabel.setWidth(160); // Imposta larghezza fissa
        statusMessageLabel.setHeight(40); // Imposta altezza fissa
        statusBarBox.row();
        statusBarBox.add(statusMessageLabel).padTop(5f).left(); // Rimuovi la larghezza da qui
    }


    private ProgressBar.ProgressBarStyle createProgressBarStyle(String color) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = skin.getDrawable("hpbar_bar");
        style.knobBefore = skin.getDrawable(color);
        style.knobBefore.setMinHeight(3); // Imposta l'altezza desiderata
        return style;
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float spriteWidth = mimiSprite.getWidth() * 0.5f;
        float spriteHeight = mimiSprite.getHeight() * 0.5f;
        batch.draw(mimiSprite, -150, -80, spriteWidth, spriteHeight);

        // Aggiungi qui l'ombra per il Pokémon
        float spriteWidth1 = pokemonSprite.getWidth() * 3f;
        float spriteHeight1 = pokemonSprite.getHeight() * 3f;

        // Imposta la posizione dell'ombra (poco sotto lo sprite)
        float shadowX = 250;
        float shadowY = 180; // Posiziona l'ombra sotto lo sprite, puoi regolare questa posizione

        // Disegna l'ombra
        batch.setColor(0, 0, 0, 0.5f); // Colore dell'ombra con trasparenza
        batch.draw(pokemonSprite, shadowX - spriteWidth1 / 6, shadowY - spriteHeight1 / 10, spriteWidth1, spriteHeight1 / 3); // Dimensione dell'ombra

        // Ripristina il colore per disegnare lo sprite
        batch.setColor(1, 1, 1, 1);
        batch.draw(pokemonSprite, 200, 100, spriteWidth1, spriteHeight1);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    // Rest of the methods remain unchanged
    public void dispose() {
        stage.dispose();
        batch.dispose();
        background.dispose();
        pokemonSprite.dispose();
    }

    public void updateCaptureProbability(float value) {
        captureProbabilityBar.setValue(value);
    }

    public void updateAngerLevel(float value) {
        angerLevelBar.setValue(value);
    }

    public void updateStatusMessage(String message) {
        statusMessageLabel.setText(message);
    }

    public OptionBox getOptionBox() {
        return optionBox;
    }
}
