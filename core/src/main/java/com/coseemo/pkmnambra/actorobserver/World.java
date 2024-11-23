package com.coseemo.pkmnambra.actorobserver;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.actors.*;
import com.coseemo.pkmnambra.map.TileMap;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.mapobjectfactory.WorldObject;

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

    // Aggiungo un giocatore al mondo
    public void addPlayer(Player a) {
        map.getTile(a.getX(), a.getY()).setActor(a);
        player = a;
    }

    // Aggiungo un NPC al mondo
    public void addNPC(ActorBehavior b) {
        int key = b.getActor().hashCode();
        map.getTile(b.getActor().getX(), b.getActor().getY()).setActor(b.getActor());
        npcs.add(b);
    }

    // Aggiungo un oggetto al mondo
    public void addObject(WorldObject o) {
        for (GridPoint2 p : o.getTiles()) {
            map.getTile(o.getX() + p.x, o.getY() + p.y).setObject(o);
        }
        objects.add(o);
    }

    // Rimuovo un attore dal mondo
    public void removeActor(Actor a) {
        map.getTile(a.getX(), a.getY()).setActor(null);

        if (a instanceof Player) {
            player = null;
        } else {
            npcs.remove(getActorBehavior(a));
        }
    }

    // Aggiorno lo stato del mondo
    public void update(float delta) {
        player.update(delta);

        for (ActorBehavior b : npcs) {
            b.update(delta);
        }
        for (WorldObject o : objects) {
            o.update(delta);
        }
    }

    // Ottengo il comportamento di un attore
    public ActorBehavior getActorBehavior(Actor a) {
        int i = 0;
        while (!Objects.equals(npcs.get(i).getActor().getName(), a.getName())) {
            i++;
        }
        return npcs.get(i);
    }

    // Restituisco la mappa del mondo
    public TileMap getMap() {
        return map;
    }

    // Restituisco gli oggetti del mondo
    public List<WorldObject> getWorldObjects() {
        return objects;
    }

    // Metodi dell'interfaccia ActorObserver
    @Override
    public void actorMoved(Actor a, DIRECTION direction, int x, int y) {
        // Non implementato
    }

    @Override
    public void attemptedMove(Actor a, DIRECTION direction) {
        // Non implementato
    }

    @Override
    public void actorBeforeMoved(Actor a, DIRECTION direction) {
        // Non implementato
    }

    // Ottengo gli NPCs nel mondo
    public ActorBehavior[] getNPCs() {
        ActorBehavior[] npcss = new ActorBehavior[npcs.size()];
        int i = 0;
        for (ActorBehavior b : npcs) {
            npcss[i] = b;
        }
        return npcss;
    }

    // Restituisco il nome del mondo
    public String getName() {
        return name;
    }

    // Ottengo il giocatore
    public Player getPlayer() {
        return player;
    }
}
