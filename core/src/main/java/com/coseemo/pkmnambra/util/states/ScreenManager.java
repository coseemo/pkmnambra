package com.coseemo.pkmnambra.util.states;

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

    public void changeScreen(Screen newScreen) {
        if(newScreen == null){
            game.setScreen(new TransitionScreen(game, currentScreen, currentScreen));
        } else {
            game.setScreen(new TransitionScreen(game, currentScreen, newScreen));
            this.currentScreen = newScreen;
        }
    }
}

