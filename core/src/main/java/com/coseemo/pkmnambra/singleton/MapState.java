package com.coseemo.pkmnambra.singleton;

import com.coseemo.pkmnambra.actorobserver.World;

import java.util.HashMap;
import java.util.Map;

public class MapState {
    private World currentPlace;
    private final Map<String, World> places;

    public MapState() {
        this.places = new HashMap<>();
    }

    public World getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(World currentPlace) {
        this.currentPlace = currentPlace;
    }

    public void addPlace(String mapName, World place) {
        places.put(mapName, place);
    }

    public World getPlace(String mapName) {
        return places.get(mapName);
    }
}
