package com.coseemo.pkmnambra.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.coseemo.pkmnambra.Settings;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.camera.Camera;
import com.coseemo.pkmnambra.actors.Actor;
import com.coseemo.pkmnambra.maplogic.*;
import com.coseemo.pkmnambra.mapobjectfactory.WorldObject;
import com.coseemo.pkmnambra.singleton.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceRenderer {

    private final AssetManager assetManager;
    private World world;
    private final TextureRegion sand1;
    private final TextureRegion sand2;
    private final TextureRegion floor;
    private final List<Integer> renderedObjects = new ArrayList<Integer>();
    private final List<YSortable> forRendering = new ArrayList<YSortable>();

    public PlaceRenderer(GameState gameState) {
        this.assetManager = gameState.getResourceManager().getAssetManager();
        this.world = gameState.getMapState().getCurrentPlace();

        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        TextureAtlas atlas = assetManager.get("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        TextureAtlas atlas1 = assetManager.get("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        sand1 = atlas.findRegion("sand1");
        sand2 = atlas.findRegion("sand2");
        floor = atlas1.findRegion("interno");
    }

    public void render(SpriteBatch batch, Camera camera) {
        float placeStartX = Gdx.graphics.getWidth() / 2 - camera.getX() * Settings.SCALED_TILE_SIZE;
        float placeStartY = Gdx.graphics.getHeight() / 2 - camera.getY() * Settings.SCALED_TILE_SIZE;

        /* render tile terrains */
        for (int x = 0; x < world.getMap().getWidth(); x++) {
            for (int y = 0; y < world.getMap().getHeight(); y++) {
                TextureRegion render;
                if (world.getMap().getTile(x, y).getTerrain() == TERRAIN.SAND_1) {
                    render = sand1;
                } else if (world.getMap().getTile(x, y).getTerrain() == TERRAIN.SAND_2) {
                    render = sand2;
                }else if (world.getMap().getTile(x, y).getTerrain() == TERRAIN.FLOOR) {
                    render = floor;
                } else {
                    render = null;
                }

                if (render != null) {
                    batch.draw(render,
                        (placeStartX + x * Settings.SCALED_TILE_SIZE),
                        (placeStartY + y * Settings.SCALED_TILE_SIZE),
                        (Settings.SCALED_TILE_SIZE),
                        (Settings.SCALED_TILE_SIZE));
                }
            }
        }

        for (WorldObject object : world.getWorldObjects()) {
            object.update(Gdx.graphics.getDeltaTime());
        }

        /* collect objects and actors */
        for (int x = 0; x < world.getMap().getWidth(); x++) {
            for (int y = 0; y < world.getMap().getHeight(); y++) {
                if (world.getMap().getTile(x, y).getActor() != null) {
                    Actor actor = world.getMap().getTile(x, y).getActor();
                    forRendering.add(actor);
                }
                if (world.getMap().getTile(x, y).getObject() != null) {
                    WorldObject object = world.getMap().getTile(x, y).getObject();
                    if (renderedObjects.contains(object.hashCode())) { // test if it's already drawn
                        continue;
                    }
                    if (object.isWalkable()) {        // if it's walkable, draw it right away
                        batch.draw(object.getSprite(),    // chances are it's probably something on the ground
                            placeStartX + object.getWorldX() * Settings.SCALED_TILE_SIZE,
                            placeStartY + object.getWorldY() * Settings.SCALED_TILE_SIZE,
                            Settings.SCALED_TILE_SIZE * object.getSizeX(),
                            Settings.SCALED_TILE_SIZE * object.getSizeY());
                        continue;
                    } else {    // add it to the list of YSortables
                        forRendering.add(object);
                        renderedObjects.add(object.hashCode());
                    }
                }
            }
        }

        Collections.sort(forRendering, new WorldObjectYComparator());
        Collections.reverse(forRendering);

        for (YSortable loc : forRendering) {
            TextureRegion sprite = loc.getSprite();
            if (sprite != null) {
                batch.draw(sprite,
                    placeStartX + loc.getWorldX() * Settings.SCALED_TILE_SIZE,
                    placeStartY + loc.getWorldY() * Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE * loc.getSizeX(),
                    Settings.SCALED_TILE_SIZE * loc.getSizeY());
            } else {
                System.err.println("Errore: sprite per l'oggetto " + loc + " è null.");
            }
        }

        renderedObjects.clear();
        forRendering.clear();
    }

    public void setWorld(World world) {
        this.world = world;
        renderedObjects.clear();
        forRendering.clear();
    }

    public World getWorld() {
        return world;
    }
}
