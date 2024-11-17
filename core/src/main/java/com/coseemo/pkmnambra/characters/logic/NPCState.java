package com.coseemo.pkmnambra.characters.logic;

import java.util.HashMap;
import java.util.Map;

public class NPCState {
    private String npcId;
    private Map<String, Object> flags;
    private boolean hasInteracted;
    private int interactionCount;
    private String currentDialoguePath;

    public NPCState(String npcId) {
        this.npcId = npcId;
        this.flags = new HashMap<>();
        this.hasInteracted = false;
        this.interactionCount = 0;
    }

    public void setFlag(String key, Object value) {
        flags.put(key, value);
    }

    public Object getFlag(String key) {
        return flags.getOrDefault(key, null);
    }

    public boolean hasFlag(String key) {
        return flags.containsKey(key); // Corretto: restituisce true se la flag esiste
    }

    public void incrementInteractions() {
        interactionCount++;
        hasInteracted = true;
    }

    public int getInteractionCount() {
        return interactionCount;
    }

    public boolean hasInteracted() {
        return hasInteracted; // Corretto: restituisce il valore effettivo
    }

    public String getCurrentDialoguePath() {
        return currentDialoguePath;
    }

    public void setCurrentDialoguePath(String path) {
        this.currentDialoguePath = path;
    }


    public void printInfo() {
        System.out.println("NPCState Information:");
        System.out.println("NPC ID: " + npcId);
        System.out.println("Has Interacted: " + hasInteracted);
        System.out.println("Interaction Count: " + interactionCount);
        System.out.println("Current Dialogue Path: " + (currentDialoguePath != null ? currentDialoguePath : "None"));

        System.out.println("Flags:");
        if (flags.isEmpty()) {
            System.out.println("  No flags set.");
        } else {
            for (Map.Entry<String, Object> entry : flags.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
