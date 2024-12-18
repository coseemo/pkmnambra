package com.coseemo.pkmnambra.singleton;

import com.badlogic.gdx.assets.AssetManager;

public class ResourceManager {
    private AssetManager assetManager;

    public ResourceManager() {
        this.assetManager = new AssetManager();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
