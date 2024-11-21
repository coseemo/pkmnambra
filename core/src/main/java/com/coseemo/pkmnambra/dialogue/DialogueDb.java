package com.coseemo.pkmnambra.dialogue;

import java.util.HashMap;

public class DialogueDb {

    private static final HashMap<String, Dialogue> knownDialogue = new HashMap<String, Dialogue>();

    protected static void addTerrain(String name, Dialogue dialogue) {
        knownDialogue.put(name, dialogue);
    }

    public static Dialogue getDialogue(String name) {
        if (!knownDialogue.containsKey(name)) {
            throw new NullPointerException("Could not find Dialogue of name "+name);
        }
        return knownDialogue.get(name);
    }

}
