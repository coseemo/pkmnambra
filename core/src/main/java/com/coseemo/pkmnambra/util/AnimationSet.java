package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.coseemo.pkmnambra.maplogic.DIRECTION;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {
    private Map<DIRECTION, Animation<TextureRegion>> walking;  // Specifica il tipo generico TextureRegion
    private Map<DIRECTION, TextureRegion> standing;

    public AnimationSet(Animation<TextureRegion> walking_east,
                        Animation<TextureRegion> walking_west,
                        Animation<TextureRegion> walking_north,
                        Animation<TextureRegion> walking_south,
                        TextureRegion standing_east,
                        TextureRegion standing_west,
                        TextureRegion standing_north,
                        TextureRegion standing_south) {

        walking = new HashMap<>();
        walking.put(DIRECTION.EAST, walking_east);
        walking.put(DIRECTION.WEST, walking_west);
        walking.put(DIRECTION.NORTH, walking_north);
        walking.put(DIRECTION.SOUTH, walking_south);

        standing = new HashMap<>();
        standing.put(DIRECTION.EAST, standing_east);
        standing.put(DIRECTION.WEST, standing_west);
        standing.put(DIRECTION.NORTH, standing_north);
        standing.put(DIRECTION.SOUTH, standing_south);
    }

    public Animation<TextureRegion> getWalking(DIRECTION dir) {
        return walking.get(dir);  // Restituisce Animation con TextureRegion
    }

    public TextureRegion getStanding(DIRECTION dir) {
        return standing.get(dir);  // Restituisce il frame statico
    }
}
