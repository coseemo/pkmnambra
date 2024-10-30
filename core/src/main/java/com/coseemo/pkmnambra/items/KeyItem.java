package com.coseemo.pkmnambra.items;

public class KeyItem extends Item {

    public KeyItem(String name, String description) {
        super(name, description, "KeyItem");
    }

    @Override
    public void use() {
        System.out.println("Using key item: " + getName());
        // Logica specifica per l'uso dei KeyItem
    }
}
