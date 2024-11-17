package com.coseemo.pkmnambra.characters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.coseemo.pkmnambra.characters.logic.NPC;
import com.coseemo.pkmnambra.dialogue.DialogueDb;
import com.coseemo.pkmnambra.items.CaptureItems.CaptureItemFactory;
import com.coseemo.pkmnambra.items.KeyItem;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.util.AnimationSet;
import com.coseemo.pkmnambra.util.GameState;

import java.util.*;

public class Professor extends NPC {
    private static final String INTRO_DIALOGUE = "init";
    private static final String EMPTY_TEAM_DIALOGUE = "professor_empty_team";
    private static final String POST_INTRO_DIALOGUE = "professor_post_intro";
    private static final String NEAR_COMPLETION_DIALOGUE = "professor_near_completion";
    private static final String COMPLETION_DIALOGUE = "professor_completion";
    private static final String NEW_POKEMON_REWARD_DIALOGUE = "new_pokemon_reward";
    private static final String MILESTONE_REWARD_DIALOGUE = "milestone_reward";
    private static final String REGULAR_REWARD_DIALOGUE = "regular_reward";

    private static final List<String> REQUIRED_POKEMON = Arrays.asList(
        "Bellossom", "Ludicolo", "Exeggutor", "Slakoth",
        "Toucannon", "Parasect", "Ursaring"
    );
    private Map<String, Integer> pokemonCaptureCount;
    private Set<String> uniquePokemonCaptured;
    private float moveTimer;
    private static final float MOVE_INTERVAL = 2.0f;
    private final Random random;
    private boolean dialogueInitialized = false;

    public Professor(Place place, int x, int y, String npcId, AssetManager assetManager) {
        super(place, x, y, npcId, createProfessorAnimationSet(assetManager), assetManager);
        this.pokemonCaptureCount = new HashMap<>();
        this.uniquePokemonCaptured = new HashSet<>();
        this.random = new Random();
        this.moveTimer = 0;
        initializePokemonList();
    }

    private void initializePokemonList() {
        for (String pokemon : REQUIRED_POKEMON) {
            pokemonCaptureCount.put(pokemon, 0);
        }
    }

    @Override
    public void update(float delta) {

        super.update(delta);
        moveTimer += delta;

        if (moveTimer >= MOVE_INTERVAL && getState() == ACTOR_STATE.STANDING) {
            moveTimer = 0;
            moveRandomly();
        }

    }

    private void moveRandomly() {
        DIRECTION[] directions = DIRECTION.values();
        DIRECTION randomDir = directions[random.nextInt(directions.length)];
        move(randomDir);
    }

    protected void handleInteraction(Player player) {
        // Reset dialogue initialization flag at the start of each interaction
        dialogueInitialized = false;

        // Initialize dialogue based on current state
        initializeDialogue();

        // Only process Pokemon and increment interaction if dialogue was successfully initialized
        if (dialogueInitialized) {
            // Process all Pokemon in the team in one interaction
            while (!player.getTeam().isEmpty()) {
                checkAndRewardPokemon(player);
            }

            // Initialize adventure and increment interactions only on first interaction
            if (!state.hasInteracted()) {
                initAdventure(player);
                state.incrementInteractions();
            }
        }
    }

    protected void initializeDialogue() {
        if (dialogueInitialized) {
            return;
        }

        Player player = GameState.getInstance().getPlayer();

        DialogueDb dialogueDb = DialogueDb.getInstance();
        String dialogueToUse;

        if (!state.hasInteracted()) {
            dialogueToUse = INTRO_DIALOGUE;
        } else if (player.getTeam().isEmpty()) {
            dialogueToUse = EMPTY_TEAM_DIALOGUE;
        } else if (uniquePokemonCaptured.size() >= REQUIRED_POKEMON.size()) {
            dialogueToUse = COMPLETION_DIALOGUE;
        } else if (uniquePokemonCaptured.size() >= 5) {
            dialogueToUse = NEAR_COMPLETION_DIALOGUE;
        } else {
            dialogueToUse = POST_INTRO_DIALOGUE;
        }

        if (dialogueDb.hasDialogue(dialogueToUse)) {
            setDialogue(dialogueDb.getDialogue(dialogueToUse));
            state.setCurrentDialoguePath(dialogueToUse);
            dialogueInitialized = true;
        }
    }

