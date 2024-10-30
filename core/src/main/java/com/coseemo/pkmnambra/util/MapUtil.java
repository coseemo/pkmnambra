package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.maplogic.PlaceObject;

public class MapUtil {
    public void addHouse(Place place, int x, int y, AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        TextureRegion houseRegion = atlas.findRegion("house");
        GridPoint2[] gridArray = new GridPoint2[12];
        int index = 0;
        for (int loopX = 0; loopX < 4; loopX++) {
            for (int loopY = 0; loopY < 3; loopY++) {

                gridArray[index] = new GridPoint2(loopX, loopY);
                index++;
            }
        }
        PlaceObject house = new PlaceObject(x, y, false, houseRegion, 4f, 4f, gridArray);
        place.addObject(house);
    }
}
