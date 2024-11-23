package com.coseemo.pkmnambra.mapobjectfactory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.actorobserver.World;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.actors.Actor;
import com.coseemo.pkmnambra.savemanager.MapLoader;
import com.coseemo.pkmnambra.singleton.GameState;

public class TeleportObject extends WorldObject {
    private final String targetWorld;
    private final int targetX;
    private final int targetY;
    private Player playerToTeleport = null;

    public TeleportObject(int x, int y, String targetWorld, int targetX, int targetY,
                           TextureRegion texture, GridPoint2[] tiles) {
        super(x, y, true, texture, 0, 0, tiles);
        this.targetWorld = targetWorld;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void onActorStep(Actor actor) {

        if (actor instanceof Player && playerToTeleport == null) {
            playerToTeleport = (Player) actor;

            executeTeleport();
        }

    }

    private void executeTeleport() {
        GameState gameState = GameState.getInstance();

        if (targetWorld != null && !targetWorld.isEmpty()) {
            World arrive = MapLoader.loadMapAndObjects("assets/maps/" + targetWorld + ".txt",
                gameState.getResourceManager().getAssetManager());
            gameState.changeScreen(null);
            playerToTeleport.changeWorld(arrive, targetX, targetY);
            gameState.getMapState().setCurrentPlace(arrive);
        } else {
            playerToTeleport.setCoords(targetX, targetY);
        }
        playerToTeleport = null; // Reset dopo teletrasporto
    }

    public String getTargetWorld() {
        return targetWorld;
    }
}
