package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.capture.CaptureLogic;
import com.coseemo.pkmnambra.capture.CaptureRenderer;
import com.coseemo.pkmnambra.controller.CaptureController;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.util.GameState;
import com.coseemo.pkmnambra.util.Observer;

public class CaptureScreen implements Screen, Observer {
    private Game game;
    private Pokemon currentPokemon;
    private CaptureLogic captureLogic;
    private CaptureController captureController;
    private GameState gameState;

    // State variables for exit logic
    private boolean notExit;
    private boolean waitingForKeyPress;
    private boolean keyPressed;
    private float exitTimer;
    private static final float EXIT_DELAY = 0.05f; // Delay before changing screen

    private CaptureRenderer captureRenderer;
    private String eventType;

    public CaptureScreen(GameState gameState, Pokemon pokemon) {
        this.game = gameState.getGame();
        this.currentPokemon = pokemon;
        this.notExit = true;
        this.gameState = GameState.getInstance();
        this.captureLogic = new CaptureLogic(currentPokemon,this);
        this.captureRenderer = new CaptureRenderer();
        this.captureController = new CaptureController(captureRenderer.getOptionBox(), this);

        gameState.getEventNotifier().registerObserver(this);
        Gdx.input.setInputProcessor(captureController);

        resetExitState();
    }

    @Override
    public void show() {
        // Actions to perform when the screen is shown
    }

    @Override
    public void render(float delta) {
        captureRenderer.render(delta);
        updateCaptureUI();

        if (waitingForKeyPress) {
            handleKeyPress(delta);
        }
    }

    private void updateCaptureUI() {
        captureRenderer.updateCaptureProbability(captureLogic.getCaptureProbability());
        captureRenderer.updateAngerLevel(captureLogic.getAngerLevel());
    }

    private void handleKeyPress(float delta) {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && !keyPressed) {
            keyPressed = true; // Wait until a key is pressed
        }

        if (keyPressed && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            exitTimer += delta;
            if (exitTimer >= EXIT_DELAY) {
                changeScreen();
                System.out.println("zao");
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle viewport resizing if necessary
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); // Remove input processor when hiding
    }

    @Override
    public void dispose() {
        gameState.getEventNotifier().deregisterObserver(this);
        gameState.getPlayer().cancelMove();
        captureRenderer.dispose();
    }

    @Override
    public void update(String eventType) {
        // Only process events if the capture logic is not in a disrupted state
        if (captureLogic.isCaptureInterrupted()) {
            return; // Skip processing if capture is interrupted
        }

        switch (eventType) {
            case "USE_STANDARDBAIT":
            case "USE_SPICYBAIT":
            case "USE_SWEETBAIT":
            case "USE_SMELLYBAIT":
                handleBait(eventType);
                break;
            case "USE_FLORALPERFUME":
            case "USE_FRUITYPERFUME":
            case "USE_HERBALPERFUME":
            case "USE_MYSTICPERFUME":
                handlePerfume(eventType);
                break;
            case "USE_BASICTRAP":
            case "USE_ADVANCEDTRAP":
            case "USE_TRICKYTRAP":
            case "USE_QUICKTRAP":
                handleTrap(eventType);
                break;
            case "USE_POKEBALL":
            case "USE_GREATBALL":
            case "USE_ULTRABALL":
            case "USE_MASTERBALL":
                captureLogic.attemptCapture(getBallIndex(eventType));
                break;
            case "CAPTURE_FAIL":
                captureRenderer.updateStatusMessage(currentPokemon.getName() + " broke free!");
                break;
            case "POKEMON_FLED":
                captureRenderer.updateStatusMessage(currentPokemon.getName() + " has fled.");
                break;
            case "POKEMON_ANGER":
                captureRenderer.updateStatusMessage(currentPokemon.getName() + " is too angry, run!");
                break;
            case "CAPTURE_SUCCESS":
                handleCaptureSuccess();
                gameState.getPlayer().printTeam();
                break;
            case "FLEE":
                captureRenderer.updateStatusMessage("Better run!");
                break;
        }
        this.eventType = eventType;
        handleExitState(eventType);
    }

    private void handleBait(String eventType) {
        int baitIndex = getBaitIndex(eventType);
        captureRenderer.updateStatusMessage("You used bait! ");
        captureLogic.handleBait(baitIndex);
    }

    private void handlePerfume(String eventType) {
        int perfumeIndex = getPerfumeIndex(eventType);
        captureRenderer.updateStatusMessage("You used perfume! ");
        captureLogic.handlePerfume(perfumeIndex);
    }

    private void handleTrap(String eventType) {
        int trapIndex = getTrapIndex(eventType);
        captureRenderer.updateStatusMessage("You set a trap! ");
        captureLogic.handleTrap(trapIndex);
    }

    private void handleCaptureSuccess() {
        if (gameState.getPlayer().addPokemon(currentPokemon)) {
            captureRenderer.updateStatusMessage("You caught " + currentPokemon.getName() + "!!!");
            eventType = "CATCH_SUCC";
        } else {
            captureRenderer.updateStatusMessage("Team is full, go to prof!");
            eventType = "CATCH_FULL";
        }
    }

    private void handleExitState(String eventType) {
        if (eventType.equals("POKEMON_FLED") ||
            eventType.equals("POKEMON_ANGER") ||
            eventType.equals("CAPTURE_SUCCESS") ||
            eventType.equals("FLEE")) {
            System.out.println(this.eventType);
            notExit = false;
            waitingForKeyPress = true;
            keyPressed = false;
            exitTimer = 0;
            captureController.setMustPress(true);
        }
    }

    private void changeScreen() {
        if(!notExit) {
            resetExitState();
            dispose();
            game.setScreen(new GameScreen(gameState));
            System.out.println("zao");
        }
    }

    private void resetExitState() {
        captureController.setMustPress(false);
        this.waitingForKeyPress = false;
        this.keyPressed = false;
        this.exitTimer = 0;
        this.notExit = true;
    }

    private int getBallIndex(String eventType) {
        switch (eventType) {
            case "USE_POKEBALL": return 0;
            case "USE_GREATBALL": return 1;
            case "USE_ULTRABALL": return 2;
            case "USE_MASTERBALL": return 3;
            default: return -1;
        }
    }

    private int getBaitIndex(String eventType) {
        switch (eventType) {
            case "USE_STANDARDBAIT": return 0;
            case "USE_SPICYBAIT": return 1;
            case "USE_SWEETBAIT": return 2;
            case "USE_SMELLYBAIT": return 3;
            default: return -1;
        }
    }

    private int getPerfumeIndex(String eventType) {
        switch (eventType) {
            case "USE_FLORALPERFUME": return 0;
            case "USE_FRUITYPERFUME": return 1;
            case "USE_HERBALPERFUME": return 2;
            case "USE_MYSTICPERFUME": return 3;
            default: return -1;
        }
    }

    private int getTrapIndex(String eventType) {
        switch (eventType) {
            case "USE_BASICTRAP": return 0;
            case "USE_ADVANCEDTRAP": return 1;
            case "USE_TRICKYTRAP": return 2;
            case "USE_QUICKTRAP": return 3;
            default: return -1;
        }
    }

}
