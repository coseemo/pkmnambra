package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.characters.Actor;
import com.coseemo.pkmnambra.characters.ActorBehavior;
import com.coseemo.pkmnambra.characters.ProfessorBehavior;
import com.coseemo.pkmnambra.maplogic.*;

public class MapLoader {
    private static final String OBJECT_MARKER = "OBJECTS:";
    private static final String TERRAIN_MARKER = "TERRAIN:";
    private static final String EVENTS_MARKER = "EVENTS:";
    private static final String NPCS_MARKER = "NPCS:";

    public static World loadMapAndObjects(String fileName, AssetManager assetManager) {
        FileHandle file = Gdx.files.internal(fileName);
        String content = file.readString();

        String[] npcSections = content.split(NPCS_MARKER);
        String beforeNPCContent = npcSections[0].trim();
        String npcData = npcSections.length > 1 ? npcSections[1].trim() : "";

        String[] mainSections = beforeNPCContent.split(EVENTS_MARKER);
        String mainContent = mainSections[0].trim();
        String eventsData = mainSections.length > 1 ? mainSections[1].trim() : "";

        String[] terrainSections = mainContent.split(OBJECT_MARKER);
        String terrainData = terrainSections[0].split(TERRAIN_MARKER)[1].trim();
        String objectData = terrainSections.length > 1 ? terrainSections[1].trim() : "";

        // Create map and world
        TileMap tileMap = createTileMap(terrainData);
        World world = new World(tileMap, assetManager);

        // Load objects using the factory
        if (!objectData.isEmpty()) {
            loadObjects(world, objectData, assetManager);
        }

        // Load NPCs con database dialoghi
        if (!npcData.isEmpty()) {
            loadNPCs(world, npcData, assetManager);
        }



        return world;
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

    private static void loadObjects(World world, String objectData, AssetManager assetManager) {
        String[] objectLines = objectData.split("\n");

        for (String line : objectLines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.trim().split(" ");
            if (parts.length < 3) continue;

            try {
                String type = parts[0].toLowerCase();
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                // Additional parameters for special objects
                String[] additionalParams = new String[parts.length - 3];
                System.arraycopy(parts, 3, additionalParams, 0, parts.length - 3);

                // Create object using factory and add to world
                WorldObject object = MapObjectFactory.createObject(type, x, y, assetManager, additionalParams);
                world.addObject(object);

                Gdx.app.log("MapLoader", "Added object: " + type + " at (" + x + ", " + y + ")");
            } catch (Exception e) {
                Gdx.app.error("MapLoader", "Error loading object: " + line, e);
            }
        }
    }

    private static void loadNPCs(World world, String npcData, AssetManager assetManager) {
        String[] npcLines = npcData.split("\n");

        for (String line : npcLines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.trim().split(" ");
            if (parts.length < 3) continue;

            try {
                String npcType = parts[0].toLowerCase();
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                if (npcType.equals("professor")) {
                    AnimationSet professorAnimations = loadprof(assetManager);
                    Actor professor = new Actor("Professor", world, x, y, professorAnimations);

                    ProfessorBehavior professorBehavior = new ProfessorBehavior(professor);

                    world.addActor(professor, professorBehavior);

                    Gdx.app.log("MapLoader", "Added professor NPC at (" + x + ", " + y + ")");
                }
            } catch (Exception e) {
                Gdx.app.error("MapLoader", "Error loading NPC: " + line, e);
            }
        }
    }

    private static AnimationSet loadprof(AssetManager assetManager){
        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        return new AnimationSet(
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_east"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_west"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_north"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
            atlas.findRegion("mimi_standing_east"),
            atlas.findRegion("mimi_standing_west"),
            atlas.findRegion("mimi_standing_north"),
            atlas.findRegion("mimi_standing_south")
        );
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
