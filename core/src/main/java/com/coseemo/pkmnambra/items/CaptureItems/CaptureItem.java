package com.coseemo.pkmnambra.items.CaptureItems;

import com.coseemo.pkmnambra.items.Item;

public abstract class CaptureItem extends Item {
    private final String capturePhrase;
    private final int effectValue;

    public CaptureItem(String name, String description, String category,
                       String capturePhrase, int effectValue) {
        super(name, description, category);
        this.capturePhrase = capturePhrase;
        this.effectValue = effectValue;
    }

    public String getCapturePhrase() {
        return capturePhrase;
    }

    public int getEffectValue() {
        return effectValue;
    }

    @Override
    public abstract void use();
}
