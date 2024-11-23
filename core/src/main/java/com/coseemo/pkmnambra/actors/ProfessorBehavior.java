package com.coseemo.pkmnambra.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.coseemo.pkmnambra.dialogue.*;
import com.coseemo.pkmnambra.maplogic.DIRECTION;

import java.util.*;

public class ProfessorBehavior extends ActorBehavior {
    private Map<String, Boolean> toCatch;
    private List<Dialogue> dialogues = new ArrayList<>();

    public ProfessorBehavior(Actor actor) {
        super(actor);
        AssetManager assetManager = new AssetManager();
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        assetManager.finishLoading();

        getActor().reface(DIRECTION.SOUTH);
        this.toCatch = new HashMap<>();
        String[] list = new String[]{"Bellossom", "Exeggutor", "Slakoth", "Parasect"};
        for (String pokemon : list) {
            toCatch.put(pokemon, true);
        }
    }

    public List<Dialogue> interact(Player player) {
        dialogues = new ArrayList<>();

        if (!player.hasInventory()) {
            Dialogue introDialogue = DialogueDb.getDialogue("professor_intro");
            dialogues.add(introDialogue);

            player.initInventory();
            player.setToCatch(toCatch);
            starterKit(player);
        } else {
            Dialogue postIntroDialogue = DialogueDb.getDialogue("professor_post_intro");
            dialogues.add(postIntroDialogue);

            List<String> team = player.getTeam();
            for (String pokemon : team) {
                if (toCatch.containsKey(pokemon) && toCatch.get(pokemon)) {
                    // Pokémon nuovo trovato
                    Dialogue newCatchDialogue = DialogueDb.getDialogue("new_pokemon_reward");
                    dialogues.add(newCatchDialogue);
                    rewardForNewPokemon(player);
                    toCatch.put(pokemon, false);
                } else {
                    // Pokémon già trovato
                    Dialogue repeatCatchDialogue = DialogueDb.getDialogue("regular_reward");
                    dialogues.add(repeatCatchDialogue);
                    regularReward(player);
                }
                toCatch = player.getToCatch();
            }

            player.clearTeam();
        }
        return dialogues;
    }

    @Override
    public void update(float delta) {
        getActor().update(delta);
    }

    public void starterKit(Player player) {
        for (int i = 0; i < 5; i++) {
            player.addItem("Pokeball");
            player.addItem("StandardBait");
            player.addItem("FloralPerfume");
            player.addItem("BasicTrap");
        }
    }

    public void rewardForNewPokemon(Player player) {
        int remainingToCatch = 0;

        // Conta quanti Pokémon restano da catturare
        for (Map.Entry<String, Boolean> entry : toCatch.entrySet()) {
            if (entry.getValue()) {
                remainingToCatch++;
            }
        }

        // Determina la ricompensa in base al numero rimanente di Pokémon da catturare
        if (remainingToCatch > 3) {
            player.addItem("GreatBall");
            player.addItem("GreatBall");
            player.addItem("GreatBall");
            player.addItem("StandardBait");
        } else if (remainingToCatch == 3) {
            player.addItem("UltraBall");
            player.addItem("UltraBall");
            player.addItem("SweetBait");
            player.addItem("SweetBait");
            player.addItem("SweetBait");
        } else if (remainingToCatch == 2) {
            player.addItem("UltraBall");
            player.addItem("UltraBall");
            player.addItem("FruityPerfume");
            player.addItem("AdvancedTrap");
            player.addItem("AdvancedTrap");
        } else if (remainingToCatch == 1) {
            player.addItem("UltraBall");
            player.addItem("UltraBall");
            player.addItem("UltraBall");
            player.addItem("MysticPerfume");
            player.addItem("MysticPerfume");
            player.addItem("QuickTrap");
        } else {
            player.addItem("MysticPerfume");
            player.addItem("TrickyTrap");
            player.addItem("GreatBall");
        }

        System.out.println("Ricompensa per nuovo Pokémon trovata. Restano " + remainingToCatch + " Pokémon da catturare.");
    }

    public void regularReward(Player player) {
        player.addItem("GreatBall");
        player.addItem("GreatBall");
        player.addItem("FloralPerfume");
        System.out.println("Ricompensa regolare assegnata: 1 GreatBall e 1 FloralPerfume.");
    }
}
