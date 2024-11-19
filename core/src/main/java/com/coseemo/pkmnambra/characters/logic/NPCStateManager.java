package com.coseemo.pkmnambra.characters.logic;

import java.util.HashMap;
import java.util.Map;

public class NPCStateManager {
    private static NPCStateManager instance;
    private final Map<String, NPCState> npcStates;

    private NPCStateManager() {
        npcStates = new HashMap<>();
    }

    public static NPCStateManager getInstance() {
        if (instance == null) {
            instance = new NPCStateManager();
        }
        return instance;
    }

    public NPCState getOrCreateState(String npcId) {
        return npcStates.computeIfAbsent(npcId, NPCState::new);
    }

    public void clearAllStates() {
        npcStates.clear();
    }
}
