package com.coseemo.pkmnambra.Saver;

import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {
    public int x, y;
    public Map<String, Integer> inventory; // Oggetti e quantità
    public List<String> team;
    public Map<String, Boolean> toCatch;
    public boolean hasInventory;

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
