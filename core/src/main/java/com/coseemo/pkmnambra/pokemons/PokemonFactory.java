package com.coseemo.pkmnambra.pokemons;

public class PokemonFactory {

    public static Pokemon createPokemon(String name) {
        switch (name.toUpperCase()) {
            case "EXEGGUTOR":
                return new Exeggutor();
            case "SLAKOTH":
                return new Slakoth();
            case "LUDICOLO":
                return new Ludicolo();
            case "ALOLAN_RAICHU":
                return new Raichu();
            case "BELLOSSOM":
                return new Bellossom();
            case "TOUCANNON":
                return new Toucannon();
            case "URSARING":
                return new Ursaring();
            case "PARASECT":
                return new Parasect();
            case "CRABRAWLER":
                return new Crabrawler();
            case "PYUKUMUKU":
                return new Pyukumuku();
            default:
                throw new IllegalArgumentException("Pokémon non riconosciuto: " + name);
        }
    }
}

