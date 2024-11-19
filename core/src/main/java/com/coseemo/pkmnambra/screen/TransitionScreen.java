package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TransitionScreen implements Screen {
    private final Screen fromScreen;
    private final Screen toScreen;
    private final Game game;
    private float progress; // Valore da 0.0 a 1.0
    private boolean transitionComplete;
    private SpriteBatch batch;
    private BitmapFont font;

    public TransitionScreen(Game game, Screen fromScreen, Screen toScreen) {
        this.game = game;
        this.fromScreen = fromScreen;
        this.toScreen = toScreen;
        this.progress = 0;
        this.transitionComplete = false;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(); // Può essere personalizzata
    }

    @Override
    public void show() {
        progress = 0;
        transitionComplete = false;
    }

    @Override
    public void render(float delta) {
        // Aggiorna il progresso della transizione
        progress += delta * 0.5f; // Modifica la velocità qui

        if (progress < 0.5f) {
            fromScreen.render(delta); // Solo per la prima metà
        } else {
            toScreen.render(delta);  // Dopo la metà della transizione
            game.setScreen(toScreen);
        }

        // Clear dello schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Disegna una schermata intermedia (ad esempio un fade)
        batch.begin();
        font.draw(batch, "Transizione in corso...", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        batch.end();
    }
    @Override
    public void resize(int width, int height) {
        fromScreen.resize(width, height);
        toScreen.resize(width, height);
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
    }
}
