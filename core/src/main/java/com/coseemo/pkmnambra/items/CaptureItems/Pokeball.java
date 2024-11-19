package com.coseemo.pkmnambra.items.CaptureItems;

public class Pokeball extends CaptureItem {

    public Pokeball(String name, String description,
                    String capturePhrase, int effectValue) {
        super(name, description, "POKEBALL", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + getCapturePhrase());
        // Logica di cattura con il valore effectValue
    }
}
