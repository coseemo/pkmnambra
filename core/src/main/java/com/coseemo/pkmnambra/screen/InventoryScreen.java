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
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.util.states.GameState;

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

        batch = new SpriteBatch();
        font = new BitmapFont();  // Default font
        titleFont = new BitmapFont();  // Title font (larger size)
        shapeRenderer = new ShapeRenderer();

        backgroundTexture = new Texture(Gdx.files.internal("assets/background/paper.jpg"));

        // Scale fonts for larger text
        font.getData().setScale(1.5f);
        titleFont.getData().setScale(2f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        player = gameState.getPlayerState().getPlayer();
        // Disable input processor to avoid unwanted interactions
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Draw the inventory and catch list
        drawInventory();
        drawCatchList();

        batch.end();

        // Handle exiting the inventory
        if (Gdx.input.isKeyJustPressed(Keys.Z)) {
            game.setScreen(new GameScreen(gameState));
        }
    }

    private void drawInventory() {
        float x = 50;
        float y = Gdx.graphics.getHeight() - 50; // Start near the top of the screen
        float lineHeight = 40; // Increased line spacing for larger text

        // Draw Inventory Title
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "INVENTORY", x, y);
        y -= 60;

        // Draw Inventory Items
        Map<Item, Integer> itemsWithQuantity = gameState.getPlayerState().getPlayer().getInventory().getItems();
        font.setColor(Color.BLACK);
        if (itemsWithQuantity.isEmpty()) {
            font.draw(batch, "Is empty.", x, y);
        } else {
            for (Map.Entry<Item, Integer> entry : itemsWithQuantity.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();
                String itemText = item.getName() + ":" + count;
                font.draw(batch, itemText, x, y);
                y -= lineHeight;
            }
        }
    }

    private void drawCatchList() {
        float x = (float) Gdx.graphics.getWidth() / 2 + 50;  // Position the catch list on the right
        float y = Gdx.graphics.getHeight() - 50;
        float lineHeight = 40; // Increased line spacing for larger text

        // Draw Catch List Title
        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, "TO CATCH", x, y);
        y -= 60;

        // Draw Catch List
        Map<String, Boolean> toCatchMap = gameState.getPlayerState().getPlayer().getToCatch();
        font.setColor(Color.BLACK);
        if (toCatchMap == null || toCatchMap.isEmpty()) {
            font.draw(batch, "No Pokémon to catch.", x, y);
        } else {
            for (Map.Entry<String, Boolean> entry : toCatchMap.entrySet()) {
                String pokemonName = entry.getKey();
                boolean caught = entry.getValue();

                // Draw Pokémon name
                float textWidth = font.getScaleX() * pokemonName.length() * 10; // Estimate text width
                font.draw(batch, pokemonName, x, y);

                // Only draw the red line if the Pokémon is in the player's team (i.e., captured)
                if (!caught) {
                    batch.end(); // End batch to switch to ShapeRenderer
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Use Filled for thicker line
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rectLine(x, y - 10, x + textWidth, y - 10, 4); // Thicker red line
                    shapeRenderer.end();
                    batch.begin(); // Restart batch for further drawing
                }

                y -= lineHeight;
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        // Handle resize (if needed)
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
        font.dispose();
        titleFont.dispose();
        shapeRenderer.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
