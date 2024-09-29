package com.coseemo.pkmnambra.screen;

import com.badlogic.gdx.assets.AssetManager;

public class ServiceLocator {
    private static AssetManager assetManager;

    public static void provideAssetManager(AssetManager manager) {
        assetManager = manager;
    }

    public static AssetManager getAssetManager() {
        if (assetManager == null) {
            throw new IllegalStateException("AssetManager not initialized");
        }
        return assetManager;
    }
}
