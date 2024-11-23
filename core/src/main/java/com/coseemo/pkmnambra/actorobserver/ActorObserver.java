package com.coseemo.pkmnambra.actorobserver;

import com.coseemo.pkmnambra.actors.Actor;
import com.coseemo.pkmnambra.maplogic.DIRECTION;

public interface ActorObserver {

    public void actorMoved(Actor a, DIRECTION direction, int x, int y);

    public void attemptedMove(Actor a, DIRECTION direction);

    public void actorBeforeMoved(Actor a, DIRECTION direction);

}
