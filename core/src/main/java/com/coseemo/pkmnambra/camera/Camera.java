package com.coseemo.pkmnambra.camera;

public class Camera {
    float x = 0f,y = 0f;

    public Camera() {
    }

    public void update(float newX, float newY){
        this.x = newX;
        this.y = newY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