    protected void checkAndRewardPokemon(Player player) {
        DialogueDb dialogueDb = DialogueDb.getInstance();
        Iterator<String> teamIterator = player.getTeam().iterator();

        // Handle dialogue first
        while (teamIterator.hasNext()) {
            String pokemon = teamIterator.next();

            if (REQUIRED_POKEMON.contains(pokemon)) {
                // After the dialogue, remove the Pokémon from the team
                teamIterator.remove();  // Remove the Pokémon from the team after dialogue is shown
                System.out.println("remove");

                int currentCount = pokemonCaptureCount.get(pokemon);
                pokemonCaptureCount.put(pokemon, currentCount + 1);

                // Handle the Pokémon reward logic
                if (!uniquePokemonCaptured.contains(pokemon)) {
                    uniquePokemonCaptured.add(pokemon);
                    setDialogue(dialogueDb.getDialogue(NEW_POKEMON_REWARD_DIALOGUE));
                    giveNewPokemonReward(player, uniquePokemonCaptured.size());
                    state.setFlag("captured_" + pokemon, true);
                } else {
                    // Milestone and regular rewards
                    if ((currentCount + 1) % 10 == 0) {
                        setDialogue(dialogueDb.getDialogue(MILESTONE_REWARD_DIALOGUE));
                        giveMilestoneReward(player, pokemon, currentCount);
                        state.setFlag("milestone_" + pokemon + "_" + (currentCount + 1), true);
                    } else {
                        setDialogue(dialogueDb.getDialogue(REGULAR_REWARD_DIALOGUE));
                        giveRegularReward(player);
                    }
                }
            }
        }

        // If all required Pokémon have been captured, give the completion reward
        if (uniquePokemonCaptured.size() >= REQUIRED_POKEMON.size() && !state.hasFlag("game_completed")) {
            setDialogue(dialogueDb.getDialogue(COMPLETION_DIALOGUE));
            giveCompletionReward(player);
            state.setFlag("game_completed", true);
        }

        state.incrementInteractions(); // Increment interaction count after dialogue and rewards
    }

    private void giveNewPokemonReward(Player player, int uniqueCount) {
        // Base reward based on unique capture count
        switch (uniqueCount) {
            case 1:
            case 2:
                for (int i = 0; i < 6; i++) {
                    player.addItem(CaptureItemFactory.createItem("POKEBALL"));
                }
                break;
            case 3:
            case 4:
                for (int i = 0; i < 4; i++) {
                    player.addItem(CaptureItemFactory.createItem("GREATBALL"));
                }
                break;
            case 5:
            case 6:
            case 7:
                for (int i = 0; i < 2; i++) {
                    player.addItem(CaptureItemFactory.createItem("ULTRABALL"));
                }
                break;
            default:
                player.addItem(CaptureItemFactory.createItem("POKEBALL"));
                break;
        }

        // Additional rewards based on progress
        if (uniqueCount > 3) {
            player.addItem(CaptureItemFactory.createItem("SPICYBAIT"));
        }
        if (uniqueCount > 5) {
            player.addItem(CaptureItemFactory.createItem("MYSTICPERFUME"));
        }

        // Update the game state to reflect this reward
        state.setFlag("reward_given_" + uniqueCount, true);
    }

    private void initAdventure(Player player) {

        player.initInventory();
        for(int i = 0; i <5; i++){
            player.addItem(CaptureItemFactory.createItem("POKEBALL"));
            player.addItem(CaptureItemFactory.createItem("STANDARDBAIT"));
            player.addItem(CaptureItemFactory.createItem("FLORALPERFUME"));
            player.addItem(CaptureItemFactory.createItem("BASICTRAP"));
        }

        player.initList(REQUIRED_POKEMON);
    }
    private void giveMilestoneReward(Player player, String pokemon, int count) {
        // Special rewards for milestone captures
        player.addItem(CaptureItemFactory.createItem("ULTRABALL"));
        player.addItem(CaptureItemFactory.createItem("SWEETBAIT"));
    }

    private void giveRegularReward(Player player) {
        for (int i = 0; i <= 5; i++) {
            player.addItem(CaptureItemFactory.createItem("POKEBALL"));
        }
    }

    private void giveCompletionReward(Player player) {
        KeyItem researchCertificate = new KeyItem(
            "Professor's Certificate",
            "A special certificate proving your contribution to Pokemon research"
        );
        player.addItem(researchCertificate);

        // Bonus completion rewards
        player.addItem(CaptureItemFactory.createItem("MASTERBALL"));
        player.addItem(CaptureItemFactory.createItem("MYSTICPERFUME"));
        player.addItem(CaptureItemFactory.createItem("ADVANCEDTRAP"));

        state.setFlag("game_completed", true);
    }

    private static AnimationSet createProfessorAnimationSet(AssetManager assetManager) {
        // Your existing animation setup code here
        TextureAtlas atlas = assetManager.get("assets/sprites/player_packed/mimipacked.atlas", TextureAtlas.class);
        return new AnimationSet(
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_east"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_west"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_north"), Animation.PlayMode.LOOP_PINGPONG),
            new Animation<>(0.3f / 2f, atlas.findRegions("mimi_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
            atlas.findRegion("mimi_standing_east"),
            atlas.findRegion("mimi_standing_west"),
            atlas.findRegion("mimi_standing_north"),
            atlas.findRegion("mimi_standing_south")
        );
    }
}
