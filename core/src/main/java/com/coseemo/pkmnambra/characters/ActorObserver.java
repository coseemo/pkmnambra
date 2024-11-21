package com.coseemo.pkmnambra.characters;

import com.coseemo.pkmnambra.maplogic.DIRECTION;

public interface ActorObserver {

    public void actorMoved(Actor a, DIRECTION direction, int x, int y);

    public void attemptedMove(Actor a, DIRECTION direction);

    public void actorBeforeMoved(Actor a, DIRECTION direction);

}
