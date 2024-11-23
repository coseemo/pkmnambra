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

public class PressXScreen implements Screen {
    private static final float VIRTUAL_WIDTH = 1280;  // Larghezza virtuale dello schermo
    private static final float VIRTUAL_HEIGHT = 720;  // Altezza virtuale dello schermo
    private static final float BOTTOM_MARGIN = 50f;  // Margine dal fondo dello schermo per il testo

    private final Game game;  // Riferimento al gioco
    private SpriteBatch batch;  // Oggetto per il rendering delle immagini
    private Texture background;  // Texture di sfondo
    private BitmapFont font;  // Font per il testo
    private float alpha;  // Opacità del testo durante il fade
    private boolean fadingIn;  // Indica se l'animazione di fade è in entrata o in uscita
    private Vector2 textPosition;  // Posizione del testo sullo schermo
    private GlyphLayout glyphLayout;  // Layout del testo per calcolare la larghezza e l'altezza
    private Viewport viewport;  // Vista per gestire la proiezione

    private float backgroundX;  // Posizione orizzontale dello sfondo
    private float backgroundY;  // Posizione verticale dello sfondo
    private float backgroundWidth;  // Larghezza dello sfondo
    private float backgroundHeight;  // Altezza dello sfondo

    // Costruttore che imposta il gioco
    public PressXScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Inizializzazione delle risorse
        batch = new SpriteBatch();  // Inizializza il batch per il rendering
        background = new Texture("assets/background/amber.png");  // Carica lo sfondo
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);  // Imposta il filtro per la texture
        font = new BitmapFont();  // Inizializza il font
        font.getData().setScale(4.0f);  // Imposta una dimensione del font molto grande
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);  // Imposta il filtro per il font

        // Inizializza i parametri per l'animazione di fade
        alpha = 0;
        fadingIn = true;

        // Imposta la viewport con una risoluzione virtuale
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Inizializza il layout per calcolare la larghezza e l'altezza del testo
        glyphLayout = new GlyphLayout();
        textPosition = new Vector2();
        updateTextPosition();  // Calcola la posizione del testo
        updateBackgroundSize();  // Calcola la dimensione dello sfondo
    }

    @Override
    public void render(float delta) {
        // Aggiorna l'effetto di fade
        updateFade(delta);

        // Pulisce lo schermo con un colore nero
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Pulisce il buffer di colore
        viewport.apply();  // Applica la viewport

        // Imposta la matrice di proiezione del batch
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Inizia il rendering
        batch.begin();

        // Disegna lo sfondo
        batch.draw(background, backgroundX, backgroundY, backgroundWidth, backgroundHeight);

        // Imposta il colore del font (bianco con trasparenza regolata dal fade)
        font.setColor(1, 1, 1, alpha);
        font.draw(batch, "Press X", textPosition.x, textPosition.y);  // Disegna il testo "Press X"

        // Termina il rendering
        batch.end();

        // Se il tasto X è stato premuto, cambia schermata
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            game.setScreen(new TransitionScreen(game, this, new MenuScreen(game)));  // Passa alla schermata del menu con una transizione
        }
    }

    // Metodo che gestisce l'animazione di fade
    private void updateFade(float delta) {
        // Se il fade è in entrata, aumenta l'alpha
        if (fadingIn) {
            alpha += delta;
            if (alpha >= 1) {  // Quando alpha arriva a 1, cambia a fade out
                alpha = 1;
                fadingIn = false;
            }
        } else {  // Se il fade è in uscita, diminuisci l'alpha
            alpha -= delta;
            if (alpha <= 0) {  // Quando alpha arriva a 0, cambia a fade in
                alpha = 0;
                fadingIn = true;
            }
        }
    }

    // Metodo che aggiorna la dimensione e posizione dello sfondo per adattarlo alla finestra
    private void updateBackgroundSize() {
        float windowRatio = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;  // Calcola il rapporto della finestra virtuale
        float textureRatio = (float)background.getWidth() / background.getHeight();  // Calcola il rapporto della texture dello sfondo

        // Adatta lo sfondo alla finestra mantenendo le proporzioni
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

        // Se lo sfondo è più piccolo della finestra, lo scala per adattarlo
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

    // Metodo che aggiorna la posizione del testo in base alle dimensioni della finestra
    private void updateTextPosition() {
        glyphLayout.setText(font, "Press X");  // Calcola la larghezza e l'altezza del testo
        textPosition.x = (VIRTUAL_WIDTH - glyphLayout.width) / 2f;  // Centra il testo orizzontalmente
        textPosition.y = BOTTOM_MARGIN + glyphLayout.height;  // Posiziona il testo vicino al fondo dello schermo
    }

    @Override
    public void resize(int width, int height) {
        // Aggiorna la viewport quando la finestra viene ridimensionata
        viewport.update(width, height, true);
        updateTextPosition();  // Ricalcola la posizione del testo
        updateBackgroundSize();  // Ricalcola la posizione e dimensione dello sfondo
    }

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
        background.dispose();  // Rilascia la texture dello sfondo
        font.dispose();  // Rilascia il font
    }
}
