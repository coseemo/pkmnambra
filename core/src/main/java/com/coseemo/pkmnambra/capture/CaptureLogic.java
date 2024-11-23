package com.coseemo.pkmnambra.capture;

import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.pokemonfactory.Pokemon;
import com.coseemo.pkmnambra.captureobserver.CaptureEventNotifier;
import com.coseemo.pkmnambra.singleton.GameState;

public class CaptureLogic {
    private final Pokemon currentPokemon;
    private CaptureEventNotifier eventNotifier;
    private final Player player;
    private float captureProbability;
    private float angerLevel;
    private boolean captureInterrupted;
    private GameState gameState;

    // Costruttore che inizializza la logica di cattura per un determinato Pokémon
    public CaptureLogic(Pokemon pokemon, GameState gameState) {
        this.currentPokemon = pokemon;
        this.gameState = gameState;
        this.eventNotifier = gameState.getEventManager().getEventNotifier();
        this.player = gameState.getPlayerState().getPlayer();
        this.captureInterrupted = false;
        this.captureProbability = pokemon.getBaseCaptureRate();
        this.angerLevel = pokemon.getBaseAngerLevel();
    }

    // Tentativo di cattura del Pokémon con un determinato Pokéball
    public void attemptCapture(int pokeballIndex) {
        double adjustedCaptureProbability;

        if (pokeballIndex != 3) {
            captureProbability = Math.min(100, captureProbability + currentPokemon.getPokeballEffect(pokeballIndex));
            double angerImpact = angerLevel / 100.0;
            adjustedCaptureProbability = captureProbability * (1 - angerImpact);
        } else {
            adjustedCaptureProbability = 100;
        }

        // Verifico se la cattura ha successo o fallisce
        if (Math.random() * 100 < adjustedCaptureProbability) {
            eventNotifier.notifyObservers("CAPTURE_SUCCESS");
        } else {
            eventNotifier.notifyObservers("CAPTURE_FAIL");
            increaseAngerLevel(10);  // Aumento il livello di rabbia del Pokémon
            handlePokemonReaction(); // Gestisco la reazione del Pokémon
        }
    }

    // Gestisco l'effetto di un'esca sul Pokémon
    public void handleBait(int baitIndex) {
        captureProbability = Math.min(100, captureProbability + currentPokemon.getBaitEffect(baitIndex));
        handlePokemonReaction();
    }

    // Gestisco l'effetto di un profumo sul Pokémon
    public void handlePerfume(int perfumeIndex) {
        angerLevel = Math.max(0, angerLevel - currentPokemon.getPerfumeEffect(perfumeIndex));
        handlePokemonReaction();
    }

    // Gestisco l'effetto di una trappola sul Pokémon
    public void handleTrap(int trapIndex) {
        captureProbability = Math.min(100, captureProbability + currentPokemon.getTrapEffect(trapIndex));
        increaseAngerLevel(50);  // Aumento significativamente la rabbia del Pokémon
        handlePokemonReaction();
    }

    // Gestisco la reazione del Pokémon alla cattura (rabbia, fuga, ecc.)
    public void handlePokemonReaction() {
        increaseAngerLevel(5);  // Aumento la rabbia del Pokémon

        if (angerLevel >= 100) {
            endBattleWithMessage("POKEMON_ANGER");  // Il Pokémon è troppo arrabbiato e scappa
        } else if (Math.random() <= 0.10) {
            endBattleWithMessage("POKEMON_FLED");  // Il Pokémon fugge
        }
    }

    // Termino la battaglia con un determinato messaggio
    private void endBattleWithMessage(String message) {
        eventNotifier.notifyObservers(message);
    }

    // Ottengo la probabilità di cattura attuale
    public float getCaptureProbability() {
        return captureProbability;
    }

    // Ottengo il livello di rabbia del Pokémon
    public float getAngerLevel() {
        return angerLevel;
    }

    // Aumento il livello di rabbia del Pokémon
    private void increaseAngerLevel(int amount) {
        angerLevel = Math.min(100, angerLevel + amount);
    }

    // Aumento il livello di rabbia del Pokémon nel tempo
    public void increaseAngerOverTime() {
        angerLevel = Math.min(100, angerLevel + 5);
    }

    // Verifico se la cattura è stata interrotta
    public boolean isCaptureInterrupted() {
        return captureInterrupted;
    }
}
