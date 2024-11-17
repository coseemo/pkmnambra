package com.coseemo.pkmnambra.controller.input;

import com.badlogic.gdx.InputAdapter;

public abstract class BaseController extends InputAdapter {
    protected InputDevice inputDevice;

    public BaseController(InputDevice inputDevice) {
        this.inputDevice = inputDevice;
    }

    public void setInputDevice(InputDevice inputDevice) {
        this.inputDevice = inputDevice;
    }

    public void update(float delta) {
        inputDevice.update();
        updateController(delta);
    }

    protected abstract void updateController(float delta);
}
