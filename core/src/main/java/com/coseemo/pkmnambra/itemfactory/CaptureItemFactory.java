package com.coseemo.pkmnambra.itemfactory;

public class CaptureItemFactory {

    public static CaptureItem createItem(String itemType) {
        switch (itemType.toUpperCase()) {
            // Varianti di Pokéball
            case "POKEBALL":
                return new Pokeball("Pokeball", "Standard Pokeball for capturing Pokémon",
                    "You throw the Pokeball and hope for the best!", 10);
            case "GREATBALL":
                return new Pokeball("GreatBall", "A better Pokeball with higher capture rate",
                    "The Great Ball shakes and catches the Pokémon!", 20);
            case "ULTRABALL":
                return new Pokeball("UltraBall", "An advanced Pokeball for higher-level Pokémon",
                    "The Ultra Ball glows brightly as it captures the Pokémon!", 30);
            case "MASTERBALL":
                return new Pokeball("MasterBall", "The best Pokeball that guarantees capture",
                    "The Master Ball catches the Pokémon without fail!", 100);

            // Varianti di Bait
            case "STANDARDBAIT":
                return new Bait("StandardBait", "An item that makes Pokémon less likely to flee",
                    "You toss some standard bait to attract the Pokémon!", 5);
            case "SPICYBAIT":
                return new Bait("SpicyBait", "A spicy bait that excites Pokémon",
                    "The spicy aroma draws Pokémon closer!", 7);
            case "SWEETBAIT":
                return new Bait("SweetBait", "A sweet bait that attracts Pokémon",
                    "The sweet scent entices the Pokémon to come closer!", 6);
            case "SMELLYBAIT":
                return new Bait("SmellyBait", "A smelly bait that attracts curious Pokémon",
                    "The pungent smell piques the curiosity of Pokémon!", 8);

            // Varianti di Perfume
            case "FLORALPERFUME":
                return new Perfume("FloralPerfume", "A lovely floral scent that calms Pokémon",
                    "The floral scent calms the Pokémon around you!", 7);
            case "FRUITYPERFUME":
                return new Perfume("FruityPerfume", "A fruity aroma that attracts Pokémon",
                    "The fruity scent entices the Pokémon to approach!", 8);
            case "HERBALPERFUME":
                return new Perfume("HerbalPerfume", "An herbal fragrance that soothes Pokémon",
                    "The herbal aroma brings a sense of peace to the Pokémon!", 6);
            case "MYSTICPERFUME":
                return new Perfume("MysticPerfume", "A mystical scent that intrigues Pokémon",
                    "The mysterious scent fascinates the nearby Pokémon!", 9);

            // Varianti di Trap
            case "BASICTRAP":
                return new Trap("BasicTrap", "A simple trap to catch Pokémon",
                    "You set a basic trap to catch the Pokémon!", 10);
            case "ADVANCEDTRAP":
                return new Trap("AdvancedTrap", "An advanced trap for skilled trainers",
                    "The advanced trap is ready to catch the escaping Pokémon!", 15);
            case "TRICKYTRAP":
                return new Trap("TrickyTrap", "A deceptive trap to fool Pokémon",
                    "The tricky trap ensnares the unsuspecting Pokémon!", 12);
            case "QUICKTRAP":
                return new Trap("QuickTrap", "A trap designed for quick captures",
                    "You quickly set the trap to catch the Pokémon!", 8);

            // Caso per oggetti sconosciuti
            default:
                throw new IllegalArgumentException("Unknown item type: " + itemType);
        }
    }
}
