package com.coseemo.pkmnambra.characters.logic;

import com.badlogic.gdx.assets.AssetManager;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.util.AnimationSet;

public abstract class NPC extends Actor {
    protected String npcId;
    protected NPCState state;
    protected AssetManager assetManager;
    private Dialogue currentDialogue;  // Aggiungiamo questo campo
    private Runnable dialogueCallback;

    public NPC(Place place, int x, int y, String npcId, AnimationSet animationSet, AssetManager assetManager) {
        super(place, x, y, animationSet);
        this.npcId = npcId;
        this.assetManager = assetManager;
        this.state = NPCStateManager.getInstance().getOrCreateState(npcId);
        initializeDialogue();
    }

    protected void setDialogueCallback(Runnable callback) {
        this.dialogueCallback = callback;
    }

    protected void executeDialogueCallback() {
        if (dialogueCallback != null) {
            dialogueCallback.run();
            dialogueCallback = null; // Reset dopo l'esecuzione
        }
    }

    protected abstract void initializeDialogue();
    protected abstract void handleInteraction(Player player);

    protected void setDialogue(Dialogue dialogue) {
        this.currentDialogue = dialogue;
    }

    public Dialogue getCurrentDialogue() {
        return currentDialogue;
    }
    public void onDialogueComplete() {
        setInDialogue(false);
        executeDialogueCallback();
    }

    @Override
    public void interactWithPlayer(Player player) {
        reface(DIRECTION.getOpposite(player.getFacing()));
        setState(ACTOR_STATE.STANDING);
        setInDialogue(true);
        handleInteraction(player);
        state.incrementInteractions();
        // Facciamo partire il dialogo se è stato impostato
        if (currentDialogue != null) {
            startDialogue();
        }
    }

    private void startDialogue() {
        initializeDialogue();
    }
}
