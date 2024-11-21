package com.coseemo.pkmnambra.util.states;

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
    private static final String SAVE_FILE = "saves/save.json";


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
