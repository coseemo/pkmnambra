package com.coseemo.pkmnambra.screen.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.coseemo.pkmnambra.Settings;
import com.coseemo.pkmnambra.camera.Camera;
import com.coseemo.pkmnambra.characters.logic.Actor;
import com.coseemo.pkmnambra.maplogic.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceRenderer {

    private final AssetManager assetManager;
    private Place place;
    private final TextureRegion sand1;
    private final TextureRegion sand2;
    private final List<Integer> renderedObjects = new ArrayList<Integer>();
    private final List<YSortable> forRendering = new ArrayList<YSortable>();

    public PlaceRenderer(AssetManager assetManager, Place place) {
        this.assetManager = assetManager;
        this.place = place;

        TextureAtlas atlas = assetManager.get("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        sand1 = atlas.findRegion("sand1");
        sand2 = atlas.findRegion("sand2");
    }

    public void render(SpriteBatch batch, Camera camera) {
        float placeStartX = Gdx.graphics.getWidth() / 2 - camera.getX() * Settings.SCALED_TILE_SIZE;
        float placeStartY = Gdx.graphics.getHeight() / 2 - camera.getY() * Settings.SCALED_TILE_SIZE;

        /* render tile terrains */
        for (int x = 0; x < place.getMap().getWidth(); x++) {
            for (int y = 0; y < place.getMap().getHeight(); y++) {
                TextureRegion render;
                if (place.getMap().getTile(x, y).getTerrain() == TERRAIN.SAND_1) {
                    render = sand1;
                } else if (place.getMap().getTile(x, y).getTerrain() == TERRAIN.SAND_2) {
                    render = sand2;
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

        for (PlaceObject object : place.getObjects()) {
            object.update(Gdx.graphics.getDeltaTime());
        }

        /* collect objects and actors */
        for (int x = 0; x < place.getMap().getWidth(); x++) {
            for (int y = 0; y < place.getMap().getHeight(); y++) {
                if (place.getMap().getTile(x, y).getActor() != null) {
                    Actor actor = place.getMap().getTile(x, y).getActor();
                    forRendering.add(actor);
                }
                if (place.getMap().getTile(x, y).getObject() != null) {
                    PlaceObject object = place.getMap().getTile(x, y).getObject();
                    if (renderedObjects.contains(object.hashCode())) { // test if it's already drawn
                        continue;
                    }
                    if (object.isWalkable()) {        // if it's walkable, draw it right away
                        batch.draw(object.getSprite(),    // chances are it's probably something on the ground
                            placeStartX + object.getPlaceX() * Settings.SCALED_TILE_SIZE,
                            placeStartY + object.getPlaceY() * Settings.SCALED_TILE_SIZE,
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

        Collections.sort(forRendering, new PlaceObjectYComparator());
        Collections.reverse(forRendering);

        for (YSortable loc : forRendering) {
            TextureRegion sprite = loc.getSprite();
            if (sprite != null) {
                batch.draw(sprite,
                    placeStartX + loc.getPlaceX() * Settings.SCALED_TILE_SIZE,
                    placeStartY + loc.getPlaceY() * Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE * loc.getSizeX(),
                    Settings.SCALED_TILE_SIZE * loc.getSizeY());
            } else {
                System.err.println("Errore: sprite per l'oggetto " + loc + " è null.");
            }
        }

        renderedObjects.clear();
        forRendering.clear();
    }

    public void setPlace(Place place) {
        this.place = place;
        renderedObjects.clear();
        forRendering.clear();
    }

    public Place getPlace() {
        return place;
    }
}
