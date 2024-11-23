package com.coseemo.pkmnambra.actorobserver;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.coseemo.pkmnambra.actors.Player;
import com.coseemo.pkmnambra.map.Tile;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.maplogic.*;
import com.coseemo.pkmnambra.util.AnimationSet;

import java.util.Objects;

public class Actor implements YSortable {

    private String name;
    private World world;
    private Dialogue dialogue;
    private ActorObserver observer;

    private int x, y; // Coordinate attuali
    private DIRECTION facing; // Direzione del personaggio
    private float worldX, worldY; // Posizione per interpolazione
    private int srcX, srcY, destX, destY; // Coordinate per il movimento
    private float animTimer; // Timer per l'animazione
    private float walkTimer; // Timer per il cammino
    private boolean moveRequestThisFrame; // Richiesta di movimento
    private boolean visible; // Visibilità dell'attore
    private ACTOR_STATE state; // Stato dell'attore
    private AnimationSet animations; // Animazioni dell'attore
    private static final float WALK_TIME_PER_TILE = 0.2f;
    private static final float REFACE_TIME = 0.1f;

    public Actor(String name, World world, int x, int y, AnimationSet animations) {
        this.name = name;
        this.world = world;
        this.observer = world;
        this.x = x;
        this.y = y;
        this.worldX = x;
        this.worldY = y;
        this.animations = animations;
        this.state = ACTOR_STATE.STANDING;
        this.facing = DIRECTION.EAST;
    }

    public Actor() {}

    public enum ACTOR_STATE {
        WALKING,
        STANDING,
        REFACING,
        STILL;
    }

    public boolean move(DIRECTION dir) {
        if (state == ACTOR_STATE.WALKING) {
            if (facing == dir) {
                moveRequestThisFrame = true;
            }
            return false;
        }

        if (!canMove(dir)) {
            observer.attemptedMove(this, dir);
            return false;
        }

        // Notifico prima del movimento
        observer.actorBeforeMoved(this, dir);

        // Controllo del movimento sul tile
        if (!world.getMap().getTile(x + dir.getDx(), y + dir.getDy()).actorBeforeStep(this)) {
            observer.attemptedMove(this, dir);
            return false;
        }

        initializeMove(dir);

        // Rimuovo l'attore dal tile attuale
        world.getMap().getTile(x, y).setActor(null);

        // Aggiorno le coordinate senza controllare eventi
        x += dir.getDx();
        y += dir.getDy();
        world.getMap().getTile(x, y).setActor(this);

        // Notifico il tile del movimento
        world.getMap().getTile(x, y).actorStep(this);

        // Notifico l'osservatore
        observer.actorMoved(this, dir, x, y);

        return true;
    }

    private boolean canMove(DIRECTION dir) {
        if (state == ACTOR_STATE.STILL) {
            return false;
        }

        int targetX = x + dir.getDx();
        int targetY = y + dir.getDy();

        // Verifico se le coordinate sono all'interno dei limiti della mappa
        if (targetX < 0 || targetX >= world.getMap().getWidth() ||
            targetY < 0 || targetY >= world.getMap().getHeight()) {
            return false;
        }

        // Ottengo il tile di destinazione
        Tile targetTile = world.getMap().getTile(targetX, targetY);
        if (targetTile == null) {
            return false;
        }

        // Controllo attori e oggetti sul tile
        return targetTile.getActor() == null &&
            (targetTile.getObject() == null || targetTile.getObject().isWalkable());
    }

    private void initializeMove(DIRECTION dir) {
        if (state == ACTOR_STATE.STILL) return;

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

    private void finishMove() {
        this.worldY = destY;
        this.worldX = destX;

        this.srcX = x;
        this.srcY = y;
        this.destX = x;
        this.destY = y;

        world.getMap().getTile(x, y).actorStep(this);

        state = ACTOR_STATE.STANDING;
        animTimer = 0f;
    }

    public void update(float delta) {
        if (state == ACTOR_STATE.STILL) {
            worldX = x;
            worldY = y;
            return;
        }

        if (state == ACTOR_STATE.WALKING) {
            animTimer += delta;
            walkTimer += delta;

            worldX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);
            worldY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);

            if (animTimer > WALK_TIME_PER_TILE) {
                float leftOverTime = animTimer - WALK_TIME_PER_TILE;
                finishMove();

                if (moveRequestThisFrame) {
                    if (move(facing)) {
                        animTimer += leftOverTime;
                        worldX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);
                        worldY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);
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

    public boolean reface(DIRECTION dir) {
        if (state != ACTOR_STATE.STANDING || facing == dir) return false;

        facing = dir;
        state = ACTOR_STATE.REFACING;
        animTimer = 0f;
        return true;
    }

    public TextureRegion getSprite() {
        if (state == ACTOR_STATE.WALKING) {
            return animations.getWalking(facing).getKeyFrame(walkTimer);
        } else if (state == ACTOR_STATE.STANDING) {
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

    public int getY() {
        return y;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public void setCoords(int targetX, int targetY) {
        this.x = targetX;
        this.y = targetY;
        this.worldX = targetX;
        this.worldY = targetY;

        this.srcX = targetX;
        this.srcY = targetY;
        this.destX = targetX;
        this.destY = targetY;
    }

    public void setState(ACTOR_STATE state) {
        this.state = state;
    }

    public ACTOR_STATE getState() {
        return state;
    }

    public void changeWorld(World world, int newX, int newY) {
        this.world.removeActor(this);
        this.setCoords(newX, newY);

        this.animTimer = 0f;
        this.moveRequestThisFrame = false;

        this.world = world;
        this.world.addPlayer((Player) this);
    }

    public DIRECTION getFacing() {
        return facing;
    }

    public World getWorld() {
        return world;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return getX() == actor.getX() &&
            getY() == actor.getY() &&
            getName().equals(actor.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getName());
    }
}
