package com.coseemo.pkmnambra.items.CaptureItems;

public class Bait extends CaptureItem {

    public Bait(String name, String description,
                String capturePhrase, int effectValue) {
        super(name, description, "BAIT", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + getCapturePhrase());
        // Logica di cattura con il valore effectValue
    }
}
