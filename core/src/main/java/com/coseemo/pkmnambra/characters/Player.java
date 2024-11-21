package com.coseemo.pkmnambra.characters;

import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.maplogic.World;
import com.coseemo.pkmnambra.util.AnimationSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Actor {
    private Inventory inventory; // Aggiungi l'inventario al giocatore
    private List<String> team; // Lista dei Pokémon nella squadra
    private boolean hasInventory;
    private Map<String, Boolean> toCatch;
    private final int MAX_TEAM = 3;

    public Player(World place, int x, int y, AnimationSet animations) {
        super("Player", place, x, y, animations);
        this.team = new ArrayList<>();
        this.hasInventory = false;
    }

    public Player() {
        this.team = new ArrayList<>();
    }

    // Metodo per aggiungere un oggetto all'inventario
    public void addItem(Item item) {
        inventory.addItem(item);
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


    // Metodo per aggiungere un Pokémon alla squadra
    public boolean addPokemon(String name) {
        if (!isTeamFull()) {
            team.add(name);
            if (toCatch.containsKey(name)) {
                toCatch.put(name, true);
            }
            return true; // Pokémon aggiunto con successo
        } else {
            System.out.println("La squadra è piena! Non puoi catturare altri Pokémon.");
            return false; // Non è stato possibile aggiungere il Pokémon
        }
    }

    // Metodo per controllare se la squadra è piena
    public boolean isTeamFull() {
        return team.size() >= MAX_TEAM;
    }

    public void printTeam() {
        if (team.isEmpty()) {
            System.out.println("La tua squadra è vuota.");
        } else {
            System.out.println("I Pokémon nella tua squadra:");
            for (int i = 0; i < team.size(); i++) {
                String name = team.get(i);
                // Supponendo che la classe Pokemon abbia i metodi getName e getLevel
                System.out.println(i + 1 + " " + name);
            }
        }
    }

    protected void initInventory() {
        this.inventory = new Inventory(100);
        setHasInventory(true);
    }

    protected void setToCatch(Map<String, Boolean> toCatch) {
        this.toCatch = toCatch;
    }

    public Map<String, Boolean> getToCatch() {
        return toCatch;
    }

    protected void clearTeam(){
        for(String string : team){
            team.remove(string);
        }
    }

    public boolean isHasInventory() {
        return hasInventory;
    }

    public void setHasInventory(boolean hasInventory) {
        this.hasInventory = hasInventory;
    }

    public List<String> getTeam() {
        return team;
    }

}
