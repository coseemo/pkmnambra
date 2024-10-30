package com.coseemo.pkmnambra.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.maplogic.YSortable;
import com.coseemo.pkmnambra.maplogic.TileMap;
import com.coseemo.pkmnambra.util.AnimationSet;

public class Actor implements YSortable {
    private TileMap map;
    private int x, y;
    private DIRECTION facing;
    private float placeX, placeY;
    private int srcX, srcY, destX, destY;
    private float animTimer;
    private float WALK_TIME_PER_TILE = 0.2f;
    private float REFACE_TIME = 0.1f;
    private float walkTimer;
    private boolean moveRequestThisFrame;
    private AnimationSet animations;
    private ACTOR_STATE state;

    public Actor(TileMap map, int x, int y, AnimationSet animations) {
        this.map = map;
        this.placeX = x;
        this.placeY = y;
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
        REFACING,
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

        if (map.getTile(x + dir.getDx(), y + dir.getDy()).getObject() != null &&
            !map.getTile(x + dir.getDx(), y + dir.getDy()).getObject().isWalkable()) {
            return false;
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
        this.placeX = x;
        this.placeY = y;
        this.animTimer = 0f;
        this.state = ACTOR_STATE.WALKING;
    }

    public void update(float delta) {
            if(state == ACTOR_STATE.WALKING) {
                animTimer += delta;
                walkTimer += delta;

                placeX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);
                placeY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);

                if(animTimer > WALK_TIME_PER_TILE) {
                    float leftOverTime = animTimer - WALK_TIME_PER_TILE;
                    finishMove();
                    if (moveRequestThisFrame) {
                        if(move(facing)){
                            animTimer += leftOverTime;
                            placeX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);
                            placeY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);
                        }
                    } else {
                        walkTimer = 0f;
                    }
                }
            }
            if (state == ACTOR_STATE.REFACING) {
                animTimer += delta;
                if (animTimer > REFACE_TIME) {
                    state = ACTOR_STATE.STANDING;
                }
            }
            moveRequestThisFrame = false;
        }

    public void reface(DIRECTION dir) {
        if (state != ACTOR_STATE.STANDING) {
            return;
        }
        if (facing == dir) {
            return;
        }
        facing = dir;
        state = ACTOR_STATE.REFACING;
        animTimer = 0f;
    }

    private void finishMove(){
        state = ACTOR_STATE.STANDING;
        this.placeY = destY;
        this.placeX = destX;
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
        } else if (state == ACTOR_STATE.REFACING) {
            return animations.getWalking(facing).getKeyFrames()[0];
        }
        return animations.getStanding(DIRECTION.EAST);
    }

    @Override
    public float getSizeX() {
        return 1;
    }

    @Override
    public float getSizeY() {
        return 1;
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
    public float getPlaceX() {
        return placeX;
    }

    public float getPlaceY() {
        return placeY;
    }

    public void setState(ACTOR_STATE state) {
        this.state = state;
    }

    public ACTOR_STATE getState() {
        return state;
    }
}

