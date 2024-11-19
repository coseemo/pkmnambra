package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;


public class MapObjectFactory {
    private final AssetManager assetManager;

    public MapObjectFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public PlaceObject createStaticObject(String objectType, int x, int y) {
        TextureAtlas atlas = assetManager.get("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);

        switch (objectType) {
            case "house":
                return new PlaceObject(x, y, false,
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
                return new PlaceObject(x, y, false,
                    atlas.findRegion("tree"),
                    1, 2,
                    new GridPoint2[]{
                        new GridPoint2(0, 0)
                    });
            default:
                throw new IllegalArgumentException("Unknown object type: " + objectType);
        }
    }

    public AnimatedPlaceObject createAnimatedObject(String objectType, int x, int y) {
        TextureAtlas atlas = assetManager.get("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);

        switch (objectType) {
            case "water":
                Animation<TextureRegion> waterAnim = new Animation<>(
                    0.2f,
                    atlas.findRegions("water_anim"),
                    Animation.PlayMode.LOOP
                );
                return new AnimatedPlaceObject(x, y, waterAnim,
                    1, 1,
                    new GridPoint2[]{new GridPoint2(0, 0)},
                    false);
            case "flower":
                Animation<TextureRegion> flowerAnim = new Animation<>(
                    0.5f,
                    atlas.findRegions("flowers"),
                    Animation.PlayMode.LOOP
                );
                return new AnimatedPlaceObject(x, y, flowerAnim,
                    0.65f, 0.65f,
                    new GridPoint2[]{new GridPoint2(0, 0)},
                    true);
            default:
                throw new IllegalArgumentException("Unknown animated object type: " + objectType);
        }
    }
}
