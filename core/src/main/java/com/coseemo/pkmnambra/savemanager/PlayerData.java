package com.coseemo.pkmnambra.savemanager;

import com.coseemo.pkmnambra.actors.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {
    public int x, y;
    public Map<String, Integer> inventory; // Gli oggetti e le loro quantità
    public List<String> team; // Il team di creature del giocatore
    public Map<String, Boolean> toCatch; // Gli obiettivi da catturare
    public boolean hasInventory; // Indica se il giocatore ha l'inventario

    public static PlayerData fromPlayer(Player player) {
        PlayerData data = new PlayerData();
        data.x = player.getX();
        data.y = player.getY();
        data.inventory = player.getInventory().getInventorySummary();
        data.team = new ArrayList<>(player.getTeam());
        data.toCatch = new HashMap<>(player.getToCatch());
        data.hasInventory = player.hasInventory();

        return data;
    }
}
