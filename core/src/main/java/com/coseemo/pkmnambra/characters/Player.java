package com.coseemo.pkmnambra.characters;

import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.maplogic.TileMap;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.util.AnimationSet;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {
    private Inventory inventory; // Aggiungi l'inventario al giocatore
    private List<Pokemon> team; // Lista dei Pokémon nella squadra
    private final int MAX_TEAM = 2;

    private boolean justCapt;

    public Player(TileMap map, int x, int y, AnimationSet animations) {
        super(map, x, y, animations);
        this.inventory = new Inventory(10); // Imposta una dimensione massima per l'inventario, ad esempio 10
        this.team = new ArrayList<>();
        this.justCapt = false;
    }

    public Player() {

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

    public List<Pokemon> getSquadra() {
        return team;
    }

    // Metodo per aggiungere un Pokémon alla squadra
    public boolean addPokemon(Pokemon pokemon) {
        if (!isTeamFull()) {
            team.add(pokemon);
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
                Pokemon pokemon = team.get(i);
                // Supponendo che la classe Pokemon abbia i metodi getName e getLevel
                System.out.println(i+1 + " " + pokemon.getName());
            }
        }
    }

    public boolean isJustCapt() {
        return justCapt;
    }

    public void setJustCapt(boolean justCapt) {
        this.justCapt = justCapt;
    }
}
