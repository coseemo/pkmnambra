package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.characters.logic.Actor;
import com.coseemo.pkmnambra.characters.logic.NPC;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.util.states.GameState;

public class NPCController extends InputAdapter {
    private final GameState gameState;
    private final DialogueController dialogueController;
    private NPC currentInteractingNPC;

    public NPCController(DialogueController dialogueController) {
        this.gameState = GameState.getInstance();
        this.dialogueController = dialogueController;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Don't process input if dialogue is already showing
        if (dialogueController.isDialogueShowing()) {
            return false;
        }

        if (keycode == Keys.X) {
            Player player = gameState.getPlayer();
            // Calculate target position based on player facing direction
            int targetX = (int) (player.getPlaceX() + player.getFacing().getDx());
            int targetY = (int) (player.getPlaceY() + player.getFacing().getDy());

            // Check map bounds
            if (isValidPosition(targetX, targetY)) {
                Actor actor = gameState.getCurrentPlace().getMap().getTile(targetX, targetY).getActor();
                if (actor instanceof NPC) {
                    NPC npc = (NPC) actor;
                    currentInteractingNPC = npc;

                    // Trigger NPC interaction
                    npc.interactWithPlayer(player);

                    // Start dialogue if available
                    Dialogue dialogue = npc.getCurrentDialogue();
                    if (dialogue != null) {
                        startNPCDialogue(npc);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < gameState.getCurrentPlace().getMap().getWidth() &&
            y >= 0 && y < gameState.getCurrentPlace().getMap().getHeight();
    }

    public void startNPCDialogue(NPC npc) {
        if (currentInteractingNPC == npc && dialogueController.isDialogueShowing()) {
            return; // Evita di riavviare il dialogo corrente
        }

        currentInteractingNPC = npc;
        dialogueController.startDialogue(npc.getCurrentDialogue(), () -> {
            if (currentInteractingNPC != null) {
                currentInteractingNPC.onDialogueComplete();
                currentInteractingNPC = null;
            }
        });
    }
}
