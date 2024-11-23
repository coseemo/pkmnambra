package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coseemo.pkmnambra.actorobserver.Actor;
import com.coseemo.pkmnambra.capture.CaptureLogic;
import com.coseemo.pkmnambra.capture.CaptureRenderer;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.controller.CaptureController;
import com.coseemo.pkmnambra.pokemonfactory.Pokemon;
import com.coseemo.pkmnambra.singleton.GameState;
import com.coseemo.pkmnambra.captureobserver.CaptureObserver;

public class CaptureScreen implements Screen, CaptureObserver {
    private final Game game;
    private final Pokemon pokemon;
    private Player player;
    private final CaptureLogic captureLogic;
    private final CaptureController captureController;
    private final GameState gameState;
    private final CaptureRenderer captureRenderer;
    private final Viewport viewport;

    private boolean notExit;  // Indica se la schermata deve continuare a essere visibile
    private boolean waitingForKeyPress;  // Indica se si sta aspettando una pressione di tasto
    private boolean keyPressed;  // Indica se il tasto è stato premuto
    private float exitTimer;  // Timer per ritardare la chiusura della schermata

    private static final float EXIT_DELAY = 0.05f; // Ritardo prima di cambiare schermata
    private static final float VIRTUAL_WIDTH = 1280;  // Larghezza virtuale desiderata
    private static final float VIRTUAL_HEIGHT = 700;  // Altezza virtuale desiderata

    private String eventType;  // Tipo di evento per la cattura

    // Costruttore della schermata di cattura
    public CaptureScreen(GameState gameState, Pokemon pokemon) {
        this.game = gameState.getGame();
        this.pokemon = pokemon;
        this.player = gameState.getPlayerState().getPlayer();
        this.captureLogic = new CaptureLogic(pokemon, gameState);
        this.captureRenderer = new CaptureRenderer(pokemon, gameState);
        this.captureController = new CaptureController(captureRenderer.getOptionBox(), gameState);
        this.gameState = gameState;

        this.viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Registrazione della schermata e dell'osservatore dell'evento
        gameState.getScreenManager().setCurrentScreen(this);
        gameState.getEventManager().getEventNotifier().registerObserver(this);

        resetExitState();  // Inizializzazione dello stato di uscita
    }

    @Override
    public void show() {
        // Imposta il processore di input per gestire gli eventi
        Gdx.input.setInputProcessor(captureController);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float delta) {
        // Applica le modifiche alla visualizzazione
        viewport.apply();

        // Rende la cattura e aggiorna l'interfaccia utente
        captureRenderer.render(delta);
        updateCaptureUI();

        // Se stiamo aspettando una pressione di tasto, gestiamo la logica
        if (waitingForKeyPress) {
            handleKeyPress(delta);
        }
    }

    private void updateCaptureUI() {
        // Aggiorna probabilità di cattura e livello di rabbia
        captureRenderer.updateCaptureProbability(captureLogic.getCaptureProbability());
        captureRenderer.updateAngerLevel(captureLogic.getAngerLevel());
    }

    private void handleKeyPress(float delta) {
        // Attende che un tasto venga premuto per continuare
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && !keyPressed) {
            keyPressed = true; // Attende che venga premuto un tasto
        }

