package com.coseemo.pkmnambra.pokemons;

public class Pokemon {

    private String name;               // Nome del Pokémon
    private int level;                 // Livello del Pokémon
    private float captureDifficulty;    // Difficoltà di cattura del Pokémon
    private float angerLevel;           // Livello di rabbia del Pokémon
    private float baseCaptureRate;      // Cattura di base del Pokémon
    private float baseAngerLevel;       // Rabbia di base del Pokémon

    // Costruttore della classe Pokémon
    public Pokemon(String name, int level, float captureDifficulty, float baseCaptureRate, float baseAngerLevel) {
        this.name = name;
        this.level = level;
        this.captureDifficulty = captureDifficulty;
        this.baseCaptureRate = baseCaptureRate;
        this.baseAngerLevel = baseAngerLevel;
        this.angerLevel = baseAngerLevel; // Inizializza il livello di rabbia alla rabbia di base
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public float getCaptureDifficulty() {
        return captureDifficulty;
    }

    public float getAngerLevel() {
        return angerLevel;
    }

    public float getBaseCaptureRate() {
        return baseCaptureRate;
    }

    public float getBaseAngerLevel() {
        return baseAngerLevel;
    }

    // Metodi per aumentare e diminuire la rabbia
    public void increaseAnger(float amount) {
        angerLevel = Math.min(angerLevel + amount, 100.0f); // Rabbia massima a 100
    }

    public void decreaseAnger(float amount) {
        angerLevel = Math.max(angerLevel - amount, 0.0f); // Rabbia minima a 0
    }

    // Reazione del Pokémon alle esche
    public void reactToBait() {
        increaseAnger(5.0f); // Esempio di aumento della rabbia se viene utilizzata un'esca
        System.out.println(name + " reacts to the bait, anger increases to " + angerLevel);
    }

    // Reazione del Pokémon ai profumi
    public void reactToPerfume() {
        decreaseAnger(10.0f); // Esempio di diminuzione della rabbia se viene utilizzato un profumo
        System.out.println(name + " reacts to the perfume, anger decreases to " + angerLevel);
    }

    public float getBaitEffect() {
        return 10.0f;
    }

    public float getPerfumeEffect() {
        return 10.0f;
    }
}
