package com.coseemo.pkmnambra.maplogic;

public class EncounterType {
    private final String[] possiblePokemons;
    private final float[] spawnRates;
    private final float encounterRate;

    public EncounterType(String[] pokemons, float[] rates, float encounterRate) {
        this.possiblePokemons = pokemons;
        this.spawnRates = rates;
        this.encounterRate = encounterRate;
    }

    public static class EncounterTypes {
        public static final EncounterType BEACH = new EncounterType(
            new String[]{"bellossom", "exeggutor", "slakoth", "parasect"},
            new float[]{0.4f, 0.3f, 0.2f, 0.1f},
            0.5f
        );

    }

    public String[] getPossiblePokemons() {
        return possiblePokemons;
    }

    public float[] getSpawnRates() {
        return spawnRates;
    }

    public float getEncounterRate() {
        return encounterRate;
    }
}
