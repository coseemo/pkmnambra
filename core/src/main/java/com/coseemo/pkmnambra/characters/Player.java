package com.coseemo.pkmnambra.characters;

import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.maplogic.TileMap;
import com.coseemo.pkmnambra.util.AnimationSet;

public class Player extends Actor {
    private Inventory inventory; // Aggiungi l'inventario al giocatore

    public Player(TileMap map, int x, int y, AnimationSet animations) {
        super(map, x, y, animations);
        this.inventory = new Inventory(10); // Imposta una dimensione massima per l'inventario, ad esempio 10
    }

    // Metodo per aggiungere un oggetto all'inventario
    public boolean addItem(Item item) {
        return inventory.addItem(item);
    }

    // Metodo per rimuovere un oggetto dall'inventario
    public boolean removeItem(Item item) {
        return inventory.removeItem(item);
    }

    // Ottieni la lista degli oggetti nell'inventario
    public Inventory getInventory() {
        return inventory;
    }

    // Mostra gli oggetti nell'inventario
    public void showInventory() {
        inventory.showInventory();
    }

    // Controlla se l'inventario è pieno
    public boolean isInventoryFull() {
        return inventory.isFull();
    }
}
