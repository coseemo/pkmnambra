package com.coseemo.pkmnambra.dialogue;

import java.util.HashMap;

public class DialogueDb {

    private static final HashMap<String, Dialogue> knownDialogue = new HashMap<>();

    // Aggiungo un dialogo alla mappa globale
    protected static void addTerrain(String name, Dialogue dialogue) {
        knownDialogue.put(name, dialogue);
    }

    // Ottengo un dialogo dato il suo nome
    public static Dialogue getDialogue(String name) {
        if (!knownDialogue.containsKey(name)) {
            throw new NullPointerException("Could not find Dialogue of name " + name);
        }
        return knownDialogue.get(name);
    }
}
