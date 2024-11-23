package com.coseemo.pkmnambra.actors;

import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.inventory.Inventory;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.util.AnimationSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Actor {

    private Inventory inventory; // L'inventario del giocatore
    private List<String> team; // La squadra del giocatore, contenente i Pokémon
    private boolean hasInventory; // Indica se il giocatore ha un inventario
    private Map<String, Boolean> toCatch; // La lista di Pokémon da catturare
    private final int MAX_TEAM = 3; // La dimensione massima della squadra di Pokémon

    // Costruttore che inizializza il giocatore con una posizione, un mondo e set di animazioni
    public Player(World place, int x, int y, AnimationSet animations) {
        super("Player", place, x, y, animations);
        this.toCatch = new HashMap<>();
        this.team = new ArrayList<>();
        this.inventory = new Inventory();
        this.hasInventory = false;
    }

    // Costruttore senza parametri, per uso generico
    public Player() {
        this.team = new ArrayList<>();
    }

    // Aggiungo un Pokémon alla squadra, ritorna true se aggiunto con successo
    public boolean addPokemon(String name) {
        if (!isTeamFull()) {
            team.add(name);
            if (toCatch.containsKey(name)) {
                toCatch.replace(name, false); // Segno che il Pokémon è stato catturato
            }
            return true;
        }
        return false; // La squadra è piena
    }

    // Verifico se la squadra ha raggiunto la sua capacità massima
    public boolean isTeamFull() {
        return team.size() >= MAX_TEAM;
    }

    // Ottengo la lista dei Pokémon nella squadra
    public List<String> getTeam() {
        return team;
    }

    // Aggiungo un oggetto all'inventario
    public void addItem(String item) {
        inventory.addItem(item);
    }

    // Rimuovo un oggetto dall'inventario, ritorna true se successo
    public boolean removeItem(String item) {
        return inventory.removeItem(item);
    }

    // Restituisco l'inventario del giocatore
    public Inventory getInventory() {
        return inventory;
    }

    // Mostro gli oggetti presenti nell'inventario
    public void showInventory() {
        inventory.showInventory();
    }

    // Imposto la mappa di Pokémon da catturare
    public void setToCatch(Map<String, Boolean> toCatch) {
        this.toCatch = toCatch;
    }

    // Ottengo la mappa dei Pokémon da catturare
    public Map<String, Boolean> getToCatch() {
        return toCatch;
    }

    // Inizializzo l'inventario per il giocatore
    protected void initInventory() {
        setHasInventory(true);
    }

    // Reset della squadra (svuotandola)
    protected void clearTeam() {
        team = new ArrayList<>();
    }

    // Verifico se il giocatore ha un inventario
    public boolean hasInventory() {
        return hasInventory;
    }

    // Imposto se il giocatore ha un inventario
    public void setHasInventory(boolean hasInventory) {
        this.hasInventory = hasInventory;
    }
}
