package com.coseemo.pkmnambra.dialogue;

import java.util.ArrayList;
import java.util.List;

public class DialogueNode {

    private final ArrayList<Integer> pointers = new ArrayList<>();
    private final ArrayList<String> labels = new ArrayList<>();
    private final String text;
    private final int id;
    private NODE_TYPE type;

    public enum NODE_TYPE {
        MULTIPLE_CHOICE,
        LINEAR,
        END,
    }

    public DialogueNode(String text, int id) {
        this.text = text;
        this.id = id;
        type = NODE_TYPE.END;
    }

    // Aggiungo una scelta con il relativo ID
    public void addChoice(String option, int nodeId) {
        if (type == NODE_TYPE.LINEAR) {
            pointers.clear();
        }
        labels.add(option);
        pointers.add(nodeId);
        type = NODE_TYPE.MULTIPLE_CHOICE;
    }

    // Rendo il nodo lineare, assegnando un unico ID di destinazione
    public void makeLinear(int nodeId) {
        pointers.clear();
        labels.clear();
        pointers.add(nodeId);
        type = NODE_TYPE.LINEAR;
    }

    public List<Integer> getPointers() {
        return pointers;
    }

    public List<String> getLabels() {
        return labels;
    }

    public NODE_TYPE getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getID() {
        return id;
    }
}
