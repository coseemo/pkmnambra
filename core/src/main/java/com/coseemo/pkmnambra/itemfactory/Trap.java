package com.coseemo.pkmnambra.itemfactory;

public class Trap extends CaptureItem {

    public Trap(String name, String description,
                String capturePhrase, int effectValue) {
        super(name, description, "TRAP", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + getCapturePhrase());
        // Logica di cattura con il valore effectValue
    }
}
