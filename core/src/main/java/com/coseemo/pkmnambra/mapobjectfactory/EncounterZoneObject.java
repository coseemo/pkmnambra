package com.coseemo.pkmnambra.mapobjectfactory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.actors.Actor;
import com.coseemo.pkmnambra.maplogic.EncounterType;
import com.coseemo.pkmnambra.mapobjectfactory.WorldObject;
import com.coseemo.pkmnambra.pokemonfactory.Pokemon;
import com.coseemo.pkmnambra.pokemonfactory.PokemonFactory;
import com.coseemo.pkmnambra.screen.CaptureScreen;
import com.coseemo.pkmnambra.singleton.GameState;

public class EncounterZoneObject extends WorldObject {
    private final EncounterType encounterType;

    private int safeX;
    private int safeY;
    private float stepCounter = 0;
    private static final float STEPS_PER_CHECK = 4;

    public EncounterZoneObject(int x, int y,
                               EncounterType encounterType,
                               TextureRegion texture, GridPoint2[] tiles) {
        super(x, y, true, texture, 0.65f, 0.65f, tiles);
        this.encounterType = encounterType;
        this.safeX = x;
        this.safeY = y;
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
        GameState.getInstance().changeScreen(new CaptureScreen(GameState.getInstance(), wildPokemon));
    }

    public EncounterType getEncounterType() {
        return encounterType;
    }
}
