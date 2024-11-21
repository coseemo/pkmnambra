package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.characters.Actor;

import java.util.ArrayList;
import java.util.List;

public class WorldObject implements YSortable {
    private final int x;
    private final int y;
    private final TextureRegion texture;
    private final float sizeX;
    private final float sizeY;
    private final List<GridPoint2> tiles;
    private boolean walkable = false;
    private boolean interactive = false;
    private ObjectState state = ObjectState.IDLE;

    public enum ObjectState {
        IDLE,
        INTERACTING,
        ANIMATING
    }


    public WorldObject(int x, int y, boolean walkable, TextureRegion texture, float sizeX, float sizeY, GridPoint2[] tiles) {
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

    public boolean interact(Actor actor) {
        if (!interactive || state == ObjectState.ANIMATING) {
            return false;
        }

        state = ObjectState.INTERACTING;
        // Logica base di interazione
        // Le sottoclassi dovrebbero sovrascrivere questo metodo per implementare
        // comportamenti specifici
        return true;
    }

    public void onActorStep(Actor actor) {
        // Logica base per quando un attore passa sull'oggetto
        // Le sottoclassi possono implementare comportamenti specifici
        // come trappole, teletrasporti, etc.
    }

    public void update(float delta) {
        // Aggiorna lo stato dell'oggetto
        // Utile per animazioni o comportamenti temporizzati
        switch (state) {
            case ANIMATING:
                updateAnimation(delta);
                break;
            case INTERACTING:
                updateInteraction(delta);
                break;
            case IDLE:
                updateIdle(delta);
                break;
        }
    }

    protected void updateAnimation(float delta) {
        // Implementato dalle sottoclassi per gestire le animazioni
    }

    protected void updateInteraction(float delta) {
        // Implementato dalle sottoclassi per gestire le interazioni
    }

    protected void updateIdle(float delta) {
        // Implementato dalle sottoclassi per gestire lo stato idle
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public float getWorldX() {
        return x;
    }

    @Override
    public float getWorldY() {
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
