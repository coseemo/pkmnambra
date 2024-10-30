package com.coseemo.pkmnambra.items;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;
    private int maxSize;

    public Inventory(int maxSize) {
        this.items = new ArrayList<>();
        this.maxSize = maxSize;
    }

    // Aggiungi un oggetto all'inventario
    public boolean addItem(Item item) {
        if (items.size() < maxSize) {
            items.add(item);
            System.out.println(item.getName() + " è stato aggiunto all'inventario.");
            return true;
        } else {
            System.out.println("L'inventario è pieno. Non puoi aggiungere " + item.getName() + ".");
            return false;
        }
    }

    // Rimuovi un oggetto dall'inventario
    public boolean removeItem(Item item) {
        if (items.remove(item)) {
            System.out.println(item.getName() + " è stato rimosso dall'inventario.");
            return true;
        } else {
            System.out.println(item.getName() + " non è presente nell'inventario.");
            return false;
        }
    }

    // Ottieni una lista di tutti gli oggetti nell'inventario
    public List<Item> getItems() {
        return new ArrayList<>(items); // Restituisce una copia della lista degli oggetti
    }

    // Mostra gli oggetti nell'inventario
    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("L'inventario è vuoto.");
        } else {
            System.out.println("Oggetti nell'inventario:");
            for (Item item : items) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }

    // Controlla se l'inventario è pieno
    public boolean isFull() {
        return items.size() >= maxSize;
    }

    // Restituisce il numero massimo di oggetti
    public int getMaxSize() {
        return maxSize;
    }
}
