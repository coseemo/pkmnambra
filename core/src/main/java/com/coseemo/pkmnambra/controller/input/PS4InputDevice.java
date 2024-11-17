package com.coseemo.pkmnambra.controller.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class PS4InputDevice implements InputDevice {
    private Controller controller;
    private static final int PS4_DPAD_UP = 0;
    private static final int PS4_DPAD_DOWN = 1;
    private static final int PS4_DPAD_LEFT = 2;
    private static final int PS4_DPAD_RIGHT = 3;
    private static final int PS4_X_BUTTON = 4;
    private static final int PS4_CIRCLE_BUTTON = 5;

    public PS4InputDevice() {
        // Cerca di trovare il primo controller disponibile
        if (Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
        }
    }

    @Override
    public boolean isUpPressed() {
        return controller != null && controller.getButton(PS4_DPAD_UP);
    }

    @Override
    public boolean isDownPressed() {
        return controller != null && controller.getButton(PS4_DPAD_DOWN);
    }

    @Override
    public boolean isLeftPressed() {
        return controller != null && controller.getButton(PS4_DPAD_LEFT);
    }

    @Override
    public boolean isRightPressed() {
        return controller != null && controller.getButton(PS4_DPAD_RIGHT);
    }

    @Override
    public boolean isActionPressed() {
        return controller != null && controller.getButton(PS4_X_BUTTON);
    }

    @Override
    public boolean isBackPressed() {
        return controller != null && controller.getButton(PS4_CIRCLE_BUTTON);
    }

    @Override
    public void update() {
        if (controller == null && Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
        }
    }
}
