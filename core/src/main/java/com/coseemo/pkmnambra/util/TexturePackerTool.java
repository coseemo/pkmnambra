package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args){
        TexturePacker.process("assets/sprites/player", "assets/sprites/player_packed", "mimipacked");
    }
}

