package com.coseemo.pkmnambra.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.coseemo.pkmnambra.Settings;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.camera.Camera;
import com.coseemo.pkmnambra.actorobserver.Actor;
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
    private final List<Integer> renderedObjects = new ArrayList<>();
    private final List<YSortable> forRendering = new ArrayList<>();

    public PlaceRenderer(GameState gameState) {
        this.assetManager = gameState.getResourceManager().getAssetManager();
        this.world = gameState.getMapState().getCurrentPlace();

        // Carico gli asset necessari
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        TextureAtlas atlas = assetManager.get("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        TextureAtlas atlas1 = assetManager.get("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);

        // Ottengo le texture dalle regioni dell'atlas
        sand1 = atlas.findRegion("sand1");
        sand2 = atlas.findRegion("sand2");
        floor = atlas1.findRegion("interno");
    }

    public void render(SpriteBatch batch, Camera camera) {
        float placeStartX = Gdx.graphics.getWidth() / 2 - camera.getX() * Settings.SCALED_TILE_SIZE;
        float placeStartY = Gdx.graphics.getHeight() / 2 - camera.getY() * Settings.SCALED_TILE_SIZE;

        // Renderizzo i terreni delle tile
        for (int x = 0; x < world.getMap().getWidth(); x++) {
            for (int y = 0; y < world.getMap().getHeight(); y++) {
                TextureRegion render = getTerrainTexture(world.getMap().getTile(x, y).getTerrain());
                if (render != null) {
                    batch.draw(render,
                        placeStartX + x * Settings.SCALED_TILE_SIZE,
                        placeStartY + y * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE);
                }
            }
        }

        // Aggiorno gli oggetti del mondo
        for (WorldObject object : world.getWorldObjects()) {
            object.update(Gdx.graphics.getDeltaTime());
        }

        // Raccolgo oggetti e attori per il rendering
        collectRenderableObjects(batch, placeStartX, placeStartY);

        // Ordino gli oggetti da rendere in base alla coordinata Y
        Collections.sort(forRendering, new WorldObjectYComparator());
        Collections.reverse(forRendering);

        // Renderizzo gli oggetti ordinati
        for (YSortable loc : forRendering) {
            TextureRegion sprite = loc.getSprite();
            if (sprite != null) {
                batch.draw(sprite,
                    placeStartX + loc.getWorldX() * Settings.SCALED_TILE_SIZE,
                    placeStartY + loc.getWorldY() * Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE * loc.getSizeX(),
                    Settings.SCALED_TILE_SIZE * loc.getSizeY());
            }
        }

        // Pulisco le liste temporanee per il prossimo ciclo di rendering
        renderedObjects.clear();
        forRendering.clear();
    }

    private TextureRegion getTerrainTexture(TERRAIN terrain) {
        switch (terrain) {
            case SAND_1:
                return sand1;
            case SAND_2:
                return sand2;
            case FLOOR:
                return floor;
            default:
                return null;
        }
    }

    private void collectRenderableObjects(SpriteBatch batch, float placeStartX, float placeStartY) {
        for (int x = 0; x < world.getMap().getWidth(); x++) {
            for (int y = 0; y < world.getMap().getHeight(); y++) {
                // Aggiungo gli attori alla lista di rendering
                if (world.getMap().getTile(x, y).getActor() != null) {
                    Actor actor = world.getMap().getTile(x, y).getActor();
                    forRendering.add(actor);
                }

                // Gestisco gli oggetti del mondo
                if (world.getMap().getTile(x, y).getObject() != null) {
                    WorldObject object = world.getMap().getTile(x, y).getObject();
                    if (renderedObjects.contains(object.hashCode())) {
                        continue; // Evito di renderizzare più volte lo stesso oggetto
                    }
                    if (object.isWalkable()) {
                        // Renderizzo direttamente gli oggetti camminabili
                        batch.draw(object.getSprite(),
                            placeStartX + object.getWorldX() * Settings.SCALED_TILE_SIZE,
                            placeStartY + object.getWorldY() * Settings.SCALED_TILE_SIZE,
                            Settings.SCALED_TILE_SIZE * object.getSizeX(),
                            Settings.SCALED_TILE_SIZE * object.getSizeY());
                    } else {
                        // Aggiungo gli oggetti non camminabili alla lista per lo sorting
                        forRendering.add(object);
                        renderedObjects.add(object.hashCode());
                    }
                }
            }
        }
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
