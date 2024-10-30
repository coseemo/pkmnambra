package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys; // Importa Keys per usare i codici dei tasti
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.util.EventNotifier;
import com.coseemo.pkmnambra.util.Observer;
import com.coseemo.pkmnambra.util.ServiceLocator;
import com.coseemo.pkmnambra.util.SkinGenerator;

public class InventoryScreen implements Screen, Observer {
    private Stage uiStage;
    private Table root;
    private EventNotifier eventNotifier;
    private Inventory inventory;
    private Skin skin;
    private Game game; // Aggiungi un riferimento a Game

    public InventoryScreen(Game game, Inventory inventory, EventNotifier eventNotifier) {
        this.game = game; // Inizializza il riferimento a Game
        this.inventory = inventory;
        this.eventNotifier = eventNotifier;
        this.eventNotifier.registerObserver(this);
        uiStage = new Stage(new ScreenViewport());
        skin = SkinGenerator.generateSkin(ServiceLocator.getAssetManager());

        initUI();
    }

    private void initUI() {
        root = new Table();
        root.setFillParent(true);
        uiStage.addActor(root);
    }

    @Override
    public void show() {
        updateInventoryDisplay();
    }

    private void updateInventoryDisplay() {
        root.clear(); // Pulisci la tabella esistente
        if (inventory.getItems().isEmpty()) {
            Label emptyLabel = new Label("L'inventario è vuoto.", skin); // Usa Skin per il Label
            root.add(emptyLabel).center(); // Aggiungi l'etichetta al centro
        } else {
            for (Item item : inventory.getItems()) {
                Label itemLabel = new Label(item.getName() + ": " + item.getDescription(), skin); // Usa Skin per il Label
                root.add(itemLabel).expandX().pad(10);
                root.row();
            }
        }
    }

    @Override
    public void render(float delta) {
        // Gestisci l'uscita dall'inventario
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) { // Puoi cambiare ESCAPE con un altro tasto

            game.setScreen(new GameScreen((Main) game, eventNotifier)); // Torna al GameScreen o alla schermata principale
        }

        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        uiStage.dispose();
    }

    @Override
    public void update(String eventType) {

    }
}
