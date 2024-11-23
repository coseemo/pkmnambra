package com.coseemo.pkmnambra.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args) {
        TexturePacker.process("assets/sprites/professor", "assets/sprites/professorpacked", "professorpacked");
    }
}

