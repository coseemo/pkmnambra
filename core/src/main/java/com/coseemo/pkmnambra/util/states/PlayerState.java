package com.coseemo.pkmnambra.util.states;

import com.coseemo.pkmnambra.characters.Player;

public class PlayerState {
    private Player player;

    public PlayerState(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

