package com.coseemo.pkmnambra.actors;

import com.coseemo.pkmnambra.dialogue.Dialogue;

import java.util.List;

public abstract class ActorBehavior {

    private Actor actor;
    public ActorBehavior(Actor actor) {
        this.actor = actor;
    }
    public abstract List<Dialogue> interact(Player player);
    public abstract void update(float delta);
    public Actor getActor() {
        return actor;
    }

}
