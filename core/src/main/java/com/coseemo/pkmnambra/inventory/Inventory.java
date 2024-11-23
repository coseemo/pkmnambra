package com.coseemo.pkmnambra.inventory;

import com.coseemo.pkmnambra.itemfactory.CaptureItemFactory;
import com.coseemo.pkmnambra.itemfactory.Item;

import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private final Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    // Aggiungo un oggetto all'inventario usando una stringa
    public void addItem(String itemName) {
        try {
            // Creo l'oggetto tramite la fabbrica
            Item item = CaptureItemFactory.createItem(itemName);

            // Recupero la quantità attuale dell'oggetto, o 0 se non presente
            int currentCount = items.getOrDefault(item, 0);

            // Aggiungo o aggiorno la quantità dell'oggetto
            items.put(item, currentCount + 1);
        } catch (IllegalArgumentException e) {
            // Gestisco l'errore nel caso di un oggetto sconosciuto
        }
    }

    // Rimuovo un oggetto dall'inventario
    public boolean removeItem(String itemName) {
        try {
            // Trovo l'oggetto tramite la fabbrica
            Item item = CaptureItemFactory.createItem(itemName);

            if (!items.containsKey(item)) {
                return false;
            }

            int currentCount = items.get(item);
            if (currentCount > 1) {
                // Decremento la quantità
                items.put(item, currentCount - 1);
            } else {
                // Rimuovo l'oggetto
                items.remove(item);
            }
            return true;
        } catch (IllegalArgumentException e) {
            // Gestisco l'errore nel caso di un oggetto sconosciuto
            return false;
        }
    }

    // Restituisco una mappa con il nome dello strumento e la sua quantità
    public Map<String, Integer> getInventorySummary() {
        return items.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getName(),
                Map.Entry::getValue
            ));
    }

    // Carico gli oggetti nell'inventario da una mappa nome-quantità
    public void loadItems(Map<String, Integer> inventory) {
        items.clear(); // Svuoto l'inventario attuale
        inventory.forEach((itemName, quantity) -> {
            try {
                // Creo l'oggetto tramite la fabbrica
                Item item = CaptureItemFactory.createItem(itemName);

                // Aggiungo l'oggetto con la quantità specificata
                items.put(item, quantity);
            } catch (IllegalArgumentException e) {
                // Gestisco l'errore nel caso di un oggetto sconosciuto
            }
        });
    }

    // Mostro gli oggetti con le loro quantità
    public void showInventory() {
        if (items.isEmpty()) {
            // Se l'inventario è vuoto, lo indico
        } else {
            items.forEach((item, quantity) -> {
                // Mostro ogni oggetto con la sua quantità
            });
        }
    }

    // Restituisco la mappa degli oggetti
    public Map<Item, Integer> getItems() {
        return items;
    }

    // Restituisco gli oggetti per categoria
    public List<Item> getItemsByCategory(String category) {
        return items.keySet().stream()
            .filter(item -> item.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }
}
