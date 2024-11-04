package com.coseemo.pkmnambra.items;

import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private Map<Item, Integer> items;
    private int maxSize;

    public Inventory(int maxSize) {
        this.items = new HashMap<>();
        this.maxSize = maxSize;
    }

    // Aggiungi un oggetto all'inventario
    public boolean addItem(Item item) {
        // Ottieni la quantità attuale dell'oggetto, o 0 se non è presente
        int currentCount = items.getOrDefault(item, 0);

        // Aggiungi o aggiorna la quantità dell'oggetto
        items.put(item, currentCount + 1);
        System.out.println(item.getName() + " è stato aggiunto all'inventario. Quantità attuale: " + (currentCount + 1));
        return true;
    }


    // Rimuovi un oggetto dall'inventario
    public boolean removeItem(Item item) {
        if (!items.containsKey(item)) {
            System.out.println(item.getName() + " non è presente nell'inventario.");
            return false;
        }

        int currentCount = items.get(item);
        if (currentCount > 1) {
            items.put(item, currentCount - 1);
            System.out.println(item.getName() + " è stato usato. Quantità rimanente: " + (currentCount - 1));
        } else {
            items.remove(item);
            System.out.println(item.getName() + " è stato rimosso dall'inventario.");
        }
        return true;
    }

    // Restituisce una lista di oggetti senza le quantità
    public List<Item> getItemList() {
        return new ArrayList<>(items.keySet());
    }

    // Restituisce solo gli oggetti con quantità
    public Map<Item, Integer> getItemsWithQuantity() {
        return new HashMap<>(items);
    }

    // Filtra oggetti per categoria
    public List<Item> getItemsByCategory(String category) {
        return items.keySet().stream()
            .filter(item -> item.getCategory().equals(category))
            .collect(Collectors.toList());
    }

    // Metodo di supporto per ottenere la quantità di un singolo oggetto
    public int getItemQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    // Metodo di supporto per verificare se un oggetto è presente
    public boolean containsItem(Item item) {
        return items.containsKey(item);
    }

    // Mostra gli oggetti con le quantità
    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("L'inventario è vuoto.");
        } else {
            System.out.println("Oggetti nell'inventario:");
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                System.out.println("- " + entry.getKey().getName() + ": " + entry.getValue());
            }
        }
    }

    // Controlla se l'inventario è pieno
    public boolean isFull() {
        return items.size() >= maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public Item getItemByName(String name) {
        for(Item item : this.getItemList()){
            if(Objects.equals(item.getName(), name)){
                return item;
            }
        }
        return null;
    }

    public boolean hasItemsInCategory(String name) {
        for(Item item : this.getItemList()){
            if (Objects.equals(name, item.getName())){
                return true;
            }
        }
        return false;
    }
}


