package com.coseemo.pkmnambra.itemfactory;

public class Perfume extends CaptureItem {

    public Perfume(String name, String description,
                   String capturePhrase, int effectValue) {
        super(name, description, "PERFUME", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + getCapturePhrase());
        // Logica di cattura con il valore effectValue
    }
}
