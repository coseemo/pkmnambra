package com.coseemo.pkmnambra.itemfactory;

public class Bait extends CaptureItem {

    public Bait(String name, String description,
                String capturePhrase, int effectValue) {
        super(name, description, "BAIT", capturePhrase, effectValue);
    }


    @Override
    public void use() {
        System.out.println("Using " + getName() + getCapturePhrase());
    }
}
