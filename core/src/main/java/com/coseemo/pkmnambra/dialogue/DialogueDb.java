package com.coseemo.pkmnambra.dialogue;

import java.util.HashMap;

public class DialogueDb {
    private final HashMap<String, Dialogue> knownDialogue = new HashMap<>();
    private static DialogueDb instance;

    public static DialogueDb getInstance() {
        if (instance == null) {
            instance = new DialogueDb();
        }
        return instance;
    }

    public void addDialogue(String name, Dialogue dialogue) {
        knownDialogue.put(name, dialogue);
    }

    public Dialogue getDialogue(String name) {
        if (!knownDialogue.containsKey(name)) {
            throw new IllegalArgumentException("Could not find Dialogue of name " + name);
        }
        return knownDialogue.get(name);
    }

    public void printDb() {
        System.out.println("Dialogue Database:");
        if (knownDialogue.isEmpty()) {
            System.out.println("  The database is empty.");
        } else {
            for (String name : knownDialogue.keySet()) {
                System.out.println("  Dialogue Name: " + name);
                Dialogue dialogue = knownDialogue.get(name);
                System.out.println("    Content: " + dialogue);
            }
        }
    }

    public boolean hasDialogue(String name) {
        return knownDialogue.containsKey(name);
    }
}
