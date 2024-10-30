package com.coseemo.pkmnambra.items.CaptureItems;

import com.coseemo.pkmnambra.items.CaptureItem;

public class Pokeball extends CaptureItem {

    public Pokeball(String name, String description,
                    String capturePhrase, int effectValue) {
       super(name, description, "Ball", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + "" + getCapturePhrase());
        // Logica di cattura con il valore effectValue
    }
}
