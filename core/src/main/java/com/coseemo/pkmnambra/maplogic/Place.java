package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.characters.Actor;
import com.coseemo.pkmnambra.events.MapEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Place{
    private TileMap map;
    private List<Actor> actors;
    private List<PlaceObject> objects;
    private MapObjectFactory objectFactory;
    private List<MapEvent> events;

    public Place(TileMap tileMap, AssetManager assetManager) {
        this.map = tileMap;
        this.actors = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.objectFactory = new MapObjectFactory(assetManager);
        this.events = new ArrayList<>();
    }

    public void addEvent(MapEvent event) {
        events.add(event);
    }

    public List<MapEvent> getEvents() {
        return events;
    }

    public List<MapEvent> getEventsAt(int x, int y) {
        return events.stream()
            .filter(e -> e.getX() == x && e.getY() == y)
            .collect(Collectors.toList());
    }

    public void removeActor(Actor actor) {
        map.getTile(actor.getX(), actor.getY()).setActor(null);
        actors.remove(actor);
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

    public void addAnimatedObject(String objectType, int x, int y) {
        AnimatedPlaceObject object = objectFactory.createAnimatedObject(objectType, x, y);
        addObject(object);
    }

    public void addStaticObject(String objectType, int x, int y) {
        PlaceObject object = objectFactory.createStaticObject(objectType, x, y);
        addObject(object);
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
