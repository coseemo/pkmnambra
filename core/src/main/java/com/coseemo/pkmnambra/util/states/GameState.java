package com.coseemo.pkmnambra.util.states;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.screen.TransitionScreen;
import com.coseemo.pkmnambra.util.EventNotifier;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private static GameState instance;
    private Player player;
    private Place currentPlace;
    private final Map<String, Place> places;
    private Main game;
    private final EventNotifier eventNotifier;
    private AssetManager assetManager;
    private Screen currentScreen;

    private GameState() {
        eventNotifier = new EventNotifier();
        places = new HashMap<>();
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void initializeGameState(Player player, Place startingPlace, Main game) {
        this.player = player;
        this.currentPlace = startingPlace;
        this.game = game;
        if (startingPlace != null) {
            places.put("starting_map", startingPlace);
        }
    }

    public Main getGame() {
        return game;
    }

    public Place getPlace(String mapName) {
        return places.get(mapName);
    }

    public void addPlace(String mapName, Place place) {
        places.put(mapName, place);
    }

    public Player getPlayer() {
        return player;
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public EventNotifier getEventNotifier() {
        return eventNotifier;
    }

    public void setCurrentPlace(Place place) {
        this.currentPlace = place;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void changeScreen(Screen arriveScreen){
        game.setScreen(new TransitionScreen(game, currentScreen, arriveScreen));
    }
}
