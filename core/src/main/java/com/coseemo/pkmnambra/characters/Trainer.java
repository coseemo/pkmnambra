package com.coseemo.pkmnambra.characters;

import java.util.List;

import com.coseemo.pkmnambra.pokemons.Pokemon;

public class Trainer {
    private String name;
    private List<Pokemon> team;
    private int currentPokemonIndex;

    public Trainer(String name, List<Pokemon> team) {
        this.name = name;
        this.team = team;
        this.currentPokemonIndex = 0;
    }

    public String getName() {
        return name;
    }

    public Pokemon getCurrentPokemon() {
        if (currentPokemonIndex < team.size()) {
            return team.get(currentPokemonIndex);
        }
        return null;
    }

    public void switchToNextPokemon() {
        currentPokemonIndex++;
    }

    public boolean hasRemainingPokemon() {
        return currentPokemonIndex < team.size();
    }
}
