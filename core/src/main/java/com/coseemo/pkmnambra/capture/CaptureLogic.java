package com.coseemo.pkmnambra.capture;

import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.screen.CaptureScreen;
import com.coseemo.pkmnambra.util.EventNotifier;
import com.coseemo.pkmnambra.util.states.GameState;


public class CaptureLogic {
    private final Pokemon currentPokemon;
    private final EventNotifier eventNotifier;
    private final Player player;
    private float captureProbability;
    private float angerLevel;
    private boolean captureInterrupted;
    private GameState gameState;


    public CaptureLogic(Pokemon pokemon, GameState gameState) {

        this.currentPokemon = pokemon;
        this.gameState = gameState;
        this.eventNotifier = gameState.getEventManager().getEventNotifier();
        this.player = gameState.getPlayerState().getPlayer();
        this.captureInterrupted = false;
        this.captureProbability = pokemon.getBaseCaptureRate();
        this.angerLevel = pokemon.getBaseAngerLevel();
    }

    public void attemptCapture(int pokeballIndex) {
        player.printTeam();
        resetCaptureState();
        double adjustedCaptureProbability;

        if (pokeballIndex != 3) {
            captureProbability = Math.min(100, captureProbability + currentPokemon.getPokeballEffect(pokeballIndex));
            double angerImpact = angerLevel / 100.0;
            adjustedCaptureProbability = captureProbability * (1 - angerImpact);
        } else {
            adjustedCaptureProbability = 100;
        }


        if (Math.random() * 100 < adjustedCaptureProbability) {
            endBattleWithMessage("CAPTURE_SUCCESS");
        } else {
            eventNotifier.notifyObservers("CAPTURE_FAIL");
            increaseAngerLevel(10);
            handlePokemonReaction();
        }
    }

    public void handleBait(int baitIndex) {
        captureProbability = Math.min(100, captureProbability + currentPokemon.getBaitEffect(baitIndex));
        handlePokemonReaction();
    }

    public void handlePerfume(int perfumeIndex) {
        angerLevel = Math.max(0, angerLevel - currentPokemon.getPerfumeEffect(perfumeIndex));
        handlePokemonReaction();
    }

    public void handleTrap(int trapIndex) {

        captureProbability = Math.min(100, captureProbability + currentPokemon.getTrapEffect(trapIndex));
        increaseAngerLevel(50);
        handlePokemonReaction();
    }

    public void handlePokemonReaction() {
        if (angerLevel >= 100) {
            endBattleWithMessage("POKEMON_ANGER");
        } else if (Math.random() <= 0.10) {
            endBattleWithMessage("POKEMON_FLED");
        }
        increaseAngerLevel(5);
    }

    private void endBattleWithMessage(String message) {
        eventNotifier.notifyObservers(message);
    }

    public float getCaptureProbability() {
        return captureProbability;
    }

    public float getAngerLevel() {
        return angerLevel;
    }

    private void increaseAngerLevel(int amount) {
        angerLevel = Math.min(100, angerLevel + amount);
    }

    public void increaseAngerOverTime() {
        angerLevel = Math.min(100, angerLevel + 5);
    }

    public boolean isCaptureInterrupted() {
        return captureInterrupted;
    }

    private void resetCaptureState() {
        this.angerLevel = currentPokemon.getBaseAngerLevel();
        this.captureProbability = currentPokemon.getBaseCaptureRate();
        this.captureInterrupted = false;
    }
}
