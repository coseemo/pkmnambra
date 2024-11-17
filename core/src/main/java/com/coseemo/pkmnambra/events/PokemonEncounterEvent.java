package com.coseemo.pkmnambra.events;

import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.screen.CaptureScreen;
import com.coseemo.pkmnambra.util.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonEncounterEvent extends MapEvent {
    private float encounterRate;
    private List<Pokemon> possibleEncounters;
    private Random random;
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

            Pokemon pokemon = possibleEncounters.get(random.nextInt(possibleEncounters.size()));
            System.out.println("Encountered Pokémon: " + pokemon.getName());
            gameState.getGame().setScreen(new CaptureScreen(gameState, pokemon));
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

    public void addPossibleEncounter(Pokemon pokemon) {
        possibleEncounters.add(pokemon);
    }
}
