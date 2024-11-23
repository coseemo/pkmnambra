package com.coseemo.pkmnambra.singleton;

import com.badlogic.gdx.Screen;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.screen.TransitionScreen;


public class GameState {
    private static GameState instance; // Istanza del Singleton

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

    public void changeScreen(Screen newScreen) {
        Screen currentScreen = getScreenManager().getCurrentScreen();
        if(newScreen == null){
            game.setScreen(new TransitionScreen(game, currentScreen, currentScreen));
        } else {
            game.setScreen(new TransitionScreen(game, currentScreen, newScreen));
            getScreenManager().setCurrentScreen(newScreen);
        }
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
