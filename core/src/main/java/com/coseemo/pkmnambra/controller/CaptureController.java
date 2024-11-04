package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.coseemo.pkmnambra.items.Inventory;
import com.coseemo.pkmnambra.items.Item;
import com.coseemo.pkmnambra.screen.CaptureScreen;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.util.EventNotifier;
import com.coseemo.pkmnambra.util.GameState;

import java.util.List;

public class CaptureController extends InputAdapter {

    CaptureScreen captureScreen;
    private OptionBox optionBox;
    private Inventory inventory;
    private EventNotifier eventNotifier;
    private boolean mustPress;
    private boolean isInInventoryMenu = false;

    public CaptureController(OptionBox optionBox, CaptureScreen captureScreen) {
        this.optionBox = optionBox;
        GameState gameState = GameState.getInstance();
        this.eventNotifier = gameState.getEventNotifier();
        this.inventory = gameState.getPlayer().getInventory();
        this.mustPress = false;
    }

    public boolean keyDown(int keycode) {

        if(mustPress){
           return false;

            } else {

            switch (keycode) {

                case Keys.UP:
                    optionBox.moveUp();
                    break;
                case Keys.DOWN:
                    optionBox.moveDown();
                    break;
                case Keys.ENTER:
                    if (isInInventoryMenu) {
                        handleSecondarySelection(optionBox.getIndex());
                    } else {
                        handleMainSelection(optionBox.getIndex());
                    }
                    break;
            }
        }
        return true;
    }

    private void handleMainSelection(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                isInInventoryMenu = true;
                showInventoryOptions("BAIT");
                break;
            case 1:
                isInInventoryMenu = true;
                showInventoryOptions("PERFUME");
                break;
            case 2:
                isInInventoryMenu = true;
                showInventoryOptions("TRAP");
                break;
            case 3:
                isInInventoryMenu = true;
                showInventoryOptions("POKEBALL");
                break;
            case 4:
                eventNotifier.notifyObservers("FLEE");
                break;
            default:
                break;
        }
    }

    private void handleSecondarySelection(int selectedIndex) {
        if (!isInInventoryMenu) {
            optionBox.setVisible(true);
            return;
        }

        // Controlla se l'utente ha selezionato "Back" per tornare al menu principale
        if (selectedIndex == optionBox.getChoices().size() - 1) {
            isInInventoryMenu = false;
            showMainOptions();
            return;
        }


        String selectedChoice = optionBox.getChoices().get(selectedIndex);
        Item selectedItem = null;

        // Cerca l'oggetto selezionato in base al nome esatto
        for (Item item : inventory.getItemList()) {
            if (selectedChoice.startsWith(item.getName())) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            switch (selectedItem.getName()) {
                case "Pokeball":
                    eventNotifier.notifyObservers("USE_POKEBALL");
                    break;
                case "Great Ball":
                    eventNotifier.notifyObservers("USE_GREATBALL");
                    break;
                case "Ultra Ball":
                    eventNotifier.notifyObservers("USE_ULTRABALL");
                    break;
                case "Master Ball":
                    eventNotifier.notifyObservers("USE_MASTERBALL");
                    break;
                case "Standard Bait":
                    eventNotifier.notifyObservers("USE_STANDARDBAIT");
                    break;
                case "Spicy Bait":
                    eventNotifier.notifyObservers("USE_SPICYBAIT");
                    break;
                case "Sweet Bait":
                    eventNotifier.notifyObservers("USE_SWEETBAIT");
                    break;
                case "Smelly Bait":
                    eventNotifier.notifyObservers("USE_SMELLYBAIT");
                    break;
                case "Floral Perfume":
                    eventNotifier.notifyObservers("USE_FLORALPERFUME");
                    break;
                case "Fruity Perfume":
                    eventNotifier.notifyObservers("USE_FRUITYPERFUME");
                    break;
                case "Herbal Perfume":
                    eventNotifier.notifyObservers("USE_HERBALPERFUME");
                    break;
                case "Mystic Perfume":
                    eventNotifier.notifyObservers("USE_MYSTICPERFUME");
                    break;
                case "Basic Trap":
                    eventNotifier.notifyObservers("USE_BASICTRAP");
                    break;
                case "Advanced Trap":
                    eventNotifier.notifyObservers("USE_ADVANCEDTRAP");
                    break;
                case "Tricky Trap":
                    eventNotifier.notifyObservers("USE_TRICKYTRAP");
                    break;
                case "Quick Trap":
                    eventNotifier.notifyObservers("USE_QUICKTRAP");
                    break;
                default:
                    eventNotifier.notifyObservers(selectedItem.getCategory().toUpperCase());
                    break;
            }
            inventory.removeItem(selectedItem);
        }

        // Torna al menu principale dopo l'uso
        isInInventoryMenu = false;
        showMainOptions();
    }

    private void showMainOptions() {
        optionBox.clearChoices();
        optionBox.addOption("Throw Bait");
        optionBox.addOption("Use Perfume");
        optionBox.addOption("Set Trap");
        optionBox.addOption("Use PokeBall");
        optionBox.addOption("Run Away");
        optionBox.setVisible(true);
    }

    private void showInventoryOptions(String category) {
        optionBox.clearChoices();
        List<Item> items = inventory.getItemsByCategory(category);

        for (Item item : items) {
            optionBox.addOption(item.getName() + " x" + inventory.getItemQuantity(item));
        }
        // Aggiunge il tasto "Back" come ultima opzione per tornare al menu principale
        optionBox.addOption("Back");

        optionBox.setVisible(true);
    }

    public void setMustPress(boolean mustPress) {
        this.mustPress = mustPress;
    }
}
