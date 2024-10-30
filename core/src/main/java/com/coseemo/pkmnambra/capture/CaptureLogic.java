package com.coseemo.pkmnambra.capture;

import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.util.EventNotifier;

public class CaptureLogic {
    private Pokemon currentPokemon;
    private EventNotifier eventNotifier;
    private float captureProbability;
    private float angerLevel;
    private boolean playerTurn;

    public CaptureLogic(Pokemon pokemon, EventNotifier eventNotifier) {
        this.currentPokemon = pokemon;
        this.eventNotifier = eventNotifier;
        this.captureProbability = pokemon.getBaseCaptureRate();
        this.angerLevel = pokemon.getBaseAngerLevel();
        this.playerTurn = true;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void attemptCapture() {
        // Controllo se il Pokémon fugge prima di tentare la cattura
        if (shouldPokemonFlee()) {
            eventNotifier.notifyObservers("POKEMON_FLED");
            return; // Non tentare la cattura se il Pokémon è fuggito
        }

        // Tentativo di cattura
        if (Math.random() * 100 < captureProbability) {
            eventNotifier.notifyObservers("CAPTURE_SUCCESS");
        } else {
            eventNotifier.notifyObservers("CAPTURE_FAIL");
            increaseAngerLevel(10); // Aumenta il livello di rabbia dopo un tentativo fallito
        }

        playerTurn = false; // Termina il turno del giocatore
    }

    public void handleBait() {
        captureProbability = Math.min(100, captureProbability + currentPokemon.getBaitEffect());
        eventNotifier.notifyObservers("BAIT_USED");
        playerTurn = false;
    }

    public void handlePerfume() {
        angerLevel = Math.max(0, angerLevel - currentPokemon.getPerfumeEffect());
        eventNotifier.notifyObservers("PERFUME_USED");
        playerTurn = false;
    }

    public void handleTrap() {
        captureProbability = Math.min(100, captureProbability + 50); // Aumenta molto la probabilità di cattura
        increaseAngerLevel(30); // Aumenta notevolmente la rabbia
        eventNotifier.notifyObservers("TRAP_SET");
        playerTurn = false;
    }


    // Metodo per determinare se il Pokémon scappa
    private boolean shouldPokemonFlee() {
        // Scappa se la rabbia è al massimo
        if (angerLevel >= 100) {
            return true; // Il Pokémon fugge se la rabbia è al massimo
        }

        // Controlla se il Pokémon scappa casualmente (10% di probabilità)
        return Math.random() < 0.10; // Cambia 0.10 per modificare la probabilità di fuga
    }

    public void handlePokemonReaction() {
        // Se il Pokémon fugge a causa della rabbia massima
        if (shouldPokemonFlee()) {
            eventNotifier.notifyObservers("POKEMON_FLED");
        } else {
            playerTurn = true; // Rendi il turno del giocatore di nuovo attivo
        }
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

    public void setPlayerTurn(boolean value) {
        this.playerTurn = value;
    }
}
