package com.coseemo.pkmnambra.items;

import com.coseemo.pkmnambra.items.CaptureItems.CaptureItemFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private final Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    // Aggiungi un oggetto all'inventario usando una stringa
    public void addItem(String itemName) {
        try {
            // Usa la fabbrica per creare l'oggetto
            Item item = CaptureItemFactory.createItem(itemName);

            // Ottieni la quantità attuale dell'oggetto, o 0 se non è presente
            int currentCount = items.getOrDefault(item, 0);

            // Aggiungi o aggiorna la quantità dell'oggetto
            items.put(item, currentCount + 1);
            System.out.println(item.getName() + " è stato aggiunto all'inventario. Quantità attuale: " + (currentCount + 1));
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: Tipo di oggetto sconosciuto - " + itemName);
        }
    }

    public boolean removeItem(String itemName) {
        try {
            // Usa la fabbrica per trovare l'oggetto corrispondente
            Item item = CaptureItemFactory.createItem(itemName);

            if (!items.containsKey(item)) {
                System.out.println(itemName + " non è presente nell'inventario.");
                return false;
            }

            int currentCount = items.get(item);
            if (currentCount > 1) {
                // Decrementa la quantità
                items.put(item, currentCount - 1);
                System.out.println(item.getName() + " è stato usato. Quantità rimanente: " + (currentCount - 1));
            } else {
                // Rimuovi l'oggetto
                items.remove(item);
                System.out.println(item.getName() + " è stato completamente rimosso dall'inventario.");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: Tipo di oggetto sconosciuto - " + itemName);
            return false;
        }
    }

    // Restituisce una mappa col nome dello strumento e la quantità
    public Map<String, Integer> getInventorySummary() {
        return items.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getName(),
                Map.Entry::getValue
            ));
    }

    // Carica l'inventario da una mappa nome-quantità
    public void loadItems(Map<String, Integer> inventory) {
        items.clear(); // Svuota l'inventario attuale
        inventory.forEach((itemName, quantity) -> {
            try {
                // Usa la fabbrica per creare l'oggetto
                Item item = CaptureItemFactory.createItem(itemName);

                // Aggiungi l'oggetto con la quantità specificata
                items.put(item, quantity);
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: Tipo di oggetto sconosciuto durante il caricamento - " + itemName);
            }
        });
    }

    // Mostra gli oggetti con le quantità
    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("L'inventario è vuoto.");
        } else {
            System.out.println("Oggetti nell'inventario:");
            items.forEach((item, quantity) ->
                System.out.println("- " + item.getName() + ": " + quantity)
            );
        }
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public List<Item> getItemsByCategory(String category) {
        return items.keySet().stream() // Ottieni tutti gli oggetti nell'inventario
            .filter(item -> item.getCategory().equalsIgnoreCase(category)) // Filtra per categoria
            .collect(Collectors.toList()); // Raccogli il risultato in una lista
    }

}
