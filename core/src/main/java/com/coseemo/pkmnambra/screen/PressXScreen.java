package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PressXScreen implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont font;
    private float alpha;
    private boolean fadingIn;

    public PressXScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("assets/background/amber.png");
        font = new BitmapFont(); // Puoi caricare un font personalizzato
        alpha = 0;
        fadingIn = true;
    }

    @Override
    public void render(float delta) {
        // Gestione fade-in e fade-out
        if (fadingIn) {
            alpha += delta;
            if (alpha >= 1) {
                alpha = 1;
                fadingIn = false;
            }
        } else {
            alpha -= delta;
            if (alpha <= 0) {
                alpha = 0;
                fadingIn = true;
            }
        }

        // Clear dello schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Disegno background e testo
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.setColor(1, 1, 1, alpha); // Cambia trasparenza
        font.draw(batch, "Press X", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        batch.end();

        // Cambia schermata se il giocatore preme X
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            game.setScreen(new TransitionScreen(game, this, new MenuScreen(game)));
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        font.dispose();
    }
}
