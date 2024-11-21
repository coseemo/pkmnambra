package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.characters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class World implements ActorObserver {

    private String name;
    private TileMap map;
    private Player player;
    private List<ActorBehavior> npcs;
    private final List<WorldObject> objects;


    public World(String name, TileMap tileMap, AssetManager assetManager) {
        this.name = name;
        this.map = tileMap;
        this.objects = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public void addPlayer(Player a) {
        map.getTile(a.getX(), a.getY()).setActor(a);
        player = a;
    }

    public void addNPC(ActorBehavior b) {
        int key = b.getActor().hashCode();
        map.getTile(b.getActor().getX(), b.getActor().getY()).setActor(b.getActor());
        npcs.add(b);
        System.out.println(npcs);
    }

    public void addObject(WorldObject o) {
        for (GridPoint2 p : o.getTiles()) {
            //System.out.println("\t Adding tile: "+p.x+", "+p.y);
            map.getTile(o.getX()+p.x, o.getY()+p.y).setObject(o);
        }
        objects.add(o);
    }

    public void removeActor(Actor a) {
        map.getTile(a.getX(), a.getY()).setActor(null);

        if(a instanceof Player){
            player = null;
        }else{
            npcs.remove(getActorBehavior(a));
        }

    }

    public void update(float delta) {

        player.update(delta);

        for (ActorBehavior b : npcs) {
            b.update(delta);
        }
        for (WorldObject o : objects) {
            o.update(delta);
        }
    }

    public ActorBehavior getActorBehavior(Actor a){
        int i = 0;
        while(!Objects.equals(npcs.get(i).getActor().getName(), a.getName())){
            i++;
        }
        return npcs.get(i);
    }

    public TileMap getMap() {
        return map;
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

    public ActorBehavior[] getNPCs() {
        ActorBehavior[] npcss = new ActorBehavior[npcs.size()];
        int i = 0;
        for(ActorBehavior b : npcs){
            npcss[i] = b;
        }
        return npcss;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer(){
        return player;
    }
}
