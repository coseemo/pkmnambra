package com.coseemo.pkmnambra.camera;

public class Camera {
    private float x = 0f, y = 0f;

    // Costruttore che inizializza la posizione della telecamera
    public Camera() {
    }

    // Aggiorno la posizione della telecamera
    public void update(float newX, float newY) {
        this.x = newX;  // Imposto la nuova posizione x
        this.y = newY;  // Imposto la nuova posizione y
    }

    // Restituisco la coordinata x della telecamera
    public float getX() {
        return x;
    }

    // Restituisco la coordinata y della telecamera
    public float getY() {
        return y;
    }
}
