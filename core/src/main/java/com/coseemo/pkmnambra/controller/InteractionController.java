package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.actors.ActorBehavior;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.map.Tile;

import java.util.List;

public class InteractionController extends InputAdapter {

    private final Actor a;
    private DialogueController dialogueController;

    private boolean isInDialogue = false;

    public InteractionController(Actor a, DialogueController dialogueController) {
        this.a = a;
        this.dialogueController = dialogueController;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (isInDialogue) {
            return false;
        }

        if (keycode == Keys.X) {
            Tile target = a.getWorld().getMap().getTile(a.getX() + a.getFacing().getDx(), a.getY() + a.getFacing().getDy());
            if (target.getActor() != null) {
                Actor targetActor = target.getActor();
                ActorBehavior actorBehavior = a.getWorld().getActorBehavior(targetActor);
                targetActor.reface(DIRECTION.getOpposite(a.getFacing()));
                List<Dialogue> dialogues = actorBehavior.interact((Player) a);
                if (dialogues != null) {
                    isInDialogue = true;
                    for (Dialogue d : dialogues) {
                        dialogueController.startDialogue(d);
                    }
                    if (dialogueController.isFinished()) {
                        isInDialogue = false;
                    }
                }
            }
            return false;
        }
        return false;
    }
}
