package com.coseemo.pkmnambra.controller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardInputDevice implements InputDevice {
    @Override
    public boolean isUpPressed() {
        return Gdx.input.isKeyPressed(Keys.UP);
    }

    @Override
    public boolean isDownPressed() {
        return Gdx.input.isKeyPressed(Keys.DOWN);
    }

    @Override
    public boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Keys.LEFT);
    }

    @Override
    public boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Keys.RIGHT);
    }

    @Override
    public boolean isActionPressed() {
        return Gdx.input.isKeyPressed(Keys.X);
    }

    @Override
    public boolean isBackPressed() {
        return Gdx.input.isKeyPressed(Keys.Z);
    }

    @Override
    public void update() {
        // No additional update needed for keyboard
    }
}