        // Quando un tasto è premuto, inizia il timer per cambiare schermata
        if (keyPressed && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            exitTimer += delta;
            if (exitTimer >= EXIT_DELAY) {
                changeScreen();  // Cambia la schermata
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Ridimensiona la visualizzazione
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Non sono necessarie azioni particolari quando la schermata viene messa in pausa
    }

    @Override
    public void resume() {
        // Non sono necessarie azioni particolari quando la schermata viene ripresa
    }

    @Override
    public void hide() {
        // Rimuove il giocatore dal posto attuale e ripristina la sua posizione sicura
        gameState.getMapState().getCurrentPlace().removeActor(player);
        player.setCoords(gameState.getPlayerState().getSafeX(), gameState.getPlayerState().getSafeY());
        player.setState(Actor.ACTOR_STATE.STANDING);
        gameState.getMapState().getCurrentPlace().addPlayer(player);
        Gdx.input.setInputProcessor(null);  // Rimuove il processore di input
    }

    @Override
    public void dispose() {
        // Ripristina lo stato del giocatore e deregistra l'osservatore
        gameState.getPlayerState().setPlayer(player);
        gameState.getEventManager().getEventNotifier().deregisterObserver(this);
        captureRenderer.dispose();  // Rilascia le risorse del renderer di cattura
    }

    @Override
    public void update(String eventType) {
        System.out.println(eventType);
        this.eventType = eventType;

        // Gestione degli eventi di cattura
        switch (eventType) {
            case "USE_STANDARDBAIT":
            case "USE_SPICYBAIT":
            case "USE_SWEETBAIT":
            case "USE_SMELLYBAIT":
                handleBait(eventType);
                break;
            case "USE_FLORALPERFUME":
            case "USE_FRUITYPERFUME":
            case "USE_HERBALPERFUME":
            case "USE_MYSTICPERFUME":
                handlePerfume(eventType);
                break;
            case "USE_BASICTRAP":
            case "USE_ADVANCEDTRAP":
            case "USE_TRICKYTRAP":
            case "USE_QUICKTRAP":
                handleTrap(eventType);
                break;
            case "USE_POKEBALL":
            case "USE_GREATBALL":
            case "USE_ULTRABALL":
            case "USE_MASTERBALL":
                captureLogic.attemptCapture(getBallIndex(eventType));  // Prova a catturare il Pokémon
                break;
            case "CAPTURE_FAIL":
                captureRenderer.updateStatusMessage(pokemon.getName() + " broke free!");  // Cattura fallita
                break;
            case "POKEMON_FLED":
                captureRenderer.updateStatusMessage(pokemon.getName() + " has fled.");  // Il Pokémon è fuggito
                break;
            case "POKEMON_ANGER":
                captureRenderer.updateStatusMessage(pokemon.getName() + " is too angry, run!");  // Il Pokémon è troppo arrabbiato
                break;
            case "CAPTURE_SUCCESS":
                handleCaptureSuccess();  // Gestisce la cattura riuscita
                break;
            case "FLEE":
                captureRenderer.updateStatusMessage("Better run!");  // Il giocatore deve fuggire
                break;
        }

        // Gestisce lo stato di uscita in base all'evento
        handleExitState(eventType);
    }

    private void handleBait(String eventType) {
        // Gestisce l'uso dell'esca
        int baitIndex = getBaitIndex(eventType);
        captureRenderer.updateStatusMessage("You used bait!");
        captureLogic.handleBait(baitIndex);
    }

    private void handlePerfume(String eventType) {
        // Gestisce l'uso del profumo
        int perfumeIndex = getPerfumeIndex(eventType);
        captureRenderer.updateStatusMessage("You used perfume!");
        captureLogic.handlePerfume(perfumeIndex);
    }

    private void handleTrap(String eventType) {
        // Gestisce l'uso di una trappola
        int trapIndex = getTrapIndex(eventType);
        captureRenderer.updateStatusMessage("You set a trap!");
        captureLogic.handleTrap(trapIndex);
    }

    private void handleCaptureSuccess() {
        // Gestisce una cattura riuscita
        System.out.println("gege");
        if (player.addPokemon(pokemon.getName())) {
            captureRenderer.updateStatusMessage("You caught " + pokemon.getName() + "!!!");
            eventType = "CATCH_SUCC";  // Successo nella cattura
        } else {
            captureRenderer.updateStatusMessage("Team is full, go to prof!");  // Il team è pieno
            eventType = "CATCH_FULL";  // Il team è pieno
        }
    }

    private void handleExitState(String eventType) {
        // Gestisce lo stato di uscita in base agli eventi di cattura
        if ("POKEMON_FLED".equals(eventType) ||
            "POKEMON_ANGER".equals(eventType) ||
            "CAPTURE_SUCCESS".equals(eventType) ||
            "FLEE".equals(eventType)) {
            notExit = false;
            waitingForKeyPress = true;  // Attende la pressione di un tasto per uscire
            keyPressed = false;
            exitTimer = 0;
            captureController.setMustPress(true);  // Attiva la necessità di premere un tasto
        }
    }

    private void changeScreen() {
        // Cambia la schermata se necessario
        if (!notExit) {
            resetExitState();  // Ripristina lo stato di uscita
            dispose();  // Rilascia le risorse
            gameState.changeScreen(new GameScreen(gameState));  // Passa alla schermata di gioco
        }
    }

    private void resetExitState() {
        // Ripristina lo stato di uscita
        captureController.setMustPress(false);
        waitingForKeyPress = false;
        keyPressed = false;
        exitTimer = 0;
        notExit = true;
    }

    private int getBallIndex(String eventType) {
        // Determina l'indice della palla in base all'evento
        switch (eventType) {
            case "USE_POKEBALL":
                return 0;
            case "USE_GREATBALL":
                return 1;
            case "USE_ULTRABALL":
                return 2;
            case "USE_MASTERBALL":
                return 3;
            default:
                return -1;
        }
    }

    private int getBaitIndex(String eventType) {
        // Determina l'indice dell'esca in base all'evento
        switch (eventType) {
            case "USE_STANDARDBAIT":
                return 0;
            case "USE_SPICYBAIT":
                return 1;
            case "USE_SWEETBAIT":
                return 2;
            case "USE_SMELLYBAIT":
                return 3;
            default:
                return -1;
        }
    }

    private int getPerfumeIndex(String eventType) {
        // Determina l'indice del profumo in base all'evento
        switch (eventType) {
            case "USE_FLORALPERFUME":
                return 0;
            case "USE_FRUITYPERFUME":
                return 1;
            case "USE_HERBALPERFUME":
                return 2;
            case "USE_MYSTICPERFUME":
                return 3;
            default:
                return -1;
        }
    }

    private int getTrapIndex(String eventType) {
        // Determina l'indice della trappola in base all'evento
        switch (eventType) {
            case "USE_BASICTRAP":
                return 0;
            case "USE_ADVANCEDTRAP":
                return 1;
            case "USE_TRICKYTRAP":
                return 2;
            case "USE_QUICKTRAP":
                return 3;
            default:
                return -1;
        }
    }
}
