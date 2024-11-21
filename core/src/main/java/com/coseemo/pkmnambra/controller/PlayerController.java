package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.coseemo.pkmnambra.characters.Actor;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.maplogic.DIRECTION;

public class PlayerController extends InputAdapter {
    private final Player player;
    private final boolean[] buttonPress;
    private final float[] pressTimer;
    private final float WALK_REFACE_TRESHOLD = 0.15f;

    public PlayerController(Player player) {
        this.player = player;

        buttonPress = new boolean[DIRECTION.values().length];
        buttonPress[DIRECTION.NORTH.ordinal()] = false;
        buttonPress[DIRECTION.SOUTH.ordinal()] = false;
        buttonPress[DIRECTION.EAST.ordinal()] = false;
        buttonPress[DIRECTION.WEST.ordinal()] = false;

        pressTimer = new float[DIRECTION.values().length];
        pressTimer[DIRECTION.NORTH.ordinal()] = 0f;
        pressTimer[DIRECTION.SOUTH.ordinal()] = 0f;
        pressTimer[DIRECTION.EAST.ordinal()] = 0f;
        pressTimer[DIRECTION.WEST.ordinal()] = 0f;

        resetButtonsAndTimers();
    }
    @Override
    public boolean keyDown(int keycode){
        if(keycode == Keys.UP)
            buttonPress[DIRECTION.NORTH.ordinal()] = true;
        if(keycode == Keys.DOWN)
            buttonPress[DIRECTION.SOUTH.ordinal()] = true;
        if(keycode == Keys.LEFT)
            buttonPress[DIRECTION.WEST.ordinal()] = true;
        if(keycode == Keys.RIGHT)
            buttonPress[DIRECTION.EAST.ordinal()] = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode){
        if(keycode == Keys.UP)
            releaseDirection(DIRECTION.NORTH);
        if(keycode == Keys.DOWN)
            releaseDirection(DIRECTION.SOUTH);
        if(keycode == Keys.LEFT)
            releaseDirection(DIRECTION.WEST);
        if(keycode == Keys.RIGHT)
            releaseDirection(DIRECTION.EAST);
        return false;
    }

    private void resetButtonsAndTimers() {
        for (DIRECTION dir : DIRECTION.values()) {
            buttonPress[dir.ordinal()] = false;
            pressTimer[dir.ordinal()] = 0f;
        }
    }

    public void update(float delta){
        for (DIRECTION dir : DIRECTION.values()) {
            if (buttonPress[dir.ordinal()]) {
                updateDirection(dir, delta);
            }
        }

    }

    private void updateDirection(DIRECTION dir, float delta){
        pressTimer[dir.ordinal()] += delta;
        considerMove(dir);
    }

    private void releaseDirection(DIRECTION dir){
        buttonPress[dir.ordinal()] = false;
        considerReface(dir);
        pressTimer[dir.ordinal()] = 0f;
    }

    private void considerMove(DIRECTION dir){
        if(pressTimer[dir.ordinal()] > WALK_REFACE_TRESHOLD){
            player.move(dir);
        }
    }
    private void considerReface(DIRECTION dir) {
        if (pressTimer[dir.ordinal()] < WALK_REFACE_TRESHOLD) {
            player.reface(dir);
        }
    }

}
