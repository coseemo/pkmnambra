package com.coseemo.pkmnambra.dialogue;

import com.coseemo.pkmnambra.dialogue.DialogueNode.NODE_TYPE;

public class DialogueTraverser {

    private Dialogue dialogue;
    private DialogueNode currentNode;

    public DialogueTraverser(Dialogue dialogue) {
        this.dialogue = dialogue;
        currentNode = dialogue.getNode(dialogue.getStart());
    }

    // Ottengo il prossimo nodo di dialogo in base all'indice del puntatore
    public DialogueNode getNextNode(int pointerIndex) {
        if (currentNode.getPointers().isEmpty()) {
            return null;
        }
        DialogueNode nextNode = dialogue.getNode(currentNode.getPointers().get(pointerIndex));
        currentNode = nextNode;
        return nextNode;
    }

    public DialogueNode getNode() {
        return currentNode;
    }
}
