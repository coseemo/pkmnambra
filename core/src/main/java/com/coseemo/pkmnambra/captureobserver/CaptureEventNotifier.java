package com.coseemo.pkmnambra.captureobserver;

import java.util.ArrayList;
import java.util.List;

public class CaptureEventNotifier {
    private final List<CaptureObserver> observers = new ArrayList<>();

    // Registra un osservatore
    public void registerObserver(CaptureObserver observer) {
        observers.add(observer);
    }

    // Deregistra un osservatore
    public void deregisterObserver(CaptureObserver observer) {
        observers.remove(observer);
    }

    // Notifica tutti gli osservatori registrati
    public void notifyObservers(String eventType) {
        for (CaptureObserver observer : observers) {
            observer.update(eventType);
        }
    }

    public void update() {

    }

    // Pulisce la lista degli osservatori
    public void clearObservers() {
        observers.clear();
    }
}
