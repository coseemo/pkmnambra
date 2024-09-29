package com.coseemo.pkmnambra.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.coseemo.pkmnambra.util.AnimationSet;

public class Actor {
    private TileMap map;
    private int x, y;
    private DIRECTION facing;
    private float worldX, worldY;
    private int srcX, srcY, destX, destY;
    private float animTimer;
    private float ANIM_TIME = 0.3f;
    private float walkTimer;
    private boolean moveRequestThisFrame;
    private AnimationSet animations;
    private ACTOR_STATE state;

    public Actor(TileMap map, int x, int y, AnimationSet animations) {
        this.map = map;
        this.worldX = x;
        this.worldY = y;
        this.animations = animations;
        map.getTile(x, y).setActor(this);
        this.x = x;
        this.y = y;
        this.state = ACTOR_STATE.STANDING;
        this.facing = DIRECTION.EAST;
    }
    public enum ACTOR_STATE{
        WALKING,
        STANDING,
        ;
    }
    public boolean move(DIRECTION dir){
        // Controlla se l'attore sta già camminando
        if (state == ACTOR_STATE.WALKING) {
            if(facing == dir)
                moveRequestThisFrame = true;
            return false;
        }

        // Controlla se le nuove coordinate sono all'interno dei limiti della mappa
        if (x + dir.getDx() < 0 || x + dir.getDx() >= map.getWidth() || y + dir.getDy() < 0 || y + dir.getDy() >= map.getHeight()) {
            return false; // Movimento fuori dai limiti
        }

        // Controlla se c'è già un attore nella nuova posizione
        if (map.getTile(x + dir.getDx(), y + dir.getDy()).getActor() != null) {
            return false; // Movimento bloccato da un altro attore
        }

        initializeMove(dir);

        // Aggiorna la posizione
        map.getTile(x, y).setActor(null); // Rimuovi l'attore dalla posizione attuale
        x += dir.getDx(); // Aggiorna la posizione dell'attore
        y += dir.getDy();
        map.getTile(x, y).setActor(this); // Imposta l'attore nella nuova posizione

        System.out.println("Moved to: (" + x + ", " + y + ")");

        return true; // Movimento avvenuto con successo
    }
    private void initializeMove(DIRECTION dir){
        this.facing = dir;
        this.srcX = x;
        this.srcY = y;
        this.destX = x + dir.getDx();
        this.destY = y + dir.getDy();
        this.worldX = x;
        this.worldY = y;
        this.animTimer = 0f;
        this.state = ACTOR_STATE.WALKING;
    }

    public void update(float delta) {
            if(state == ACTOR_STATE.WALKING) {
                animTimer += delta;
                walkTimer += delta;

                worldX = Interpolation.linear.apply(srcX, destX, animTimer / ANIM_TIME);
                worldY = Interpolation.linear.apply(srcY, destY, animTimer / ANIM_TIME);

                if(animTimer > ANIM_TIME) {
                    finishMove();
                }
            }

            // Continue moving if requested
            if (moveRequestThisFrame) {
                move(facing);
            } else {
                walkTimer = 0f; // Reset the walk timer when not moving
            }

            moveRequestThisFrame = false; // Reset movement request
        }
    private void finishMove(){
        state = ACTOR_STATE.STANDING;
        this.worldY = destY;
        this.worldX = destX;
        this.srcY = 0;
        this.srcX = 0;
        this.destY = 0;
        this.destX = 0;
    }
    public TextureRegion getSprite(){
        if(state == ACTOR_STATE.WALKING){
            return animations.getWalking(facing).getKeyFrame(walkTimer);
        } else if (state == ACTOR_STATE.STANDING){
            return animations.getStanding(facing);
        }
        return animations.getStanding(DIRECTION.EAST);
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }
}

