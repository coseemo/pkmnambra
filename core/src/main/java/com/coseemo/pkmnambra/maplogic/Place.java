package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.characters.Actor;

import java.util.ArrayList;
import java.util.List;

public class Place{
    private TileMap map;
    private List<Actor> actors;
    private List<PlaceObject> objects;

    public Place(int Width, int Height) {
        this.map = new TileMap(Width, Height);
        this.actors = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public void addActor(Actor a){
        map.getTile(a.getX(), a.getY()).setActor(a);
        actors.add(a);
    }

    public void addObject(PlaceObject o){
        for(GridPoint2 p : o.getTiles())
            map.getTile(o.getX()+p.x, o.getY()+p.y).setObject(o);
        objects.add(o);
    }

    public void update(float delta){
        for(Actor a : actors)
            a.update(delta);
        for(PlaceObject o : objects)
            o.update(delta);
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<PlaceObject> getObjects() {
        return objects;
    }

    public TileMap getMap() {
        return map;
    }
}
