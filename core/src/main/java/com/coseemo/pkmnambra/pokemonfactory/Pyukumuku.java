package com.coseemo.pkmnambra.pokemonfactory;

import com.coseemo.pkmnambra.itemfactory.CaptureItem;
import com.coseemo.pkmnambra.itemfactory.CaptureItemFactory;

public class Pyukumuku extends Pokemon {

    public Pyukumuku() {
        super("Pyukumuku", 10.0f, 20.0f); // Valori di base per Pyukumuku
    }

    @Override
    public float getBaitEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "STANDARDBAIT" : i == 1 ? "SPICYBAIT" : i == 2 ? "SWEETBAIT" : "SMELLYBAIT");
        return item.getEffectValue() * 1.0f; // Effetto del cibo standard
    }

    @Override
    public float getPerfumeEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "FLORALPERFUME" : i == 1 ? "FRUITYPERFUME" : i == 2 ? "HERBALPERFUME" : "MYSTICPERFUME");
        return item.getEffectValue() * 1.2f; // Effetto dei profumi aumentato
    }

    @Override
    public float getTrapEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "BASICTRAP" : i == 1 ? "ADVANCEDTRAP" : i == 2 ? "TRICKYTRAP" : "QUICKTRAP");
        return item.getEffectValue() * 1.5f; // Effetto delle trappole aumentato
    }

    @Override
    public float getPokeballEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "POKEBALL" : i == 1 ? "GREATBALL" : i == 2 ? "ULTRABALL" : "MASTERBALL");
        return item.getEffectValue() * 1.1f; // Effetto delle Pokéball aumentato
    }
}
