package com.coseemo.pkmnambra.itemfactory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaptureItemFactoryTest {

    // Sequenze ANSI per i colori
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    @Test
    void testCreatePokeball() {
        System.out.println(YELLOW + "Eseguendo testCreatePokeball()..." + RESET);

        // Test per la creazione di una Pokeball
        CaptureItem item = CaptureItemFactory.createItem("POKEBALL");
        assertNotNull(item, RED + "Pokeball non è stato creato correttamente!" + RESET);
        assertEquals("Pokeball", item.getName(), RED + "Il nome della Pokeball non è corretto!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testCreatePokeball()" + RESET);
    }

    @Test
    void testCreateBait() {
        System.out.println(YELLOW + "Eseguendo testCreateBait()..." + RESET);

        // Test per la creazione di uno StandardBait
        CaptureItem item = CaptureItemFactory.createItem("STANDARDBAIT");
        assertNotNull(item, RED + "StandardBait non è stato creato correttamente!" + RESET);
        assertEquals("StandardBait", item.getName(), RED + "Il nome dello StandardBait non è corretto!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testCreateBait()" + RESET);
    }

    @Test
    void testCreatePerfume() {
        System.out.println(YELLOW + "Eseguendo testCreatePerfume()..." + RESET);

        // Test per la creazione di un FloralPerfume
        CaptureItem item = CaptureItemFactory.createItem("FLORALPERFUME");
        assertNotNull(item, RED + "FloralPerfume non è stato creato correttamente!" + RESET);
        assertEquals("FloralPerfume", item.getName(), RED + "Il nome del FloralPerfume non è corretto!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testCreatePerfume()" + RESET);
    }

    @Test
    void testCreateTrap() {
        System.out.println(YELLOW + "Eseguendo testCreateTrap()..." + RESET);

        // Test per la creazione di un BasicTrap
        CaptureItem item = CaptureItemFactory.createItem("BASICTRAP");
        assertNotNull(item, RED + "BasicTrap non è stato creato correttamente!" + RESET);
        assertEquals("BasicTrap", item.getName(), RED + "Il nome del BasicTrap non è corretto!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testCreateTrap()" + RESET);
    }

    @Test
    void testInvalidItemType() {
        System.out.println(YELLOW + "Eseguendo testInvalidItemType()..." + RESET);

        // Test per tipo di oggetto non valido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CaptureItemFactory.createItem("INVALIDTYPE");
        });

        assertTrue(exception.getMessage().contains("Unknown item type"), RED + "L'eccezione non contiene il messaggio atteso!" + RESET);

        System.out.println(GREEN + "Test completato con successo: testInvalidItemType()" + RESET);
    }
}
