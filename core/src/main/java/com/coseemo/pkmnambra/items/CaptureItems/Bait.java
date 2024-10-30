package com.coseemo.pkmnambra.items.CaptureItems;

import com.coseemo.pkmnambra.items.CaptureItem;

public class Bait extends CaptureItem {

    public Bait(String name, String description,
                    String capturePhrase, int effectValue) {
        super(name, description, "Bait", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + "" + getCapturePhrase());
        // Logica di cattura con il valore effectValue
    }
}
