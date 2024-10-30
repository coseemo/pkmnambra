package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.coseemo.pkmnambra.ui.OptionBox;
import com.coseemo.pkmnambra.util.EventNotifier;

public class CaptureController extends InputAdapter {

    private OptionBox optionBox;
    private EventNotifier eventNotifier;

    public CaptureController(OptionBox optionBox, EventNotifier eventNotifier) {
        this.optionBox = optionBox;
        this.eventNotifier = eventNotifier;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.UP:
                optionBox.moveUp();
                break;
            case Keys.DOWN:
                optionBox.moveDown();
                break;
            case Keys.ENTER: // Conferma l'opzione selezionata
                handleSelection(optionBox.getIndex());
                break;
        }
        return true;
    }

    private void handleSelection(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                eventNotifier.notifyObservers("BAIT");
                break;
            case 1:
                eventNotifier.notifyObservers("PERFUME");
                break;
            case 2:
                eventNotifier.notifyObservers("TRAP");
                break;
            case 3:
                eventNotifier.notifyObservers("ATTEMPT_CAPTURE");
                break;
            case 4:
                eventNotifier.notifyObservers("FLEE");
                break;
            default:
                break;
        }
    }
}
