package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.coseemo.pkmnambra.characters.Actor;
import com.coseemo.pkmnambra.characters.ActorBehavior;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.maplogic.Tile;

import java.util.List;

public class InteractionController extends InputAdapter {

    private final Actor a;
    private DialogueController dialogueController;

    public InteractionController(Actor a, DialogueController dialogueController) {
        this.a = a;
        this.dialogueController = dialogueController;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.X) {

            Tile target = a.getWorld().getMap().getTile(a.getX()+a.getFacing().getDx(), a.getY()+a.getFacing().getDy());
            if (target.getActor() != null) {
                Actor targetActor = target.getActor();
                ActorBehavior actorBehavior = a.getWorld().getActorBehavior(targetActor);
                List<Dialogue> dialogues = actorBehavior.interact((Player) a);
                if (dialogues != null) {
                    System.out.println("bibu");
                    targetActor.reface(DIRECTION.getOpposite(a.getFacing()));
                        for(Dialogue d : dialogues){
                            dialogueController.startDialogue(d);
                        }
                }else {
                    System.out.println("dialogo vuoto");
                }
            }else{
                System.out.println("non c'è l'actor");
            }
            return false;
        }
        return false;
    }

}
