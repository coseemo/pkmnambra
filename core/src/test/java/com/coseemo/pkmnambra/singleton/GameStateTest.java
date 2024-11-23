package com.coseemo.pkmnambra.singleton;

import com.badlogic.gdx.assets.AssetManager;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.map.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    // Sequenze ANSI per i colori
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";

    @Test
    void testSingletonInstance() {
        System.out.println(YELLOW + "Eseguendo testSingletonInstance()..." + RESET);

        // Ottieni due istanze del Singleton
        GameState instance1 = GameState.getInstance();
        GameState instance2 = GameState.getInstance();

        // Verifica che le due istanze siano la stessa
        assertSame(instance1, instance2, RED + "GameState non è un Singleton, le istanze differiscono!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testSingletonInstance()" + RESET);
    }

    @Test
    void testSingletonNotNull() {
        System.out.println(YELLOW + "Eseguendo testSingletonNotNull()..." + RESET);

        // Verifica che l'istanza del Singleton non sia null
        GameState instance = GameState.getInstance();
        assertNotNull(instance, RED + "GameState.getInstance() ha restituito null!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testSingletonNotNull()" + RESET);
    }

    @Test
    void testInitialization() {
        System.out.println(YELLOW + "Eseguendo testInitialization()..." + RESET);

        // Simula l'inizializzazione del Singleton
        Main mockGame = new Main();
        Player mockPlayer = new Player();  // Supponi che il costruttore di Player sia disponibile
        String mockMapname = "test";
        TileMap testMap = new TileMap(1, 1);
        AssetManager testAssetManager = new AssetManager();
        World mockWorld = new World(mockMapname, testMap, testAssetManager);    // Supponi che il costruttore di World sia disponibile

        GameState gameState = GameState.getInstance();
        gameState.initialize(mockGame, mockPlayer, mockWorld);

        // Verifica che i valori inizializzati siano corretti
        assertEquals(mockGame, gameState.getGame(), RED + "GameState non ha inizializzato correttamente il gioco!" + RESET);
        assertNotNull(gameState.getPlayerState(), RED + "PlayerState non è stato inizializzato!" + RESET);
        assertNotNull(gameState.getMapState(), RED + "MapState non è stato inizializzato!" + RESET);
        assertNotNull(gameState.getScreenManager(), RED + "ScreenManager non è stato inizializzato!" + RESET);
        assertNotNull(gameState.getResourceManager(), RED + "ResourceManager non è stato inizializzato!" + RESET);
        assertNotNull(gameState.getEventManager(), RED + "EventManager non è stato inizializzato!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testInitialization()" + RESET);
    }
}
