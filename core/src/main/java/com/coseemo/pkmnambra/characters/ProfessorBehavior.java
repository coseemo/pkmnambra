package com.coseemo.pkmnambra.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.coseemo.pkmnambra.dialogue.*;
import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.pokemons.Pokemon;
import com.coseemo.pkmnambra.characters.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorBehavior extends ActorBehavior {
    private Map<String, Boolean> toCatch;
    private List<Dialogue> dialogues = new ArrayList<>();



    public ProfessorBehavior(Actor actor) {
        super(actor);
        AssetManager assetManager = new AssetManager();
        assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
        assetManager.load("assets/dialogues/dialogues.xml", DialogueDb.class);
        assetManager.finishLoading();

        this.toCatch = new HashMap<>();
        String[] list = new String[]{"Bellossom", "Exeggutor", "Slakoth", "Parasect"};
        for(String pokemon : list){
            toCatch.put(pokemon, true);
        }

    }

    public List<Dialogue> interact(Player player) {

        dialogues = new ArrayList<>();

        if (!player.hasInventory()) {
            // Ottieni dialogo introduttivo dal database
            Dialogue introDialogue = DialogueDb.getDialogue("professor_intro");
            dialogues.add(introDialogue);

            player.initInventory();
            player.setToCatch(toCatch);

        } else {
            // Ottieni dialogo post-intro dal database
            Dialogue postIntroDialogue = DialogueDb.getDialogue("professor_post_intro");
            dialogues.add(postIntroDialogue);

            toCatch = player.getToCatch();

            // Controlla la squadra del giocatore
            List<String> team = player.getTeam();
            for (String pokemon : team) {
                if (!toCatch.get(pokemon)) {
                    // Pokémon non catturato: mostra dialogo e assegna ricompensa
                    Dialogue newCatchDialogue = DialogueDb.getDialogue("new_pokemon_reward");
                    dialogues.add(newCatchDialogue);
                } else {
                    // Pokémon già catturato: mostra dialogo alternativo e assegna ricompensa
                    Dialogue repeatCatchDialogue = DialogueDb.getDialogue("regular_reward");
                    dialogues.add(repeatCatchDialogue);
                }
            }

            // Rimuove i Pokémon dalla squadra del giocatore dopo il controllo
            player.clearTeam();
        }
        return dialogues;
    }

    @Override
    public void update(float delta) {
        getActor().update(delta);
    }

}
