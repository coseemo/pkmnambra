package com.coseemo.pkmnambra.mapobjectfactory;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.mapobjectfactory.WorldObject;

public class AnimatedPlaceObject extends WorldObject {
    private final Animation<TextureRegion> animation;
    private float stateTime;
    private boolean isAnimating;

    public AnimatedPlaceObject(int x, int y, Animation<TextureRegion> animation,
                               float sizeX, float sizeY, GridPoint2[] tiles, boolean walkable) {
        super(x, y, walkable, animation.getKeyFrame(0), sizeX, sizeY, tiles);
        this.animation = animation;
        this.stateTime = 0;
        this.isAnimating = true;
    }

    @Override
    public void update(float delta) {
        if (isAnimating) {
            stateTime += delta;
        }
    }

    @Override
    public TextureRegion getSprite() {
        return isAnimating ? animation.getKeyFrame(stateTime, true) : super.getSprite();
    }

    public void setAnimating(boolean animating) {
        this.isAnimating = animating;
    }

    public void resetAnimation() {
        this.stateTime = 0;
    }
}
