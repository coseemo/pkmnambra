package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coseemo.pkmnambra.Settings;
import com.coseemo.pkmnambra.controller.PlayerController;
import com.coseemo.pkmnambra.models.Player;

public class GameScreen extends BaseScreen {
    private SpriteBatch batch;
    private Texture slakoth_south_1;
    private Player player;

    @Override
    public void show() {
        slakoth_south_1 = new Texture("assets/sprites/slakoth/slakoth_south_1.png");
        player = new Player(0, 0);
        batch = new SpriteBatch();
        PlayerController controller = new PlayerController(player);
        Gdx.input.setInputProcessor(controller);
    }
    @Override
    public void render(float delta) {
        // Pulisci lo schermo
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1); // Colore di sfondo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Logica di rendering del gioco
        updateGame(delta);
        drawGame();
    }
    private void updateGame(float delta) {
        // Aggiorna la logica del gioco
        // Ad esempio, gestisci il movimento dei personaggi, controlla le collisioni, ecc.
    }
    private void drawGame() {
        batch.begin();
        batch.draw(slakoth_south_1, player.getX()* Settings.SCALED_TILE_SIZE, player.getY()*Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE);
        batch.end();
    }
    @Override
    public void resize(int width, int height) {
        // Gestisci il ridimensionamento della finestra, se necessario
    }
    @Override
    public void pause() {
        // Logica da eseguire quando il gioco è in pausa
    }
    @Override
    public void resume() {
        // Logica da eseguire quando il gioco riprende
    }
    @Override
    public void hide() {
        // Logica da eseguire quando la schermata viene nascosta
    }
    @Override
    public void dispose() {
        // Pulisci le risorse quando la schermata non è più necessaria
    }
}
