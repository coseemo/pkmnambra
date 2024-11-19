package com.coseemo.pkmnambra.events;

import com.badlogic.gdx.math.Vector2;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.util.states.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<Vector2, List<MapEvent>> eventMap;
    private final GameState gameState;
    private boolean isProcessingEvent;
    private final Vector2 lastProcessedPosition;

    public EventManager(GameState gameState) {
        this.gameState = gameState;
        this.eventMap = new HashMap<>();
        this.isProcessingEvent = false;
        this.lastProcessedPosition = new Vector2(-1, -1);
    }

    public void registerEvent(MapEvent event) {
        Vector2 position = new Vector2(event.getX(), event.getY());
        eventMap.computeIfAbsent(position, k -> new ArrayList<>()).add(event);
        System.out.println("Event registered at: " + position);
    }

    public void registerEvents(List<MapEvent> events) {
        if (events != null) {
            for (MapEvent event : events) {
                registerEvent(event);
            }
        }

    }

    public void clearEvents() {
        eventMap.clear();
        lastProcessedPosition.set(-1, -1);
    }

    public void checkEvents(Player player) {
        if (isProcessingEvent) {
            return;
        }

        int playerX = (int) Math.floor(player.getPlaceX());
        int playerY = (int) Math.floor(player.getPlaceY());
        Vector2 playerPos = new Vector2(playerX, playerY);

        if (!playerPos.equals(lastProcessedPosition)) {
            List<MapEvent> eventsAtPosition = eventMap.get(playerPos);
            if (eventsAtPosition != null) {
                System.out.println("Found " + eventsAtPosition.size() + " events at position " + playerPos);  // Verifica eventi
                for (MapEvent event : eventsAtPosition) {
                    processEvent(event);
                }
            } else {
                System.out.println("No events found at position " + playerPos);  // Nessun evento trovato
            }
            lastProcessedPosition.set(playerPos);
        }
    }

    private void processEvent(MapEvent event) {
        isProcessingEvent = true;

        try {
            switch (event.getType()) {
                case TELEPORT:
                    handleTeleport((TeleportEvent) event);
                    break;
                case POKEMON_ENCOUNTER:
                    System.out.println("eccoci");
                    handlePokemonEncounter((PokemonEncounterEvent) event);
                    break;
            }
        } finally {
            isProcessingEvent = false;
        }
    }

    private void handleTeleport(TeleportEvent event) {
        // Salva gli eventi della mappa corrente prima del teletrasporto
        Place currentPlace = gameState.getCurrentPlace();
        List<MapEvent> currentEvents = currentPlace != null ? currentPlace.getEvents() : null;

        gameState.getPlayer().cancelMove();
        // Esegui il teletrasporto
        event.trigger(gameState);

        // Aggiorna la mappa degli eventi con la nuova posizione
        Place newPlace = gameState.getCurrentPlace();
        if (newPlace != null && newPlace.getEvents() != null) {
            clearEvents();
            registerEvents(newPlace.getEvents());
        }

        // Reset della posizione processata per permettere nuovi eventi
        lastProcessedPosition.set(-1, -1);
    }

    private void handlePokemonEncounter(PokemonEncounterEvent event) {
        if (event.isActive()) {
            System.out.println("Pokemon encounter triggered!");
            event.trigger(gameState);
        } else {
            reactivateEncounterTiles();
        }
    }

    public void reactivateEncounterTiles() {
        for (List<MapEvent> events : eventMap.values()) {
            for (MapEvent event : events) {
                if (event instanceof PokemonEncounterEvent) {
                    ((PokemonEncounterEvent) event).reactivateTile();
                }
            }
        }
        // Reset della posizione processata per permettere nuovi eventi
        lastProcessedPosition.set(-1, -1);
    }

}
