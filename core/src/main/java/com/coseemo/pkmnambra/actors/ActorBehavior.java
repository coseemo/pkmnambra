package com.coseemo.pkmnambra.actors;

import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.dialogue.Dialogue;

import java.util.List;

public abstract class ActorBehavior {

    private Actor actor;

    // Costruttore che inizializza l'attore
    public ActorBehavior(Actor actor) {
        this.actor = actor;
    }

    // Metodo astratto per gestire l'interazione con il giocatore
    public abstract List<Dialogue> interact(Player player);

    // Metodo astratto per aggiornare lo stato dell'attore
    public abstract void update(float delta);

    // Restituisco l'attore associato al comportamento
    public Actor getActor() {
        return actor;
    }
}
