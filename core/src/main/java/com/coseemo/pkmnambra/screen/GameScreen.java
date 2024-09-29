package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.Settings;
import com.coseemo.pkmnambra.controller.PlayerController;
import com.coseemo.pkmnambra.models.Actor;
import com.coseemo.pkmnambra.models.Camera;
import com.coseemo.pkmnambra.models.TERRAIN;
import com.coseemo.pkmnambra.models.TileMap;
import com.coseemo.pkmnambra.util.AnimationSet;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture mimi_standing_south;
    private Texture sand1;
    private Texture sand2;
    private Camera camera;
    private TileMap map;
    private Actor player;
    private PlayerController controller;

    @Override
    public void show() {
        mimi_standing_south = new Texture("assets/sprites/player/mimi_standing_south.png");
        sand1 = new Texture("assets/tiles/sands/sand1.png");
        sand2 = new Texture("assets/tiles/sands/sand2.png");

        AssetManager assetManager = ServiceLocator.getAssetManager();
        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);

        AnimationSet animations = new AnimationSet(
            new Animation<>(0.3f/2f, atlas.findRegions("mimi_walking_east"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f/2f, atlas.findRegions("mimi_walking_west"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f/2f, atlas.findRegions("mimi_walking_north"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f/2f, atlas.findRegions("mimi_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
            atlas.findRegion("mimi_standing_east"),
            atlas.findRegion("mimi_standing_west"),
            atlas.findRegion("mimi_standing_north"),
            atlas.findRegion("mimi_standing_south")
        );

        camera = new Camera();
        map = new TileMap(10,10);
        player = new Actor(map, 0, 0, animations);
        batch = new SpriteBatch();

        controller = new PlayerController(player);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {

        // Pulisci lo schermo
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1); // Colore di sfondo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controller.update(delta);
        player.update(delta);
        camera.update(player.getWorldX()+0.5f, player.getWorldY()+0.5f);


        // Inizia il rendering
        batch.begin();
        float worldStartX = (float) Gdx.graphics.getWidth() /2 - camera.getX()*Settings.SCALED_TILE_SIZE;
        float worldStartY = (float) Gdx.graphics.getHeight() /2 - camera.getY()*Settings.SCALED_TILE_SIZE;


        // Disegna la mappa
        for(int x = 0; x < map.getWidth(); ++x) {
            for(int y = 0; y < map.getHeight(); ++y) {
                Texture render;
                if (map.getTile(x, y).getTerrain() == TERRAIN.SAND_1) {
                    render = sand1;
                } else {
                    render = sand2;
                }
                batch.draw(render,
                    worldStartX + x * Settings.SCALED_TILE_SIZE,
                    worldStartY + y * Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE);
            }
        }

        // Logica di rendering del gioco
        updateGame(delta);

        batch.draw(player.getSprite(),
            worldStartX+player.getWorldX() * Settings.SCALED_TILE_SIZE,
            worldStartY+player.getWorldY() * Settings.SCALED_TILE_SIZE,
            Settings.SCALED_TILE_SIZE,
            Settings.SCALED_TILE_SIZE);

        // Fine del rendering
        batch.end();
    }

    private void updateGame(float delta) {
        // Aggiorna la logica del gioco
        // Ad esempio, gestisci il movimento dei personaggi, controlla le collisioni, ecc.
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
        batch.dispose();
        mimi_standing_south.dispose();
        sand1.dispose();
        sand2.dispose();
    }
}
