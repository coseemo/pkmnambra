package com.coseemo.pkmnambra.util;

import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.Place;

public class GameState {
    private static GameState instance;
    private Player player;
    private Place currentPlace;
    private final EventNotifier eventNotifier;

    private GameState() {
        eventNotifier = new EventNotifier();
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void initializeGameState(Player player, Place place) {
        this.player = player;
        this.currentPlace = place;
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

    // Metodo per resettare lo stato (utile per testing o nuovo gioco)
    public static void reset() {
        instance = null;
    }
}
