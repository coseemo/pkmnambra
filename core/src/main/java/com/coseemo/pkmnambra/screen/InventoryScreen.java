package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.itemfactory.Item;
import com.coseemo.pkmnambra.singleton.GameState;

import java.util.Map;

public class InventoryScreen implements Screen {
    private final SpriteBatch batch;
    private final GameState gameState;
    private final Game game;
    private Player player;
    private Texture backgroundTexture;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ShapeRenderer shapeRenderer;

    public InventoryScreen(GameState gameState) {
        this.game = gameState.getGame();
        this.gameState = gameState;

        // Creo il batch e gli strumenti di rendering
        batch = new SpriteBatch();
        font = new BitmapFont();  // Font predefinito
        titleFont = new BitmapFont();  // Font per i titoli
        shapeRenderer = new ShapeRenderer();

        // Imposto lo sfondo
        backgroundTexture = new Texture(Gdx.files.internal("assets/background/paper.jpg"));

        // Ridimensiono i font
        font.getData().setScale(1.5f);
        titleFont.getData().setScale(2f);
    }

    @Override
    public void show() {
        // Disabilito l'input per evitare interazioni indesiderate
        Gdx.input.setInputProcessor(null);
        player = gameState.getPlayerState().getPlayer();
    }

    @Override
    public void resize(int width, int height) {
        // Adatto il rendering alle dimensioni dello schermo
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    }

    @Override
    public void render(float delta) {
        // Pulisco lo schermo
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inizio il rendering
        batch.begin();

        // Disegno lo sfondo
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Disegno le varie sezioni
        drawInventory();
        drawCatchList();
        drawTeam();

        // Termino il rendering del batch
        batch.end();

        // Gestisco l'uscita dall'inventario premendo Z
        if (Gdx.input.isKeyJustPressed(Keys.Z)) {
            game.setScreen(new GameScreen(gameState));
        }
    }

    private void drawInventory() {
        // Posiziono l'inventario sulla sinistra
        float x = 50;
        float y = Gdx.graphics.getHeight() - 50; // Parto vicino al bordo superiore
        float lineHeight = 40; // Spaziatura tra le righe

        // Disegno il titolo "Inventario"
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "INVENTORY", x, y);
        y -= 60;

        // Disegno gli oggetti dell'inventario
        Map<Item, Integer> itemsWithQuantity = gameState.getPlayerState().getPlayer().getInventory().getItems();
        font.setColor(Color.BLACK);
        if (itemsWithQuantity.isEmpty()) {
            font.draw(batch, "Is empty.", x, y);
        } else {
            for (Map.Entry<Item, Integer> entry : itemsWithQuantity.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();
                font.draw(batch, item.getName() + ": " + count, x, y);
                y -= lineHeight;
            }
        }
    }

    private void drawCatchList() {
        // Posiziono la lista dei Pokémon da catturare sulla destra
        float x = Gdx.graphics.getWidth() - 300;
        float y = Gdx.graphics.getHeight() - 50;
        float lineHeight = 40;

        // Disegno il titolo "To Catch"
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "TO CATCH", x, y);
        y -= 60;

        // Disegno la lista dei Pokémon
        Map<String, Boolean> toCatchMap = gameState.getPlayerState().getPlayer().getToCatch();
        font.setColor(Color.BLACK);
        if (toCatchMap == null || toCatchMap.isEmpty()) {
            font.draw(batch, "No Pokémon to catch.", x, y);
        } else {
            for (Map.Entry<String, Boolean> entry : toCatchMap.entrySet()) {
                String pokemonName = entry.getKey();
                boolean caught = entry.getValue();

                // Disegno il nome del Pokémon
                font.draw(batch, pokemonName, x, y);

                // Disegno una linea rossa se il Pokémon non è stato catturato
                if (!caught) {
                    batch.end(); // Termino il batch per usare ShapeRenderer
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rectLine(x, y - 10, x + font.getScaleX() * pokemonName.length() * 10, y - 10, 4);
                    shapeRenderer.end();
                    batch.begin(); // Riprendo il batch per disegni successivi
                }

                y -= lineHeight;
            }
        }
    }

    private void drawTeam() {
        // Posiziono la squadra in basso al centro dello schermo
        float y = 70; // Altezza dei Pokémon
        float screenWidth = Gdx.graphics.getWidth();
        float spacing = 150; // Spaziatura tra i Pokémon
        float startX = (screenWidth - (spacing * player.getTeam().size())) / 2; // Calcolo la posizione iniziale

        // Disegno il titolo "Squadra"
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "SQUADRA", screenWidth / 2f - 60, y + 40);

        // Disegno i Pokémon
        font.setColor(Color.BLACK);
        for (String pokemon : player.getTeam()) {
            font.draw(batch, pokemon, startX, y);
            startX += spacing; // Sposto a destra per il prossimo Pokémon
        }
    }

    @Override
    public void pause() {
        // Non faccio nulla in pausa
    }

    @Override
    public void resume() {
        // Non faccio nulla nel resume
    }

    @Override
    public void hide() {
        // Non faccio nulla nel hide
    }

    @Override
    public void dispose() {
        // Libero le risorse
        batch.dispose();
        font.dispose();
        titleFont.dispose();
        shapeRenderer.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
