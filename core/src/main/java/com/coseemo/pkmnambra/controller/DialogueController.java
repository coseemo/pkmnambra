package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.dialogue.DialogueNode;
import com.coseemo.pkmnambra.dialogue.DialogueTraverser;
import com.coseemo.pkmnambra.dialogue.DialogueNode.NODE_TYPE;
import com.coseemo.pkmnambra.ui.DialogueBox;
import com.coseemo.pkmnambra.ui.OptionBox;

public class DialogueController extends InputAdapter {
    private DialogueTraverser traverser;
    private DialogueBox dialogueBox;
    private OptionBox optionBox;
    private Runnable completionCallback;
    private boolean waitingForInput = false;

    public DialogueController(DialogueBox box, OptionBox optionBox) {
        this.dialogueBox = box;
        this.optionBox = optionBox;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Se il dialogo è visibile, blocchiamo tutti gli input
        if (dialogueBox.isVisible()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!dialogueBox.isVisible()) {
            return false;
        }

        // Gestione delle opzioni
        if (optionBox.isVisible()) {
            if (keycode == Keys.UP) {
                optionBox.moveUp();
                return true;
            } else if (keycode == Keys.DOWN) {
                optionBox.moveDown();
                return true;
            }
        }

        // Gestione del tasto X per far avanzare il testo carattere per carattere
        if (keycode == Keys.X) {
            if (!dialogueBox.isTextComplete()) {
                // Se il testo sta ancora animando, lo completiamo immediatamente
                dialogueBox.completeAnimation();
                waitingForInput = true;
            } else if (waitingForInput) {
                // Se stavamo aspettando input, avanziamo al prossimo nodo
                waitingForInput = false;
                if (traverser != null) {
                    if (traverser.getType() == NODE_TYPE.END) {
                        endDialogue();
                    } else if (traverser.getType() == NODE_TYPE.LINEAR) {
                        progress(0);
                    } else if (traverser.getType() == NODE_TYPE.MULTIPLE_CHOICE && optionBox.isVisible()) {
                        progress(optionBox.getIndex());
                    }
                } else {
                    endDialogue();
                }
            }
            return true;
        }

        return true;
    }

    private void progress(int index) {
        if (traverser == null) return;

        DialogueNode nextNode = traverser.getNextNode(index);
        optionBox.setVisible(false);

        if (nextNode == null) {
            endDialogue();
            return;
        }

        // Mostra il nuovo testo
        dialogueBox.animateText(nextNode.getText());
        waitingForInput = false;

        // Gestisci le opzioni se necessario
        if (nextNode.getType() == NODE_TYPE.MULTIPLE_CHOICE) {
            optionBox.clear();
            for (String s : nextNode.getLabels()) {
                optionBox.addOption(s);
            }
        }
    }

    private void endDialogue() {
        traverser = null;
        dialogueBox.setVisible(false);
        optionBox.setVisible(false);
        waitingForInput = false;
        if (completionCallback != null) {
            completionCallback.run();
            completionCallback = null;
        }
    }

    public void update(float delta) {
        if (dialogueBox.isVisible()) {
            if (dialogueBox.isTextComplete() && !waitingForInput) {
                // Quando il testo finisce di animare, aspettiamo l'input dell'utente
                waitingForInput = true;
            }

            // Mostra le opzioni solo quando il testo è finito e siamo in un nodo a scelta multipla
            if (dialogueBox.isTextComplete() && traverser != null &&
                traverser.getType() == NODE_TYPE.MULTIPLE_CHOICE) {
                optionBox.setVisible(true);
            }
        }

        // Aggiorna il dialogue box per l'animazione del testo
        dialogueBox.act(0.1f);

        // Se l'option box è visibile, aggiornalo
        if (optionBox.isVisible()) {
            optionBox.act(delta);
        }
    }

    public void startDialogue(Dialogue dialogue, Runnable onComplete) {
        if (dialogue == null) {
            System.err.println("Warning: Attempted to start null dialogue");
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        this.completionCallback = onComplete;
        traverser = new DialogueTraverser(dialogue);
        dialogueBox.setVisible(true);
        dialogueBox.animateText(traverser.getText());
        waitingForInput = false;

        // Imposta le opzioni se è un nodo a scelta multipla
        if (traverser.getType() == NODE_TYPE.MULTIPLE_CHOICE) {
            optionBox.clear();
            for (String s : dialogue.getNode(dialogue.getStart()).getLabels()) {
                optionBox.addOption(s);
            }
        }
    }

    public boolean isDialogueShowing() {
        return dialogueBox.isVisible();
    }
}
