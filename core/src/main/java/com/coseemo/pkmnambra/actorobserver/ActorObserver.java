package com.coseemo.pkmnambra.actorobserver;

import com.coseemo.pkmnambra.maplogic.DIRECTION;

public interface ActorObserver {

    // Quando l'attore si muove in una nuova posizione
    void actorMoved(Actor a, DIRECTION direction, int x, int y);

    // Quando si tenta un movimento senza successo
    void attemptedMove(Actor a, DIRECTION direction);

    // Prima che l'attore si muova
    void actorBeforeMoved(Actor a, DIRECTION direction);
}
