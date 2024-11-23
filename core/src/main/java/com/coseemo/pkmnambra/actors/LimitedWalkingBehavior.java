package com.coseemo.pkmnambra.actors;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.maplogic.DIRECTION;

public class LimitedWalkingBehavior extends ActorBehavior {

    private float moveIntervalMinimum;
    private float moveIntervalMaximum;
    private Random random;

    private float timer;
    private float currentWaitTime;

    private GridPoint2 moveDelta;
    private int limNorth, limSouth, limEast, limWest;

    // Costruttore che imposta i limiti di movimento, gli intervalli di tempo e il generatore di numeri casuali
    public LimitedWalkingBehavior(Actor actor, int limNorth, int limSouth, int limEast, int limWest,
                                  float moveIntervalMinimum, float moveIntervalMaximum, Random random) {
        super(actor);
        this.limNorth = limNorth;
        this.limSouth = limSouth;
        this.limEast = limEast;
        this.limWest = limWest;
        this.moveIntervalMinimum = moveIntervalMinimum;
        this.moveIntervalMaximum = moveIntervalMaximum;
        this.random = random;
        this.timer = 0f;
        this.currentWaitTime = calculateWaitTime();
        this.moveDelta = new GridPoint2();
    }

    // Metodo per gestire l'interazione con il giocatore (non implementata in questa classe)
    @Override
    public List<Dialogue> interact(Player player) {
        return null;
    }

    // Metodo di aggiornamento chiamato ogni frame per gestire il movimento limitato
    @Override
    public void update(float delta) {
        // Incremento il timer con il delta di tempo
        timer += delta;

        // Se è trascorso abbastanza tempo, eseguo un nuovo movimento
        if (timer >= currentWaitTime) {
            // Scelgo una direzione casuale tra quelle disponibili
            int directionIndex = random.nextInt(DIRECTION.values().length);
            DIRECTION moveDirection = DIRECTION.values()[directionIndex];

            // Verifico che il movimento non superi i limiti impostati
            if (this.moveDelta.x + moveDirection.getDx() > limEast ||
                -(this.moveDelta.x + moveDirection.getDx()) > limWest ||
                this.moveDelta.y + moveDirection.getDy() > limNorth ||
                -(this.moveDelta.y + moveDirection.getDy()) > limSouth) {

                // Se il movimento è fuori limite, rioriento l'attore nella direzione selezionata
                getActor().reface(moveDirection);

                // Ricalcolo il tempo di attesa per il prossimo movimento
                currentWaitTime = calculateWaitTime();
                timer = 0f;
                return;
            }

            // Eseguo il movimento dell'attore
            boolean moved = getActor().move(moveDirection);
            if (moved) {
                this.moveDelta.x += moveDirection.getDx();
                this.moveDelta.y += moveDirection.getDy();
            }

            // Ricalcolo il tempo di attesa per il prossimo movimento
            currentWaitTime = calculateWaitTime();
            timer = 0f;
        }
    }

    // Metodo per calcolare un tempo di attesa casuale tra i movimenti
    private float calculateWaitTime() {
        return random.nextFloat() * (moveIntervalMaximum - moveIntervalMinimum) + moveIntervalMinimum;
    }
}
