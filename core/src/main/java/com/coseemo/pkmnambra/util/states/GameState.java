package com.coseemo.pkmnambra.util.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.World;

public class GameState {
    private static GameState instance; // Singleton instance

    private Main game;
    private PlayerState playerState;
    private MapState mapState;
    private ScreenManager screenManager;
    private ResourceManager resourceManager;
    private EventManager eventManager;

    private GameState() {}

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }


    public void initialize(Main game, Player player, World startingPlace) {
        this.game = game;
        this.playerState = new PlayerState(player);
        this.mapState = new MapState();
        this.mapState.setCurrentPlace(startingPlace);
        this.mapState.addPlace("starting_map", startingPlace);
        this.screenManager = new ScreenManager(game);
        this.resourceManager = new ResourceManager();
        this.eventManager = new EventManager();
    }


    // Salvataggio su file
    public void saveToFile() {
        Json json = new Json();

        String playerData = json.toJson(this.getPlayerState().getPlayer());
        // Salva il World senza riferimenti ciclici
        String worldData = json.toJson(this.getMapState().getCurrentPlace());

        Gdx.files.local("saves/save.json").writeString("{\"player\":" + playerData + ", \"world\":" + worldData + "}", false);
        Gdx.app.log("GameState", "Partita salvata correttamente in " + "saves/");
    }

    // Caricamento da file
    public GameState loadGame() {
        String saveData = Gdx.files.local("save.json").readString(); // Leggi il file
        Json json = new Json();
        JsonValue root = new JsonReader().parse(saveData); // Parso i dati JSON

        // Ricostruisci il Player
        JsonValue playerData = root.get("player");
        Player player = json.fromJson(Player.class, playerData.toString()); // Usa la stringa JSON

        // Ricostruisci il World
        JsonValue worldData = root.get("world");
        World world = json.fromJson(World.class, worldData.toString());

        // Ricostruisci GameState
        GameState gameState = GameState.getInstance();
        gameState.initialize(this.game, player, world);

        return gameState;
    }



    // Getter per accedere alle proprietà
    public Main getGame() {
        return game;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public MapState getMapState() {
        return mapState;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
