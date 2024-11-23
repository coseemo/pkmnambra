package com.coseemo.pkmnambra.mapobjectfactory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.maplogic.EncounterType;

public class MapObjectFactory {

    // Static method to create objects
    public static WorldObject createObject(String objectType, int x, int y, AssetManager assetManager, String... params) {

        switch (objectType.toLowerCase()) {
            case "lab":
            case "palml":
            case "palmr":
            case "backcenter":
            case "backleft":
            case "backright":
            case "middlecenter":
            case "middleleft":
            case "rightcenter":
            case "righttop":
            case "topcenter":
            case "topleft":
            case "wall":
            case "rug":
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
        TextureAtlas atlas = assetManager.get("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        switch (objectType.toLowerCase()) {
            case "lab":
                return new WorldObject(x, y, false,
                    atlas.findRegion("lab"),
                    8, 8,
                    new GridPoint2[]{
                        new GridPoint2(0, 1),
                        new GridPoint2(0, 2),
                        new GridPoint2(0, 3),
                        new GridPoint2(1, 1),
                        new GridPoint2(1, 3),
                        new GridPoint2(2, 2),
                        new GridPoint2(2, 3),
                        new GridPoint2(3, 1),
                        new GridPoint2(3, 2),
                        new GridPoint2(3, 3),
                    });
            case "palml":
                return new WorldObject(x, y, false,
                    atlas.findRegion("palml"),
                    2, 3,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "palmr":
                return new WorldObject(x, y, false,
                    atlas.findRegion("palmr"),
                    2, 3,
                    new GridPoint2[]{new GridPoint2(1, 0)});
            case "backcenter":
                return new WorldObject(x, y, false,
                    atlas.findRegion("backcenter"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "backleft":
                return new WorldObject(x, y, false,
                    atlas.findRegion("backleft"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "backright":
                return new WorldObject(x, y, false,
                    atlas.findRegion("backright"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "middlecenter":
                return new WorldObject(x, y, false,
                    atlas.findRegion("middlecenter"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "middleleft":
                return new WorldObject(x, y, false,
                    atlas.findRegion("middleleft"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "rightcenter":
                return new WorldObject(x, y, false,
                    atlas.findRegion("rightcenter"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "righttop":
                return new WorldObject(x, y, false,
                    atlas.findRegion("righttop"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "topcenter":
                return new WorldObject(x, y, false,
                    atlas.findRegion("topcenter"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "topleft":
                return new WorldObject(x, y, false,
                    atlas.findRegion("topleft"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "wall":
                return new WorldObject(x, y, false,
                    atlas.findRegion("wall"),
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)});
            case "rug":
                return new WorldObject(x, y, true,
                    atlas.findRegion("rug"),
                    3, 2,
                    new GridPoint2[]{new GridPoint2(0, 0),new GridPoint2(2, 0)});
            default:
                throw new IllegalArgumentException("Unknown static object type: " + objectType);
        }
    }


    // Refactor to use static asset manager
    protected static AnimatedPlaceObject createAnimatedObject(String objectType, int x, int y, AssetManager assetManager) {
        TextureAtlas atlas1 = assetManager.get("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        TextureAtlas atlas = assetManager.get("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        switch (objectType.toLowerCase()) {
            case "water":
                Animation<TextureRegion> waterAnim = new Animation<>(0.2f,
                    atlas.findRegions("water"),
                    Animation.PlayMode.LOOP);
                return new AnimatedPlaceObject(x, y, waterAnim,
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)},
                    false);
            case "flower":
                Animation<TextureRegion> flowerAnim = new Animation<>(0.5f,
                    atlas1.findRegions("flowers"),
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
        TextureAtlas atlas = assetManager.get("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        TextureRegion texture = atlas.findRegion("teleport");
        assetManager.finishLoading();

        return new TeleportObject(x, y, targetWorld, targetX, targetY, texture,
            new GridPoint2[]{new GridPoint2(0, 0)});
    }

    // Static method for creating encounter zone object
    private static EncounterZoneObject createEncounterZone(int x, int y, EncounterType encounterType, AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        TextureRegion texture = atlas.findRegion("grass");
        assetManager.finishLoading();

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
