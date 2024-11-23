package com.coseemo.pkmnambra.dialogue;

import java.util.HashMap;
import java.util.Map;

public class Dialogue {

    private Map<Integer, DialogueNode> nodes = new HashMap<>();

    // Ottengo il nodo del dialogo con l'ID specificato
    public DialogueNode getNode(int id) {
        return nodes.get(id);
    }

    // Aggiungo un nodo al dialogo
    public void addNode(DialogueNode node) {
        this.nodes.put(node.getID(), node);
    }

    // Ottengo l'ID di partenza del dialogo
    public int getStart() {
        return 0;
    }

    // Restituisco la dimensione del dialogo (numero di nodi)
    public int size() {
        return nodes.size();
    }
}
