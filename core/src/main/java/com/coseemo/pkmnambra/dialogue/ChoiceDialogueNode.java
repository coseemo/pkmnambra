package com.coseemo.pkmnambra.dialogue;

import java.util.ArrayList;
import java.util.List;

public class ChoiceDialogueNode extends DialogueNode {

    private String text;
    private int id;
    private List<Integer> pointers = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    public ChoiceDialogueNode(String text, int id) {
        super(text, id);
        this.text = text;
        this.id = id;
    }

    // Aggiungo una scelta di dialogo con il relativo ID di destinazione
    public void addChoice(String text, int targetId) {
        pointers.add(targetId);
        labels.add(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public List<Integer> getPointers() {
        return pointers;
    }

    public List<String> getLabels() {
        return labels;
    }
}
