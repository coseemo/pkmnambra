package com.coseemo.pkmnambra.items;

import com.coseemo.pkmnambra.items.CaptureItems.Bait;
import com.coseemo.pkmnambra.items.CaptureItems.Perfume;
import com.coseemo.pkmnambra.items.CaptureItems.Pokeball;
import com.coseemo.pkmnambra.items.CaptureItems.Trap;

public class ItemFactory {

    public static Item createItem(String itemType) {
        switch (itemType.toLowerCase()) {
            // Varianti di Pokéball
            case "pokeball":
                return new Pokeball("Pokeball", "Standard Pokeball for capturing Pokémon",
                    "You throw the Pokeball and hope for the best!", 10);
            case "greatball":
                return new Pokeball("Great Ball", "A better Pokeball with higher capture rate",
                    "The Great Ball shakes and catches the Pokémon!", 20);
            case "ultraball":
                return new Pokeball("Ultra Ball", "An advanced Pokeball for higher-level Pokémon",
                    "The Ultra Ball glows brightly as it captures the Pokémon!", 30);
            case "masterball":
                return new Pokeball("Master Ball", "The best Pokeball that guarantees capture",
                    "The Master Ball catches the Pokémon without fail!", 100);

            // Varianti di Bait
            case "standardbait":
                return new Bait("Standard Bait", "An item that makes Pokémon less likely to flee",
                    "You toss some standard bait to attract the Pokémon!", 5);
            case "spicybait":
                return new Bait("Spicy Bait", "A spicy bait that excites Pokémon",
                    "The spicy aroma draws Pokémon closer!", 7);
            case "sweetbait":
                return new Bait("Sweet Bait", "A sweet bait that attracts Pokémon",
                    "The sweet scent entices the Pokémon to come closer!", 6);
            case "smellybait":
                return new Bait("Smelly Bait", "A smelly bait that attracts curious Pokémon",
                    "The pungent smell piques the curiosity of Pokémon!", 8);

            // Varianti di Perfume
            case "floralperfume":
                return new Perfume("Floral Perfume", "A lovely floral scent that calms Pokémon",
                    "The floral scent calms the Pokémon around you!", 7);
            case "fruityperfume":
                return new Perfume("Fruity Perfume", "A fruity aroma that attracts Pokémon",
                    "The fruity scent entices the Pokémon to approach!", 8);
            case "herbalperfume":
                return new Perfume("Herbal Perfume", "An herbal fragrance that soothes Pokémon",
                    "The herbal aroma brings a sense of peace to the Pokémon!", 6);
            case "mysticperfume":
                return new Perfume("Mystic Perfume", "A mystical scent that intrigues Pokémon",
                    "The mysterious scent fascinates the nearby Pokémon!", 9);

            // Varianti di Trap
            case "basictrap":
                return new Trap("Basic Trap", "A simple trap to catch Pokémon",
                    "You set a basic trap to catch the Pokémon!", 10);
            case "advancedtrap":
                return new Trap("Advanced Trap", "An advanced trap for skilled trainers",
                    "The advanced trap is ready to catch the escaping Pokémon!", 15);
            case "trickytrap":
                return new Trap("Tricky Trap", "A deceptive trap to fool Pokémon",
                    "The tricky trap ensnares the unsuspecting Pokémon!", 12);
            case "quicktrap":
                return new Trap("Quick Trap", "A trap designed for quick captures",
                    "You quickly set the trap to catch the Pokémon!", 8);

            // Caso per KeyItem
            case "keyitem":
                return new KeyItem("Ancient Key", "A mysterious key for opening ancient doors");

            // Caso per oggetti sconosciuti
            default:
                throw new IllegalArgumentException("Unknown item type: " + itemType);
        }
    }
}
