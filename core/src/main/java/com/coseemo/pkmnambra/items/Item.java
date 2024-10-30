package com.coseemo.pkmnambra.items;

// Classe astratta di base per tutti gli oggetti del gioco
public abstract class Item {
    private String name;
    private String description;
    private String category;

    public Item(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public abstract void use();
}
