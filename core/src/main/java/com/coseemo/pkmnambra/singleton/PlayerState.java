package com.coseemo.pkmnambra.singleton;

import com.coseemo.pkmnambra.actors.Player;

public class PlayerState {
    private Player player;
    private int safeX;
    private int safeY;

    public PlayerState(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSafeCoords(int x, int y){
        safeX = x;
        safeY = y;
    }

    public int getSafeX() {
        return safeX;
    }

    public int getSafeY() {
        return safeY;
    }
}

