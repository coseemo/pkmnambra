package com.coseemo.pkmnambra.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.ControllerListener;

public class PS4InputDevice implements InputDevice, ControllerListener {
    private Controller controller;
    // PS4 controller mapping constants
    private static final int PS4_DPAD_UP = 13;
    private static final int PS4_DPAD_DOWN = 14;
    private static final int PS4_DPAD_LEFT = 15;
    private static final int PS4_DPAD_RIGHT = 16;
    private static final int PS4_X_BUTTON = 1;
    private static final int PS4_CIRCLE_BUTTON = 2;

    public PS4InputDevice() {
        initializeController();
    }

    private void initializeController() {

        System.out.println("Initializing PS4 controller...");
        System.out.println("Available controllers: " + Controllers.getControllers().size);

        for (Controller c : Controllers.getControllers()) {
            System.out.println("Found controller: " + c.getName());
            if (isPS4Controller(c)) {
                controller = c;
                System.out.println("PS4 controller connected: " + c.getName());
                break;
            }
        }

        if (controller == null) {
            System.out.println("No PS4 controller found. Please connect a controller.");
        }
    }

    private boolean isPS4Controller(Controller c) {
        String name = c.getName().toLowerCase();
        return name.contains("ps4") || name.contains("playstation") || name.contains("wireless controller");
    }

    // InputDevice methods
    @Override
    public boolean isUpPressed() {
        if (controller != null) {
            return controller.getButton(PS4_DPAD_UP);
        }
        return false;
    }

    @Override
    public boolean isDownPressed() {
        if (controller != null) {
            return controller.getButton(PS4_DPAD_DOWN);
        }
        return false;
    }

    @Override
    public boolean isLeftPressed() {
        if (controller != null) {
            return controller.getButton(PS4_DPAD_LEFT);
        }
        return false;
    }

    @Override
    public boolean isRightPressed() {
        if (controller != null) {
            return controller.getButton(PS4_DPAD_RIGHT);
        }
        return false;
    }

    @Override
    public boolean isActionPressed() {
        if (controller != null) {
            return controller.getButton(PS4_X_BUTTON);
        }
        return false;
    }

    @Override
    public boolean isBackPressed() {
        if (controller != null) {
            return controller.getButton(PS4_CIRCLE_BUTTON);
        }
        return false;
    }

    @Override
    public void update() {
        if (controller == null) {
            initializeController();
        }
    }

    // ControllerListener required methods
    @Override
    public void connected(Controller controller) {
        if (this.controller == null && isPS4Controller(controller)) {
            this.controller = controller;
            System.out.println("PS4 controller connected: " + controller.getName());
        }
    }

    @Override
    public void disconnected(Controller controller) {
        if (this.controller == controller) {
            this.controller = null;
            System.out.println("PS4 controller disconnected");
        }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        System.out.println("Button pressed: " + buttonCode);
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }
}
