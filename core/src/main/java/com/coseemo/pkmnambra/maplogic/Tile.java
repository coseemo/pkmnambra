package com.coseemo.pkmnambra.maplogic;

import com.coseemo.pkmnambra.characters.Actor;

public class Tile {
    private TERRAIN terrain;
    private Actor actor;
    private PlaceObject object;
    public Tile(TERRAIN terrain) {
        this.terrain = terrain;
    }
    public TERRAIN getTerrain() {
        return terrain;
    }
    public Actor getActor() {
        return actor;
    }
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public PlaceObject getObject() {
        return object;
    }

    public void setObject(PlaceObject object) {
        this.object = object;
    }
}
