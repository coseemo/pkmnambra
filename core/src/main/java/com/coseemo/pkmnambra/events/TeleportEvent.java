package com.coseemo.pkmnambra.events;

import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.util.states.GameState;
import com.coseemo.pkmnambra.util.MapLoader;

public class TeleportEvent extends MapEvent {
    private final String targetMap;
    private final int targetX;
    private final int targetY;


    public TeleportEvent(int x, int y, String targetMap, int targetX, int targetY) {
        super(x, y, EVENT_TYPE.TELEPORT);
        this.targetMap = targetMap;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void trigger(GameState gameState) {
        Player player = gameState.getPlayer();

        gameState.changeScreen(gameState.getCurrentScreen());
        // Carica la nuova mappa
        Place newPlace = MapLoader.loadMapAndObjects(targetMap, gameState.getAssetManager());

        player.changePlace(newPlace, targetX, targetY);

        // Aggiorna la mappa corrente nel GameState
        gameState.setCurrentPlace(newPlace);

        System.out.println("Teletrasporto completato a: " + targetX + ", " + targetY + " sulla mappa: " + targetMap);
    }

}

