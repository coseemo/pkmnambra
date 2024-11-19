package com.coseemo.pkmnambra.events;

import com.coseemo.pkmnambra.pokemons.PokemonFactory;
import com.coseemo.pkmnambra.screen.CaptureScreen;
import com.coseemo.pkmnambra.util.states.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonEncounterEvent extends MapEvent {
    private final float encounterRate;
    private final List<String> possibleEncounters;
    private final Random random;
    private boolean isActive;
    private boolean isProcessing;

    public PokemonEncounterEvent(int x, int y, float encounterRate) {
        super(x, y, EVENT_TYPE.POKEMON_ENCOUNTER);
        this.encounterRate = encounterRate;
        this.possibleEncounters = new ArrayList<>();
        this.random = new Random();
        this.isActive = true;
        this.isProcessing = false;
    }

    @Override
    public void trigger(GameState gameState) {
        System.out.println("Attempting to trigger Pokémon encounter at (" + getX() + ", " + getY() + ")");
        System.out.println("Current state - isActive: " + isActive + ", isProcessing: " + isProcessing);

        if (!isActive || isProcessing) {
            System.out.println("Event not triggered - tile is inactive or being processed");
            return;
        }

        System.out.println("Triggering encounter. Checking probability...");
        if (random.nextFloat() < encounterRate && !possibleEncounters.isEmpty()) {
            isProcessing = true;
            isActive = false;
            System.out.println("Encounter successful - deactivating tile");

            // Estrazione del Pokémon con peso basato sulla posizione
            String pokemon = weightedRandomEncounter();
            System.out.println("Encountered Pokémon: " + pokemon);
            gameState.changeScreen(new CaptureScreen(gameState, PokemonFactory.createPokemon(pokemon)));
        } else {
            System.out.println("No Pokémon encountered - probability check failed");
        }
    }

    public void reactivateTile() {
        System.out.println("Reactivating tile at (" + getX() + ", " + getY() + ")");
        isActive = true;
        isProcessing = false;
        System.out.println("Tile reactivated - isActive: " + isActive + ", isProcessing: " + isProcessing);
    }

    public boolean isActive() {
        return isActive;
    }

    public void addPossibleEncounter(String pokemon) {
        possibleEncounters.add(pokemon);
    }

    /**
     * Seleziona un Pokémon casuale dalla lista con una probabilità maggiore per quelli in cima.
     */
    private String weightedRandomEncounter() {
        int totalWeight = 0;
        for (int i = 1; i <= possibleEncounters.size(); i++) {
            totalWeight += i; // Peso crescente in base alla posizione
        }

        int randomValue = random.nextInt(totalWeight) + 1;
        int cumulativeWeight = 0;

        for (int i = 0; i < possibleEncounters.size(); i++) {
            cumulativeWeight += (i + 1); // Peso della posizione
            if (randomValue <= cumulativeWeight) {
                return possibleEncounters.get(i);
            }
        }

        // Default, non dovrebbe mai succedere
        return possibleEncounters.get(possibleEncounters.size() - 1);
    }
}
