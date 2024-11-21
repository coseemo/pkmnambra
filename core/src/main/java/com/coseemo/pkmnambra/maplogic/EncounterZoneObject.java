package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.characters.Actor;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.pokemons.PokemonFactory;
import com.coseemo.pkmnambra.screen.CaptureScreen;
import com.coseemo.pkmnambra.util.states.GameState;
import com.coseemo.pkmnambra.util.states.ScreenManager;

public class EncounterZoneObject extends WorldObject {
    private final EncounterType encounterType;
    private float stepCounter = 0;
    private static final float STEPS_PER_CHECK = 4;

    public EncounterZoneObject(int x, int y,
                               EncounterType encounterType,
                               TextureRegion texture, GridPoint2[] tiles) {
        super(x, y, true, texture, 1, 1, tiles);
        this.encounterType = encounterType;
    }

    @Override
    public void onActorStep(Actor actor) {
        if (!(actor instanceof Player)) return;

        stepCounter++;
        if (stepCounter >= STEPS_PER_CHECK) {
            stepCounter = 0;
            checkForEncounter((Player) actor);
        }
    }

    private void checkForEncounter(Player player) {
        float encounterRoll = MathUtils.random();

        if (encounterRoll < encounterType.getEncounterRate()) {
            String selectedPokemon = selectRandomPokemon();
            triggerEncounter(player, selectedPokemon);
        }
    }

    private String selectRandomPokemon() {
        float totalWeight = 0;
        for (float weight : encounterType.getSpawnRates()) {
            totalWeight += weight;
        }

        float roll = MathUtils.random() * totalWeight;
        float currentWeight = 0;

        for (int i = 0; i < encounterType.getSpawnRates().length; i++) {
            currentWeight += encounterType.getSpawnRates()[i];
            if (roll < currentWeight) {
                return encounterType.getPossiblePokemons()[i];
            }
        }

        return encounterType.getPossiblePokemons()[0];
    }

    private void triggerEncounter(Player player, String pokemonId) {
        Pokemon wildPokemon = PokemonFactory.createPokemon(pokemonId);
        ScreenManager screenManager = GameState.getInstance().getScreenManager();
        screenManager.changeScreen(new CaptureScreen(GameState.getInstance(), wildPokemon));
    }
}
