package com.coseemo.pkmnambra.maplogic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.coseemo.pkmnambra.characters.Player;
import com.coseemo.pkmnambra.characters.Actor;
import com.coseemo.pkmnambra.controller.PlayerController;
import com.coseemo.pkmnambra.util.MapLoader;
import com.coseemo.pkmnambra.util.states.GameState;

public class TeleportObject extends WorldObject {
    private final String targetWorld;
    private final int targetX;
    private final int targetY;
    private Player playerToTeleport = null;

    public TeleportObject(int x, int y, String targetWorld, int targetX, int targetY,
                           TextureRegion texture, GridPoint2[] tiles) {
        super(x, y, true, texture, 1, 1, tiles);
        this.targetWorld = targetWorld;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void onActorStep(Actor actor) {
        // Se l'attore è il player e non è già in fase di teletrasporto
        if (actor instanceof Player && playerToTeleport == null) {
            playerToTeleport = (Player) actor;

            executeTeleport();
        }
    }

    private void executeTeleport() {
        GameState gameState = GameState.getInstance();

        System.out.println("Inizio teletrasporto...");
        System.out.println("Destinazione: " + targetWorld + " (" + targetX + ", " + targetY + ")");
        if (targetWorld != null && !targetWorld.isEmpty()) {
            World arrive = MapLoader.loadMapAndObjects("assets/maps/" + targetWorld + ".txt",
                gameState.getResourceManager().getAssetManager());
            System.out.println("Cambio mondo...");
            gameState.getScreenManager().changeScreen(null); // Considera se null è corretto qui
            playerToTeleport.changeWorld(arrive, targetX, targetY);
            gameState.getMapState().setCurrentPlace(arrive);
        } else {
            System.out.println("Teletrasporto nello stesso mondo...");
            playerToTeleport.setCoords(targetX, targetY);
        }
        playerToTeleport = null; // Reset dopo teletrasporto
    }

}
