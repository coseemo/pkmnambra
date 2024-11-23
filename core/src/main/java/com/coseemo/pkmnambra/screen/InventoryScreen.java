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
    private final SpriteBatch batch;  // Per il rendering delle immagini
    private final GameState gameState;  // Stato del gioco
    private final Game game;  // Il gioco stesso
    private Player player;  // Riferimento al giocatore
    private Texture backgroundTexture;  // Immagine di sfondo
    private BitmapFont font;  // Font per i testi generali
    private BitmapFont titleFont;  // Font per i titoli
    private ShapeRenderer shapeRenderer;  // Per disegnare forme come linee

    // Costruttore che imposta lo stato del gioco, il batch, i font, e lo sfondo
    public InventoryScreen(GameState gameState) {
        this.game = gameState.getGame();  // Imposta il gioco
        this.gameState = gameState;  // Imposta lo stato del gioco

        // Inizializzo gli oggetti di rendering
        batch = new SpriteBatch();
        font = new BitmapFont();  // Font per il testo
        titleFont = new BitmapFont();  // Font per i titoli
        shapeRenderer = new ShapeRenderer();  // Renderer per le forme

        // Carico lo sfondo
        backgroundTexture = new Texture(Gdx.files.internal("assets/background/paper.jpg"));

        // Ridimensiono i font per adattarli meglio all'interfaccia
        font.getData().setScale(1.5f);
        titleFont.getData().setScale(2f);
    }

    // Metodo chiamato quando la schermata diventa visibile
    @Override
    public void show() {
        // Disabilito temporaneamente l'input per evitare azioni indesiderate
        Gdx.input.setInputProcessor(null);
        player = gameState.getPlayerState().getPlayer();  // Ottengo il giocatore dallo stato del gioco
    }

    // Metodo per adattare il rendering alle dimensioni dello schermo
    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);  // Adatta la matrice di proiezione per il batch
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());  // Imposta la matrice di proiezione per il renderer di forme
    }

    // Metodo di rendering chiamato ad ogni frame
    @Override
    public void render(float delta) {
        // Pulisce lo schermo e imposta un colore di sfondo
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);  // Colore di sfondo azzurro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Pulisce il buffer dello schermo

        // Inizio il rendering
        batch.begin();

        // Disegno lo sfondo
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Disegno le varie sezioni dell'inventario
        drawInventory();
        drawCatchList();
        drawTeam();

        // Termino il rendering
        batch.end();

        // Gestisco la pressione del tasto Z per uscire dall'inventario
        if (Gdx.input.isKeyJustPressed(Keys.Z)) {
            game.setScreen(new GameScreen(gameState));  // Torna alla schermata di gioco
        }
    }

    // Metodo per disegnare l'inventario del giocatore
    private void drawInventory() {
        // Posiziono l'inventario sulla sinistra
        float x = 50;
        float y = Gdx.graphics.getHeight() - 50;  // Posizione vicino al bordo superiore
        float lineHeight = 40;  // Spaziatura tra le righe

        // Disegno il titolo "Inventario"
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "INVENTORY", x, y);
        y -= 60;  // Sposto il testo per lasciare spazio

        // Disegno gli oggetti nell'inventario
        Map<Item, Integer> itemsWithQuantity = gameState.getPlayerState().getPlayer().getInventory().getItems();
        font.setColor(Color.BLACK);  // Imposto il colore del font
        if (itemsWithQuantity.isEmpty()) {
            font.draw(batch, "Is empty.", x, y);  // Se l'inventario è vuoto, lo indico
        } else {
            // Disegno ogni oggetto e la sua quantità
            for (Map.Entry<Item, Integer> entry : itemsWithQuantity.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();
                font.draw(batch, item.getName() + ": " + count, x, y);
                y -= lineHeight;  // Sposto verso il basso per la prossima riga
            }
        }
    }

    // Metodo per disegnare la lista dei Pokémon da catturare
    private void drawCatchList() {
        // Posiziono la lista sulla destra
        float x = Gdx.graphics.getWidth() - 300;
        float y = Gdx.graphics.getHeight() - 50;
        float lineHeight = 40;  // Spaziatura tra le righe

        // Disegno il titolo "To Catch"
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "TO CATCH", x, y);
        y -= 60;  // Sposto il titolo verso il basso

        // Disegno la lista dei Pokémon da catturare
        Map<String, Boolean> toCatchMap = gameState.getPlayerState().getPlayer().getToCatch();
        font.setColor(Color.BLACK);
        if (toCatchMap == null || toCatchMap.isEmpty()) {
            font.draw(batch, "No Pokémon to catch.", x, y);  // Se non ci sono Pokémon da catturare
        } else {
            // Disegno ogni Pokémon e la sua situazione di cattura
            for (Map.Entry<String, Boolean> entry : toCatchMap.entrySet()) {
                String pokemonName = entry.getKey();
                boolean caught = entry.getValue();

                // Disegno il nome del Pokémon
                font.draw(batch, pokemonName, x, y);

                // Disegno una linea rossa se il Pokémon non è stato catturato
                if (!caught) {
                    batch.end();  // Termino il batch per usare il ShapeRenderer
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rectLine(x, y - 10, x + font.getScaleX() * pokemonName.length() * 10, y - 10, 4);  // Disegno la linea
                    shapeRenderer.end();
                    batch.begin();  // Riprendo il batch per il prossimo disegno
                }

                y -= lineHeight;  // Sposto verso il basso per il prossimo Pokémon
            }
        }
    }

    // Metodo per disegnare la squadra del giocatore
    private void drawTeam() {
        // Posiziono la squadra in basso al centro
        float y = 70;  // Altezza in cui disegnare la squadra
        float screenWidth = Gdx.graphics.getWidth();
        float spacing = 150;  // Spaziatura tra i Pokémon
        float startX = (screenWidth - (spacing * player.getTeam().size())) / 2;  // Calcolo la posizione iniziale al centro

        // Disegno il titolo "Squadra"
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "SQUADRA", screenWidth / 2f - 60, y + 40);

        // Disegno ogni Pokémon della squadra
        font.setColor(Color.BLACK);
        for (String pokemon : player.getTeam()) {
            font.draw(batch, pokemon, startX, y);
            startX += spacing;  // Sposto a destra per il prossimo Pokémon
        }
    }

    // I metodi seguenti non sono utilizzati in questa schermata
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    // Metodo per liberare le risorse quando la schermata viene chiusa
    @Override
    public void dispose() {
        batch.dispose();  // Rilascia il batch
        font.dispose();  // Rilascia il font
        titleFont.dispose();  // Rilascia il font del titolo
        shapeRenderer.dispose();  // Rilascia il renderer di forme
        if (backgroundTexture != null) {
            backgroundTexture.dispose();  // Rilascia la texture di sfondo
        }
    }
}
