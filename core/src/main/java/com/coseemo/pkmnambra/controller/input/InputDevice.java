package com.coseemo.pkmnambra.controller.input;
public interface InputDevice {
    boolean isUpPressed();
    boolean isDownPressed();
    boolean isLeftPressed();
    boolean isRightPressed();
    boolean isActionPressed();
    boolean isBackPressed();
    void update();
}
