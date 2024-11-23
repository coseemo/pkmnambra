package com.coseemo.pkmnambra.pokemonfactory;

import com.coseemo.pkmnambra.itemfactory.CaptureItem;
import com.coseemo.pkmnambra.itemfactory.CaptureItemFactory;


public abstract class Pokemon {

    private final String name;               // Nome del Pokémon
    private final float angerLevel;           // Livello di rabbia del Pokémon
    private final float baseCaptureRate;      // Cattura di base del Pokémon
    private final float baseAngerLevel;       // Rabbia di base del Pokémon

    // Costruttore della classe Pokémon
    public Pokemon(String name, float baseCaptureRate, float baseAngerLevel) {
        this.name = name;
        this.baseCaptureRate = baseCaptureRate;
        this.baseAngerLevel = baseAngerLevel;
        this.angerLevel = baseAngerLevel; // Inizializza il livello di rabbia alla rabbia di base
    }

    // Getters
    public String getName() {
        return name;
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

    // Ogni pokemon reagisce diversamente agli oggetti

    public abstract float getBaitEffect(int i);

    public abstract float getPerfumeEffect(int i);

    public abstract float getTrapEffect(int i);

    public abstract float getPokeballEffect(int i);
}
