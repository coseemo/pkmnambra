package com.coseemo.pkmnambra;

import com.badlogic.gdx.Game;
import com.coseemo.pkmnambra.screen.FirstScreen;
import com.coseemo.pkmnambra.screen.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
