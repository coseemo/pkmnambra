package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.assets.AssetManager;
import com.coseemo.pkmnambra.events.PokemonEncounterEvent;
import com.coseemo.pkmnambra.events.TeleportEvent;
import com.coseemo.pkmnambra.maplogic.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.coseemo.pkmnambra.pokemons.Pokemon;

public class MapLoader {
    private static final String OBJECT_MARKER = "OBJECTS:";
    private static final String TERRAIN_MARKER = "TERRAIN:";
    private static final String EVENTS_MARKER = "EVENTS:";

    public static Place loadMapAndObjects(String fileName, AssetManager assetManager) {
        FileHandle file = Gdx.files.internal(fileName);
        String content = file.readString();

        // Dividi il contenuto in sezioni
        String[] sections = content.split(EVENTS_MARKER);
        String mainContent = sections[0].trim();
        String eventsData = sections.length > 1 ? sections[1].trim() : "";

        // Dividi il contenuto principale in terreno e oggetti
        String[] mainSections = mainContent.split(OBJECT_MARKER);
        String terrainData = mainSections[0].split(TERRAIN_MARKER)[1].trim();
        String objectData = mainSections.length > 1 ? mainSections[1].trim() : "";

        // Crea la mappa
        TileMap tileMap = createTileMap(terrainData);

        // Crea la Place con la mappa appena creata
        Place place = new Place(tileMap, assetManager);

        // Carica gli oggetti
        if (!objectData.isEmpty()) {
            loadObjects(place, objectData);
        }

        // Carica gli eventi
        if (!eventsData.isEmpty()) {
            loadEvents(place, eventsData);
        }

        return place;
    }

    private static void loadEvents(Place place, String eventsData) {
        String[] eventLines = eventsData.split("\n");

        for (String line : eventLines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = line.split("\\s+");
                String eventType = parts[0].toLowerCase();

                switch (eventType) {
                    case "encounter":
                        if (parts.length >= 4) {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            float rate = Float.parseFloat(parts[3]);
                            PokemonEncounterEvent event = new PokemonEncounterEvent(x, y, rate);
                            event.addPossibleEncounter(new Pokemon("Parasect", 50, 20));
                            place.addEvent(event);
                        }
                        break;

                    case "teleport":
                        if (parts.length >= 6) {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            String targetMap = parts[3];
                            int targetX = Integer.parseInt(parts[4]);
                            int targetY = Integer.parseInt(parts[5]);
                            place.addEvent(new TeleportEvent(x, y, targetMap, targetX, targetY));
                        }
                        break;

                    default:
                        Gdx.app.error("MapLoader", "Unknown event type: " + eventType);
                }
            } catch (NumberFormatException e) {
                Gdx.app.error("MapLoader", "Error parsing event at line: " + line, e);
            } catch (Exception e) {
                Gdx.app.error("MapLoader", "Error processing event at line: " + line, e);
            }
        }
    }

    private static TileMap createTileMap(String terrainData) {
        String[] lines = terrainData.split("\n");

        // Determina dimensioni mappa
        int width = lines[0].split(" ").length;
        int height = lines.length;

        TileMap tileMap = new TileMap(width, height);

        // Carica i terreni
        for (int y = height - 1; y >= 0; y--) {
            String[] tiles = lines[height - 1 - y].trim().split(" ");
            for (int x = 0; x < width; x++) {
                TERRAIN terrain = getTerrainFromChar(tiles[x]);
                tileMap.getTile(x, y).setTerrain(terrain);
            }
        }

        return tileMap;
    }

    private static void loadObjects(Place place, String objectData) {
        String[] objectLines = objectData.split("\n");

        for (String line : objectLines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.trim().split(" ");
            if (parts.length < 4) continue;

            String type = parts[0];
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            boolean isAnimated = Boolean.parseBoolean(parts[3]);

            try {
                if (isAnimated) {
                    place.addAnimatedObject(type, x, y);
                } else {
                    place.addStaticObject(type, x, y);
                }
            } catch (IllegalArgumentException e) {
                Gdx.app.error("MapLoader", "Invalid object type: " + type, e);
            }
        }
    }

    private static TERRAIN getTerrainFromChar(String terrainChar) {
        switch (terrainChar) {
            case "S1": return TERRAIN.SAND_1;
            case "S2": return TERRAIN.SAND_2;
            default: return TERRAIN.SAND_1;
        }
    }
}
