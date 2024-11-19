package com.coseemo.pkmnambra.items;

import java.util.Objects;

// Classe astratta di base per tutti gli oggetti del gioco
public abstract class Item {
    private final String name;
    private final String description;
    private final String category;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(name, item.name); // Confronta solo il nome per considerare gli oggetti uguali
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Usa solo il nome per generare l'hash
    }
}
