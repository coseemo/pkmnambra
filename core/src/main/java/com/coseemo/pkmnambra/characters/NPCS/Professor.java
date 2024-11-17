package com.coseemo.pkmnambra.characters.NPCS;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.util.AnimationSet;

public class Professor extends NPC {
    // Costanti per i nomi dei dialoghi
    private static final String INTRO_DIALOGUE = "professor_intro";
    private static final String QUEST_DIALOGUE = "professor_quest";
    private static final String QUEST_PROGRESS_DIALOGUE = "professor_quest_progress";
    private static final String QUEST_COMPLETE_DIALOGUE = "professor_quest_complete";

    public Professor(Place place, int x, int y, String npcId, AssetManager assetManager) {
        super(place, x, y, npcId, createProfessorAnimationSet(assetManager), assetManager);
    }

    @Override
    protected void initializeDialogue() {
        DialogueDb dialogueDb = DialogueDb.getInstance();

        if (state.hasFlag("quest_completed")) {
            // Quest completata
            if (dialogueDb.hasDialogue(QUEST_COMPLETE_DIALOGUE)) {
                setDialogue(dialogueDb.getDialogue(QUEST_COMPLETE_DIALOGUE));
            }
        } else if (state.hasFlag("quest_started")) {
            // Quest in corso
            if (dialogueDb.hasDialogue(QUEST_PROGRESS_DIALOGUE)) {
                setDialogue(dialogueDb.getDialogue(QUEST_PROGRESS_DIALOGUE));
            }
        } else if (state.hasInteracted()) {
            // Il giocatore ha già parlato ma non ha accettato la quest
            if (dialogueDb.hasDialogue(QUEST_DIALOGUE)) {
                setDialogue(dialogueDb.getDialogue(QUEST_DIALOGUE));
            }
        } else {
            // Prima interazione
            if (dialogueDb.hasDialogue(INTRO_DIALOGUE)) {
                setDialogue(dialogueDb.getDialogue(INTRO_DIALOGUE));
            }
        }
    }

    @Override
    protected void handleInteraction(Player player) {
        // Aggiorna il dialogo prima dell'interazione
        System.out.println("bubi");
        initializeDialogue();

        // Gestisci gli stati della quest
        if (state.hasFlag("quest_started")) {
            handleQuestStart(player);
        } else if (state.hasFlag("quest_completed")) {
            handleQuestProgress(player);
        } else {
            handleQuestComplete(player);
        }
    }

    private void handleQuestStart(Player player) {
        // Implementa la logica di inizio quest
        // Questo verrà chiamato quando il giocatore accetta la quest
        setDialogueCallback(() -> {
            // Questo codice viene eseguito quando il dialogo è completato
            state.setFlag("quest_started", true);
            // Qui puoi aggiungere altri effetti di inizio quest
            // Per esempio: player.addQuest(new ProfessorQuest());
        });
    }

    private void handleQuestProgress(Player player) {
        // Controlla se il giocatore ha completato gli obiettivi
        if (checkQuestCompleted(player)) {
            state.setFlag("quest_completed", true);
            setDialogue(DialogueDb.getInstance().getDialogue(QUEST_COMPLETE_DIALOGUE));
            giveReward(player);
        }
    }

    private void handleQuestComplete(Player player) {
        // Gestisce l'interazione dopo che la quest è stata completata
        // Non serve fare nulla di speciale qui, il dialogo appropriato
        // è già stato impostato in initializeDialogue()
    }

    private boolean checkQuestCompleted(Player player) {
        // Implementa la logica per controllare se il giocatore
        // ha completato gli obiettivi della quest
        // Per esempio:
        // return player.hasItem("special_item");
        return false; // Placeholder
    }

    private void giveReward(Player player) {
        // Implementa la logica per dare la ricompensa al giocatore
        // Per esempio:
        // player.addItem(new Item("reward_item"));
        // player.addExperience(1000);
    }

    private static AnimationSet createProfessorAnimationSet(AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        AnimationSet animations = new AnimationSet(
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_east"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_west"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_north"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
            atlas.findRegion("mimi_standing_east"),
            atlas.findRegion("mimi_standing_west"),
            atlas.findRegion("mimi_standing_north"),
            atlas.findRegion("mimi_standing_south")
        );
        return animations;
    }
}
