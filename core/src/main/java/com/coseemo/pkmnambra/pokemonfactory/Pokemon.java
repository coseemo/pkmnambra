package com.coseemo.pkmnambra.pokemonfactory;

import com.coseemo.pkmnambra.itemfactory.CaptureItem;
import com.coseemo.pkmnambra.itemfactory.CaptureItemFactory;


public class Pokemon {

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

    public float getBaitEffect(int i) {

        CaptureItem item;
        float effect = 0;

        switch (i) {
            case 0:
                item = CaptureItemFactory.createItem("STANDARDBAIT");
                break; // Aggiunto break
            case 1:
                item = CaptureItemFactory.createItem("SPICYBAIT");
                break; // Aggiunto break
            case 2:
                item = CaptureItemFactory.createItem("SWEETBAIT");
                break; // Aggiunto break
            case 3:
                item = CaptureItemFactory.createItem("SMELLYBAIT");
                break; // Aggiunto break
            default:
                item = CaptureItemFactory.createItem("STANDARDBAIT");
                break; // Aggiunto break
        }

        return effect; // Assicurati che getEffectValue() sia un metodo di CaptureItem
    }

    public float getPerfumeEffect(int i) {

        CaptureItem item;
        float effect = 0;

        switch (i) {
            case (0):
                item = CaptureItemFactory.createItem("FLORALPERFUME");
                effect = item.getEffectValue() * 2;
                break;
            case (1):
                item = CaptureItemFactory.createItem("FRUITYPERFUME");
                effect = item.getEffectValue() * 2;
                break;
            case (2):
                item = CaptureItemFactory.createItem("HERBALPERFUME");
                effect = item.getEffectValue() * 2;
                break;
            case (3):
                item = CaptureItemFactory.createItem("MYSTICPERFUME");
                effect = item.getEffectValue() * 2;
                break;
            default:
                item = CaptureItemFactory.createItem("FLORALPERFUME");
                effect = item.getEffectValue() * 2;
                break;

        }
        return effect;
    }

    public float getTrapEffect(int i) {

        CaptureItem item;
        float effect = 0f;

        switch (i) {
            case (0):
                item = CaptureItemFactory.createItem("BASICTRAP");
                effect = item.getEffectValue() * 2;
                break;
            case (1):
                item = CaptureItemFactory.createItem("ADVANCEDTRAP");
                effect = item.getEffectValue() * 2;
                break;
            case (2):
                item = CaptureItemFactory.createItem("TRICKYTRAP");
                effect = item.getEffectValue() * 2;
                break;
            case (3):
                item = CaptureItemFactory.createItem("QUICKTRAP");
                effect = item.getEffectValue() * 2;
                break;
            default:
                item = CaptureItemFactory.createItem("BASICTRAP");
                effect = item.getEffectValue() * 2;
                break;
        }
        return effect;
    }

    public float getPokeballEffect(int i) {
        CaptureItem item;
        float effect = 0f;

        switch (i) {
            case (0):
                item = CaptureItemFactory.createItem("POKEBALL");
                effect = item.getEffectValue() * 2;
                break;
            case (1):
                item = CaptureItemFactory.createItem("GREATBALL");
                effect = item.getEffectValue() * 2;
                break;
            case (2):
                item = CaptureItemFactory.createItem("ULTRABALL");
                effect = item.getEffectValue() * 2;
                break;
            case (3):
                effect = 100;
                break;
            default:
                item = CaptureItemFactory.createItem("POKEBALL");
                effect = item.getEffectValue() * 2;
                break;
        }
        return effect;
    }
}
