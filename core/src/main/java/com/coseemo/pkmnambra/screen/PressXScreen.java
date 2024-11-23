package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class PressXScreen implements Screen {
    private static final float VIRTUAL_WIDTH = 1280;
    private static final float VIRTUAL_HEIGHT = 720;
    private static final float BOTTOM_MARGIN = 50f; // Margine dal fondo dello schermo

    private final Game game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont font;
    private float alpha;
    private boolean fadingIn;
    private Vector2 textPosition;
    private GlyphLayout glyphLayout;
    private Viewport viewport;

    private float backgroundX;
    private float backgroundY;
    private float backgroundWidth;
    private float backgroundHeight;

    public PressXScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("assets/background/amber.png");
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont();
        // Aumenta ancora di più la dimensione del font
        font.getData().setScale(4.0f);  // Aumentato a 4.0f per un testo più grande
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        alpha = 0;
        fadingIn = true;

        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        glyphLayout = new GlyphLayout();
        textPosition = new Vector2();
        updateTextPosition();
        updateBackgroundSize();
    }

    @Override
    public void render(float delta) {
        updateFade(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.draw(background, backgroundX, backgroundY, backgroundWidth, backgroundHeight);

        font.setColor(1, 1, 1, alpha);
        font.draw(batch, "Press X", textPosition.x, textPosition.y);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            game.setScreen(new TransitionScreen(game, this, new MenuScreen(game)));
        }
    }

    private void updateFade(float delta) {
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
    }

    private void updateBackgroundSize() {
        float windowRatio = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;
        float textureRatio = (float)background.getWidth() / background.getHeight();

        if (windowRatio > textureRatio) {
            backgroundWidth = VIRTUAL_WIDTH;
            backgroundHeight = backgroundWidth / textureRatio;
            backgroundX = 0;
            backgroundY = (VIRTUAL_HEIGHT - backgroundHeight) / 2;
        } else {
            backgroundHeight = VIRTUAL_HEIGHT;
            backgroundWidth = backgroundHeight * textureRatio;
            backgroundX = (VIRTUAL_WIDTH - backgroundWidth) / 2;
            backgroundY = 0;
        }

        if (backgroundWidth < VIRTUAL_WIDTH) {
            float scale = VIRTUAL_WIDTH / backgroundWidth;
            backgroundWidth *= scale;
            backgroundHeight *= scale;
            backgroundX = (VIRTUAL_WIDTH - backgroundWidth) / 2;
        }
        if (backgroundHeight < VIRTUAL_HEIGHT) {
            float scale = VIRTUAL_HEIGHT / backgroundHeight;
            backgroundWidth *= scale;
            backgroundHeight *= scale;
            backgroundY = (VIRTUAL_HEIGHT - backgroundHeight) / 2;
        }
    }

    private void updateTextPosition() {
        glyphLayout.setText(font, "Press X");
        textPosition.x = (VIRTUAL_WIDTH - glyphLayout.width) / 2f;
        // Posiziona il testo in basso allo schermo
        textPosition.y = BOTTOM_MARGIN + glyphLayout.height;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        updateTextPosition();
        updateBackgroundSize();
    }

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
