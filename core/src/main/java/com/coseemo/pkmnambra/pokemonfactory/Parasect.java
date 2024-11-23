package com.coseemo.pkmnambra.pokemonfactory;

import com.coseemo.pkmnambra.itemfactory.CaptureItem;
import com.coseemo.pkmnambra.itemfactory.CaptureItemFactory;

public class Parasect extends Pokemon {

    public Parasect() {
        super("Parasect", 14.0f, 17.0f); // Valori di base su scala 10-20
    }

    @Override
    public float getBaitEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "STANDARDBAIT" : i == 1 ? "SPICYBAIT" : i == 2 ? "SWEETBAIT" : "SMELLYBAIT");
        return item.getEffectValue() * 1.3f;
    }

    @Override
    public float getPerfumeEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "FLORALPERFUME" : i == 1 ? "FRUITYPERFUME" : i == 2 ? "HERBALPERFUME" : "MYSTICPERFUME");
        return item.getEffectValue() * 1.0f;
    }

    @Override
    public float getTrapEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "BASICTRAP" : i == 1 ? "ADVANCEDTRAP" : i == 2 ? "TRICKYTRAP" : "QUICKTRAP");
        return item.getEffectValue() * 1.1f;
    }

    @Override
    public float getPokeballEffect(int i) {
        CaptureItem item = CaptureItemFactory.createItem(i == 0 ? "POKEBALL" : i == 1 ? "GREATBALL" : i == 2 ? "ULTRABALL" : "MASTERBALL");
        return item.getEffectValue() * 1.4f;
    }
}
