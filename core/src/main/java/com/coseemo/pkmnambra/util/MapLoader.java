package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.coseemo.pkmnambra.characters.Professor;
import com.coseemo.pkmnambra.characters.logic.NPC;
import com.coseemo.pkmnambra.events.PokemonEncounterEvent;
import com.coseemo.pkmnambra.events.TeleportEvent;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.maplogic.TERRAIN;
import com.coseemo.pkmnambra.maplogic.TileMap;
import com.coseemo.pkmnambra.pokemons.Pokemon;

import java.util.Random;

public class MapLoader {
    private static final String OBJECT_MARKER = "OBJECTS:";
    private static final String TERRAIN_MARKER = "TERRAIN:";
    private static final String EVENTS_MARKER = "EVENTS:";
    private static final String NPCS_MARKER = "NPCS:";

    public static Place loadMapAndObjects(String fileName, AssetManager assetManager) {
        FileHandle file = Gdx.files.internal(fileName);
        String content = file.readString();

        String[] npcSections = content.split("NPCS:");
        String beforeNPCContent = npcSections[0].trim();
        String npcData = npcSections.length > 1 ? npcSections[1].trim() : "";

// Dividi il contenuto principale in terreno, oggetti ed eventi
        String[] mainSections = beforeNPCContent.split(EVENTS_MARKER);
        String mainContent = mainSections[0].trim();
        String eventsData = mainSections.length > 1 ? mainSections[1].trim() : "";
        Gdx.app.log("MapLoader", "NPC data: " + npcData);

// Dividi il terreno e gli oggetti
        String[] terrainSections = mainContent.split(OBJECT_MARKER);
        String terrainData = terrainSections[0].split(TERRAIN_MARKER)[1].trim();
        String objectData = terrainSections.length > 1 ? terrainSections[1].trim() : "";

        // Creazione della mappa
        TileMap tileMap = createTileMap(terrainData);

        // Creazione della Place con la mappa
        Place place = new Place(tileMap, assetManager);

        // Caricamento degli oggetti
        if (!objectData.isEmpty()) {
            loadObjects(place, objectData);
        }

        // Caricamento degli eventi
        if (!eventsData.isEmpty()) {
            loadEvents(place, eventsData);
        }

        // Caricamento degli NPC
        if (!npcData.isEmpty()) {
            loadNPCs(place, npcData, assetManager);
        }

        return place;
    }

    private static TileMap createTileMap(String terrainData) {
        String[] lines = terrainData.split("\n");

        int width = lines[0].split(" ").length;
        int height = lines.length;

        TileMap tileMap = new TileMap(width, height);

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
                            event.addPossibleEncounter("Parasect");
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

    private static void loadNPCs(Place place, String npcData, AssetManager assetManager) {
        if (npcData == null || npcData.trim().isEmpty()) {
            Gdx.app.log("MapLoader", "No NPC data found.");
            return;
        }

        String[] npcLines = npcData.split("\n");
        for (String line : npcLines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) continue; // Ignora commenti o righe vuote

            try {
                String[] parts = line.split("\\s+");
                if (parts.length < 4) {
                    Gdx.app.error("MapLoader", "Invalid NPC definition: " + line);
                    continue;
                }

                String npcType = parts[0].toUpperCase();
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                String npcId = parts[3];

                // Crea l'NPC
                NPC npc = createNPCByType(place, npcType, x, y, npcId, assetManager);
                if (npc != null) {
                    place.addActor(npc);
                    Gdx.app.log("MapLoader", "NPC added: " + npcType + " at (" + x + ", " + y + ")");
                } else {
                    Gdx.app.error("MapLoader", "Failed to create NPC: " + line);
                }

            } catch (Exception e) {
                Gdx.app.error("MapLoader", "Error loading NPC: " + line, e);
            }
        }
    }

    private static NPC createNPCByType(Place place, String npcType, int x, int y,
                                       String npcId, AssetManager assetManager) {
        try {
            switch (npcType) {
                case "PROFESSOR":
                    return new Professor(place, x, y, npcId, assetManager);
                // Aggiungi altri tipi di NPC qui
                default:
                    Gdx.app.error("MapLoader", "Unknown NPC type: " + npcType);
                    return null;
            }
        } catch (Exception e) {
            Gdx.app.error("MapLoader", "Error creating NPC of type " + npcType, e);
            return null;
        }
    }

    private static TERRAIN getTerrainFromChar(String terrainChar) {
        switch (terrainChar) {
            case "S1":
                return TERRAIN.SAND_1;
            case "S2":
                return TERRAIN.SAND_2;
            default:
                return TERRAIN.SAND_1;
        }
    }
}
