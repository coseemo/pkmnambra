package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.characters.ActorBehavior;
import com.coseemo.pkmnambra.characters.ActorObserver;
import com.coseemo.pkmnambra.characters.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class World implements ActorObserver {
    private TileMap map;
    private final List<Actor> actors;
    private HashMap<Integer, ActorBehavior> brains;
    private final List<WorldObject> objects;


    public World(TileMap tileMap, AssetManager assetManager) {
        this.map = tileMap;
        this.actors = new CopyOnWriteArrayList<>();
        this.objects = new CopyOnWriteArrayList<>();
        this.brains = new HashMap<Integer, ActorBehavior>();
    }

    public World() {
        this.actors = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public void addActor(Actor a) {
        map.getTile(a.getX(), a.getY()).setActor(a);
        actors.add(a);
    }

    public void addActor(Actor a, ActorBehavior b) {
        int key = a.hashCode();
        addActor(a);
        brains.put(key, b);
        System.out.println(brains);
    }

    public void addObject(WorldObject o) {
        for (GridPoint2 p : o.getTiles()) {
            //System.out.println("\t Adding tile: "+p.x+", "+p.y);
            map.getTile(o.getX()+p.x, o.getY()+p.y).setObject(o);
        }
        objects.add(o);
    }

    public void removeActor(Actor a) {
        int key = a.hashCode();
        map.getTile(a.getX(), a.getY()).setActor(null);
        actors.remove(a);

        brains.remove(key);
    }

    public void update(float delta) {

        for (Actor a : actors) {
            int key = a.hashCode();
            if (brains.containsKey(key)) {
                brains.get(key).update(delta);
            }
            a.update(delta);
        }
        for (WorldObject o : objects) {
            o.update(delta);
        }
    }

    public TileMap getMap() {
        return map;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<WorldObject> getWorldObjects() {
        return objects;
    }

    @Override
    public void actorMoved(Actor a, DIRECTION direction, int x, int y) {

    }

    @Override
    public void attemptedMove(Actor a, DIRECTION direction) {

    }

    @Override
    public void actorBeforeMoved(Actor a, DIRECTION direction) {

    }

    public ActorBehavior getActorBehavior(Actor a){
        int key = a.hashCode();
        System.out.println(key);
        return brains.get(key);
    }

}
