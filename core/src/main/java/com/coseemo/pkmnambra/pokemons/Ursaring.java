package com.coseemo.pkmnambra.pokemons;

import com.coseemo.pkmnambra.items.CaptureItems.CaptureItem;
import com.coseemo.pkmnambra.items.CaptureItems.CaptureItemFactory;

public class Ursaring extends Pokemon {

    public Ursaring() {
        super("Ursaring", 16.0f, 20.0f); // Valori di base più alti
    }

    @Override
    public float getBaitEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "STANDARDBAIT" : i == 1 ? "SPICYBAIT" : i == 2 ? "SWEETBAIT" : "SMELLYBAIT");
        return item.getEffectValue() * 1.5f;
    }

    @Override
    public float getPerfumeEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "FLORALPERFUME" : i == 1 ? "FRUITYPERFUME" : i == 2 ? "HERBALPERFUME" : "MYSTICPERFUME");
        return item.getEffectValue() * 0.9f;
    }

    @Override
    public float getTrapEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "BASICTRAP" : i == 1 ? "ADVANCEDTRAP" : i == 2 ? "TRICKYTRAP" : "QUICKTRAP");
        return item.getEffectValue() * 1.4f;
    }

    @Override
    public float getPokeballEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "POKEBALL" : i == 1 ? "GREATBALL" : i == 2 ? "ULTRABALL" : "MASTERBALL");
        return item.getEffectValue() * 1.3f;
    }
}
