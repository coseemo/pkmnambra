package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

public class MapObjectFactory {

    // Static method to create objects
    public static WorldObject createObject(String objectType, int x, int y, AssetManager assetManager,String... params) {

        switch (objectType.toLowerCase()) {
            case "house":
            case "tree":
                return createStaticObject(objectType, x, y, assetManager);

            case "water":
            case "flower":
                return createAnimatedObject(objectType, x, y, assetManager);

            case "teleport":
                if (params.length >= 3) {
                    return createTeleportObject(x, y, params[0],
                        Integer.parseInt(params[1]),
                        Integer.parseInt(params[2]), assetManager);
                }
                throw new IllegalArgumentException("Insufficient parameters for teleport object");

            case "encounter":
                if (params.length >= 1) {
                    return createEncounterZone(x, y, getEncounterType(params[0], assetManager), assetManager);
                }
                throw new IllegalArgumentException("Encounter type not specified");

            default:
                throw new IllegalArgumentException("Unknown object type: " + objectType);
        }
    }

    protected static WorldObject createStaticObject(String objectType, int x, int y, AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);

        switch (objectType.toLowerCase()) {
            case "house":
                return new WorldObject(x, y, false,
                    atlas.findRegion("house"),
                    4, 4,
                    new GridPoint2[]{
                        new GridPoint2(0, 0),
                        new GridPoint2(0, 1),
                        new GridPoint2(0, 2),
                        new GridPoint2(1, 2),
                        new GridPoint2(2, 1),
                        new GridPoint2(2, 2),
                        new GridPoint2(3, 0),
                        new GridPoint2(3, 1),
                        new GridPoint2(3, 2),
                    });
            case "tree":
                return new WorldObject(x, y, false,
                    atlas.findRegion("tree"),
                    1, 2,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            default:
                throw new IllegalArgumentException("Unknown static object type: " + objectType);
        }
    }

    // Refactor to use static asset manager
    protected static AnimatedPlaceObject createAnimatedObject(String objectType, int x, int y, AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);

        switch (objectType.toLowerCase()) {
            case "water":
                Animation<TextureRegion> waterAnim = new Animation<>(0.2f,
                    atlas.findRegions("water_anim"),
                    Animation.PlayMode.LOOP);
                return new AnimatedPlaceObject(x, y, waterAnim,
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)},
                    false);
            case "flower":
                Animation<TextureRegion> flowerAnim = new Animation<>(0.5f,
                    atlas.findRegions("flowers"),
                    Animation.PlayMode.LOOP);
                return new AnimatedPlaceObject(x, y, flowerAnim,
                    0.65f, 0.65f,
                    new GridPoint2[]{new GridPoint2(0, 0)},
                    true);
            default:
                throw new IllegalArgumentException("Unknown animated object type: " + objectType);
        }
    }

    // Static method for creating teleport object
    private static TeleportObject createTeleportObject(int x, int y, String targetWorld, int targetX, int targetY, AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        TextureRegion texture = atlas.findRegion("grass1");

        return new TeleportObject(x, y, targetWorld, targetX, targetY, texture,
            new GridPoint2[]{new GridPoint2(0, 0)});
    }

    // Static method for creating encounter zone object
    private static EncounterZoneObject createEncounterZone(int x, int y, EncounterType encounterType, AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        TextureRegion texture = atlas.findRegion("grass");

        return new EncounterZoneObject(x, y, encounterType, texture,
            new GridPoint2[]{new GridPoint2(0, 0)});
    }

    // Refactor this method to use a static asset manager reference
    private static EncounterType getEncounterType(String type, AssetManager assetManager) {
        switch (type.toUpperCase()) {
            case "BEACH":
                return EncounterType.EncounterTypes.BEACH;
            default:
                throw new IllegalArgumentException("Unknown encounter type: " + type);
        }
    }
}
