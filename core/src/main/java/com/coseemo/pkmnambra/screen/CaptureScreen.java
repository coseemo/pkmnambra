package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.capture.CaptureLogic;
import com.coseemo.pkmnambra.capture.CaptureRenderer;
import com.coseemo.pkmnambra.controller.CaptureController;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.util.EventNotifier;
import com.coseemo.pkmnambra.util.Observer;

public class CaptureScreen implements Screen, Observer {
    private Game game;
    private Pokemon currentPokemon;
    private EventNotifier eventNotifier;
    private CaptureLogic captureLogic;
    private CaptureRenderer captureRenderer;

    public CaptureScreen(Game game, Pokemon pokemon, EventNotifier eventNotifier) {
        this.game = game;
        this.currentPokemon = pokemon;
        this.eventNotifier = eventNotifier;
        this.eventNotifier.registerObserver(this);

        // Inizializza la logica della cattura e il renderer
        captureLogic = new CaptureLogic(currentPokemon, eventNotifier);
        captureRenderer = new CaptureRenderer(eventNotifier);

        CaptureController captureController = new CaptureController(captureRenderer.getOptionBox(), eventNotifier);
        Gdx.input.setInputProcessor(captureController);
    }

    @Override
    public void show() {
        // Potresti voler implementare qui eventuali azioni da eseguire quando la schermata viene mostrata
    }

    @Override
    public void render(float delta) {
        captureRenderer.render(delta);

        // Aggiorna barre e messaggi
        captureRenderer.updateCaptureProbability(captureLogic.getCaptureProbability());
        captureRenderer.updateAngerLevel(captureLogic.getAngerLevel());
    }

    @Override
    public void resize(int width, int height) {
        // Puoi gestire il ridimensionamento della vista se necessario
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        captureRenderer.dispose();
    }

    @Override
    public void update(String eventType) {
        if (!captureLogic.isPlayerTurn()) return;

        switch (eventType) {
            case "BAIT":
                captureLogic.handleBait();
                captureRenderer.updateStatusMessage("You used bait! ");
                break;
            case "PERFUME":
                captureLogic.handlePerfume();
                captureRenderer.updateStatusMessage("You used perfume! ");
                break;
            case "TRAP":
                captureLogic.handleTrap();
                captureRenderer.updateStatusMessage("You set a trap! ");
                break;
            case "ATTEMPT_CAPTURE":
                captureLogic.attemptCapture();
                break;
            case "CAPTURE_SUCCESS":
                captureRenderer.updateStatusMessage("You caught " + currentPokemon.getName() + "!!!");
                break;
            case "CAPTURE_FAIL":
                captureRenderer.updateStatusMessage(currentPokemon.getName() + "is free !!!");
                break;
            case "POKEMON_FLED":
            case "FLEE":
                // Gestisci la fuga qui
                game.setScreen(new GameScreen((Main) game, eventNotifier));
                break;
        }

        captureRenderer.updateCaptureProbability(captureLogic.getCaptureProbability());
        captureRenderer.updateAngerLevel(captureLogic.getAngerLevel());

        captureLogic.setPlayerTurn(true);
    }
}
