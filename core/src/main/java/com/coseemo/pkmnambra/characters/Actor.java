package com.coseemo.pkmnambra.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.coseemo.pkmnambra.dialogue.Dialogue;
import com.coseemo.pkmnambra.maplogic.DIRECTION;
import com.coseemo.pkmnambra.maplogic.YSortable;
import com.coseemo.pkmnambra.maplogic.TileMap;
import com.coseemo.pkmnambra.maplogic.Place;
import com.coseemo.pkmnambra.util.AnimationSet;

public class Actor implements YSortable {
    private Place place;
    private TileMap map;
    private Dialogue dialogue;
    private int x, y; // Coordinate attuali
    private DIRECTION facing; // Direzione del personaggio
    private float placeX, placeY; // Posizione per interpolazione
    private int srcX, srcY, destX, destY; // Coordinate per il movimento
    private float animTimer; // Timer per l'animazione
    private final float WALK_TIME_PER_TILE = 0.2f;
    private final float REFACE_TIME = 0.1f;
    private float walkTimer;
    private boolean moveRequestThisFrame; // Richiesta di movimento
    private AnimationSet animations;
    private ACTOR_STATE state;

    public Actor(Place place, int x, int y, AnimationSet animations) {
        this.place = place;
        this.map = place.getMap();
        this.placeX = x;
        this.placeY = y;
        this.animations = animations;
        map.getTile(x, y).setActor(this);
        this.x = x;
        this.y = y;
        this.state = ACTOR_STATE.STANDING;
        this.facing = DIRECTION.EAST;
    }

    public Actor() {
    }

    public enum ACTOR_STATE {
        WALKING,
        STANDING,
        REFACING
    }

    public boolean move(DIRECTION dir) {
        if (state == ACTOR_STATE.WALKING) {
            if (facing == dir) moveRequestThisFrame = true;
            return false;
        }

        if (!canMove(dir)) return false;

        initializeMove(dir);

        // Remove actor from current tile
        map.getTile(x, y).setActor(null);

        // Update coordinates but don't check events yet
        x += dir.getDx();
        y += dir.getDy();
        map.getTile(x, y).setActor(this);

        return true;
    }

    private boolean canMove(DIRECTION dir) {
        int targetX = x + dir.getDx();
        int targetY = y + dir.getDy();

        return !(targetX < 0 || targetX >= map.getWidth() || targetY < 0 || targetY >= map.getHeight() ||
            map.getTile(targetX, targetY).getActor() != null ||
            (map.getTile(targetX, targetY).getObject() != null &&
                !map.getTile(targetX, targetY).getObject().isWalkable()));
    }

    private void initializeMove(DIRECTION dir) {
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

    private boolean playerHasMoved() {
        return placeX != x || placeY != y;
    }

    private void finishMove() {
        // Update position first
        this.placeY = destY;
        this.placeX = destX;
        this.srcY = 0;
        this.srcX = 0;
        this.destY = 0;
        this.destX = 0;

        // Then change state to standing
        state = ACTOR_STATE.STANDING;

    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    protected void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public void update(float delta) {
        if (state == ACTOR_STATE.WALKING) {
            animTimer += delta;
            walkTimer += delta;

            placeX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);
            placeY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);

            if (animTimer > WALK_TIME_PER_TILE) {
                float leftOverTime = animTimer - WALK_TIME_PER_TILE;
                finishMove();
                if (moveRequestThisFrame) {
                    if (move(facing)) {
                        animTimer += leftOverTime;
                        placeX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);
                        placeY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);
                    }
                } else {
                    walkTimer = 0f;
                }
            }
            System.out.println("Player current position: (" + getPlaceX() + ", " + getPlaceY() + ")");
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

    public Place getPlace() {
        return place;
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

    public void setCoords(int targetX, int targetY) {
        this.x = targetX;
        this.y = targetY;
        this.placeX = x;
        this.placeY = y;
    }

    public void setMap(TileMap map) {
        this.map = map;
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

    public void changePlace(Place world, int newX, int newY) {
        this.place.removeActor(this);
        this.place = world;
        this.map = place.getMap();
        this.setCoords(newX, newY);
        this.place.addActor(this);
    }
    public void cancelMove() {
        // Arrotonda per difetto la posizione corrente
        this.placeX = (float) Math.floor(this.placeX);
        this.placeY = (float) Math.floor(this.placeY);

        // Allinea le coordinate logiche alla posizione attuale arrotondata
        this.x = (int) placeX;
        this.y = (int) placeY;

        // Rimuove il personaggio dalla posizione prevista, se necessario
        if (destX != x || destY != y) { // Verifica se c'è una posizione prevista diversa
            map.getTile(destX, destY).setActor(null);
        }

        // Cancella eventuali movimenti in corso
        this.destX = this.x;
        this.destY = this.y;

        // Reimposta il timer e lo stato
        this.animTimer = 0f;
        this.walkTimer = 0f;
        this.moveRequestThisFrame = false;
        this.state = ACTOR_STATE.STANDING;

        // Aggiorna la posizione effettiva della tile
        map.getTile(x, y).setActor(this);

        System.out.println("Move cancelled. Player position reset to (" + getX() + ", " + getY() + ")");
    }

    public DIRECTION getFacing() {
        return facing;
    }

    public void interactWithPlayer(Player player){}

}
