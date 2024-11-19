package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.util.states.GameState;
import com.coseemo.pkmnambra.util.Observer;
import com.coseemo.pkmnambra.util.SkinGenerator;

import java.util.Map;

public class InventoryScreen implements Screen, Observer {
    private final Stage uiStage;
    private Table root;
    private final GameState gameState;
    private final Skin skin;
    private final Game game; // Aggiungi un riferimento a Game

    public InventoryScreen(GameState gameState) {
        this.game = gameState.getGame(); // Inizializza il riferimento a Game
        this.gameState = GameState.getInstance();
        gameState.getEventNotifier().registerObserver(this);
        uiStage = new Stage(new ScreenViewport());
        skin = SkinGenerator.generateSkin(gameState.getAssetManager());

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
        if (gameState.getPlayer().getInventory().getItemsWithQuantity().isEmpty()) {
            Label emptyLabel = new Label("L'inventario è vuoto.", skin);
            root.add(emptyLabel).center();
        } else {
            // Recupera gli oggetti e le loro quantità
            Map<Item, Integer> itemsWithQuantity = gameState.getPlayer().getInventory().getItemsWithQuantity();

            // Mostra ogni oggetto una sola volta con la quantità
            for (Map.Entry<Item, Integer> entry : itemsWithQuantity.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();
                Label itemLabel = new Label(item.getName() + ": " + item.getDescription() + " (Quantità: " + count + ")", skin);
                root.add(itemLabel).expandX().pad(10);
                root.row();
            }
        }
    }


    @Override
    public void render(float delta) {
        // Gestisci l'uscita dall'inventario
        if (Gdx.input.isKeyJustPressed(Keys.Z)) { // Puoi cambiare ESCAPE con un altro tasto

            game.setScreen(new GameScreen(gameState)); // Torna al GameScreen o alla schermata principale
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
        uiStage.dispose();
    }

    @Override
    public void update(String eventType) {

    }
}
