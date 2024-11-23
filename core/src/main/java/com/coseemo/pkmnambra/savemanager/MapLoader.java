package com.coseemo.pkmnambra.savemanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.map.TileMap;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.actors.ProfessorBehavior;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.dialogue.DialogueLoader;
import com.coseemo.pkmnambra.maplogic.*;
import com.coseemo.pkmnambra.mapobjectfactory.MapObjectFactory;
import com.coseemo.pkmnambra.mapobjectfactory.WorldObject;
import com.coseemo.pkmnambra.util.AnimationSet;

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

        // Creo la mappa e il mondo
        TileMap tileMap = createTileMap(terrainData);
        World world = new World(fileName, tileMap, assetManager);

        // Carico gli oggetti utilizzando la factory
        if (!objectData.isEmpty()) {
            loadObjects(world, objectData, assetManager);
        }

        // Carico gli NPC con il database dei dialoghi
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

                // Gestisco eventuali parametri aggiuntivi per oggetti speciali
                String[] additionalParams = new String[parts.length - 3];
                System.arraycopy(parts, 3, additionalParams, 0, parts.length - 3);

                // Creo l'oggetto usando la factory e lo aggiungo al mondo
                WorldObject object = MapObjectFactory.createObject(type, x, y, assetManager, additionalParams);
                world.addObject(object);

                Gdx.app.log("MapLoader", "Aggiunto oggetto: " + type + " a (" + x + ", " + y + ")");
            } catch (Exception e) {
                Gdx.app.error("MapLoader", "Errore durante il caricamento dell'oggetto: " + line, e);
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

                    AnimationSet professorAnimations = loadMimi(assetManager);

                    Actor professor = new Actor(npcType, world, x, y, professorAnimations);
                    ProfessorBehavior professorBehavior = new ProfessorBehavior(professor);

                    world.addNPC(professorBehavior);

                    Gdx.app.log("MapLoader", "Aggiunto NPC professor a (" + x + ", " + y + ")");
                }
            } catch (Exception e) {
                Gdx.app.error("MapLoader", "Errore durante il caricamento dell'NPC: " + line, e);
            }
        }
    }

    public static World loadMapFromSave(SaveData saveData, AssetManager assetManager) {

        loadAsset(assetManager);

        World world = loadMapAndObjects(saveData.mapFileName, assetManager);

        // Ripristino il giocatore
        Player player = new Player(world, saveData.playerData.x, saveData.playerData.y, loadMimi(assetManager));
        player.setToCatch(saveData.playerData.toCatch);
        player.setHasInventory(saveData.playerData.hasInventory);
        player.getInventory().loadItems(saveData.playerData.inventory);
        player.getTeam().addAll(saveData.playerData.team);
        world.addPlayer(player);

        return world;
    }

    private static void loadAsset(AssetManager assetManager) {
        assetManager.load("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/sands_packed/sandspacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/houses_packed/housespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/tiles/runtiles_packed/runtilespacked.atlas", TextureAtlas.class);
        assetManager.load("assets/sprites/professorpacked/professorpacked.atlas", TextureAtlas.class);
        assetManager.load("assets/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("assets/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.load("assets/tiles/tilespack/tilespack.atlas", TextureAtlas.class);
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        assetManager.finishLoading();
    }

    private static AnimationSet loadMimi(AssetManager assetManager) {
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

    private static AnimationSet loadProf(AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/sprites/professorpacked/professorpacked.atlas", TextureAtlas.class);
        return new AnimationSet(
            new Animation<>(0.3f / 2f, atlas.findRegions("kukui_walking_east"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("kukui_walking_west"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("kukui_walking_north"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("kukui_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
            atlas.findRegion("kukui_standing_east"),
            atlas.findRegion("kukui_standing_west"),
            atlas.findRegion("kukui_standing_north"),
            atlas.findRegion("kukui_standing_south")
        );
    }

    private static TERRAIN getTerrainFromChar(String terrainChar) {
        switch (terrainChar) {
            case "S1":
                return TERRAIN.SAND_1;
            case "S2":
                return TERRAIN.SAND_2;
            case "F":
                return TERRAIN.FLOOR;
            default:
                return TERRAIN.SAND_1;
        }
    }
}
