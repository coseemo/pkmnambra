package com.coseemo.pkmnambra.singleton;

import com.badlogic.gdx.Screen;
import com.coseemo.pkmnambra.Main;
import com.coseemo.pkmnambra.screen.TransitionScreen;

public class ScreenManager {
    private Main game;
    private Screen currentScreen;

    public ScreenManager(Main game) {
        this.game = game;
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
    }

}

