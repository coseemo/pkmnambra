package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class PlaceObject implements YSortable {
    private int x, y;
    private TextureRegion texture;
    private float sizeX, sizeY;
    private List<GridPoint2> tiles;
    private boolean walkable;

    public PlaceObject(int x, int y, boolean walkable, TextureRegion texture, float sizeX, float sizeY, GridPoint2[] tiles) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = new ArrayList<GridPoint2>();
        for (GridPoint2 p : tiles) {
            this.tiles.add(p);
            this.walkable = walkable;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update(float delta) {
    }

    @Override
    public float getPlaceX() {
        return x;
    }

    @Override
    public float getPlaceY() {
        return y;
    }

    @Override
    public TextureRegion getSprite() {
        return texture;
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public List<GridPoint2> getTiles() {
        return tiles;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
}
