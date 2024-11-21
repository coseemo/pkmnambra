package com.coseemo.pkmnambra.Saver;

import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.World;

public class GameData {
    private Player player;
    private World world;

    public GameData(Player player, World world) {
        this.player = player;
        this.world = world;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
