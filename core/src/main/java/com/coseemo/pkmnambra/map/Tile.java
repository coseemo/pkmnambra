package com.coseemo.pkmnambra.map;

import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.maplogic.TERRAIN;
import com.coseemo.pkmnambra.mapobjectfactory.WorldObject;

public class Tile {
    private TERRAIN terrain;
    private Actor actor;
    private WorldObject object;

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

    public void setTerrain(TERRAIN terrain) {
        this.terrain = terrain;
    }

    public WorldObject getObject() {
        return object;
    }

    public void setObject(WorldObject object) {
        this.object = object;
    }

    public void actorStep(Actor a) {
        // Gestisce gli effetti quando un attore si muove su questo tile
        switch (terrain) {
            case SAND_2:

                break;
            default:
                break;
        }

        // Se c'è un oggetto, notifica anche lui
        if (object != null) {
            object.onActorStep(a);
        }
    }

    public boolean actorBeforeStep(Actor a) {
        return true;
    }
}
