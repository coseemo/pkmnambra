package com.coseemo.pkmnambra.events;

import com.coseemo.pkmnambra.util.GameState;

public abstract class MapEvent {
    protected int x;
    protected int y;
    protected EVENT_TYPE type;

    public MapEvent(int x, int y, EVENT_TYPE type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public EVENT_TYPE getType() { return type; }

    public abstract void trigger(GameState gameState);
}

