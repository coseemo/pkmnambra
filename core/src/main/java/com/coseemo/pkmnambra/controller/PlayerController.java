package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.maplogic.DIRECTION;

public class PlayerController extends InputAdapter {
    private final Player player;
    private final boolean[] buttonPress;
    private final float[] pressTimer;
    private final float WALK_REFACE_TRESHOLD = 0.15f;

    public PlayerController(Player player) {
        this.player = player;

        // Inizializzo lo stato dei tasti e dei timer
        buttonPress = new boolean[DIRECTION.values().length];
        pressTimer = new float[DIRECTION.values().length];
        resetButtonsAndTimers();
    }

    @Override
    public boolean keyDown(int keycode) {
        // Se il giocatore è fermo, resetto gli stati
        if (player.getState() == Actor.ACTOR_STATE.STILL) {
            resetButtonsAndTimers();
            return true;
        }

        // Registro la pressione dei tasti direzionali
        if (keycode == Keys.UP)
            buttonPress[DIRECTION.NORTH.ordinal()] = true;
        if (keycode == Keys.DOWN)
            buttonPress[DIRECTION.SOUTH.ordinal()] = true;
        if (keycode == Keys.LEFT)
            buttonPress[DIRECTION.WEST.ordinal()] = true;
        if (keycode == Keys.RIGHT)
            buttonPress[DIRECTION.EAST.ordinal()] = true;

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Quando il tasto viene rilasciato, registro il rilascio della direzione
        if (keycode == Keys.UP)
            releaseDirection(DIRECTION.NORTH);
        if (keycode == Keys.DOWN)
            releaseDirection(DIRECTION.SOUTH);
        if (keycode == Keys.LEFT)
            releaseDirection(DIRECTION.WEST);
        if (keycode == Keys.RIGHT)
            releaseDirection(DIRECTION.EAST);

        return false;
    }

    public void resetButtonsAndTimers() {
        // Resetto tutti i tasti e i timer
        for (DIRECTION dir : DIRECTION.values()) {
            buttonPress[dir.ordinal()] = false;
            pressTimer[dir.ordinal()] = 0f;
        }
    }

    public void update(float delta) {
        // Se il giocatore è fermo, non aggiorno i movimenti
        if (player.getState() == Actor.ACTOR_STATE.STILL) {
            resetButtonsAndTimers(); // Resetta ogni input attivo
            return;
        }

        // Aggiorno ogni direzione in base ai tasti premuti
        for (DIRECTION dir : DIRECTION.values()) {
            if (buttonPress[dir.ordinal()]) {
                updateDirection(dir, delta);
            }
        }
    }

    private void updateDirection(DIRECTION dir, float delta) {
        // Incremento il timer per la direzione premuta
        pressTimer[dir.ordinal()] += delta;
        considerMove(dir);
    }

    private void releaseDirection(DIRECTION dir) {
        // Resetto lo stato del tasto e considero il cambiamento di direzione
        buttonPress[dir.ordinal()] = false;
        considerReface(dir);
        pressTimer[dir.ordinal()] = 0f;
    }

    private void considerMove(DIRECTION dir) {
        // Se il tasto è stato premuto abbastanza a lungo, eseguo il movimento
        if (pressTimer[dir.ordinal()] > WALK_REFACE_TRESHOLD) {
            player.move(dir);
        }
    }

    private void considerReface(DIRECTION dir) {
        // Se il tasto non è stato premuto abbastanza a lungo, cambio la faccia del giocatore
        if (pressTimer[dir.ordinal()] < WALK_REFACE_TRESHOLD) {
            player.reface(dir);
        }
    }
}
