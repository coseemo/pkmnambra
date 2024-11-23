package com.coseemo.pkmnambra.captureobserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaptureEventNotifier {
    // Utilizzo di CopyOnWriteArrayList per thread safety e sicurezza durante la modifica
    private final List<CaptureObserver> observers = new CopyOnWriteArrayList<>();

    // Metodo per registrare un osservatore
    public void registerObserver(CaptureObserver observer) {
        observers.add(observer);
    }

    // Metodo per de-registrare un osservatore
    public void deregisterObserver(CaptureObserver observer) {
        observers.remove(observer);
    }

    // Metodo per notificare tutti gli osservatori registrati
    public void notifyObservers(String eventType) {

        for (CaptureObserver observer : observers) {
            observer.update(eventType);
        }
    }

    // Metodo per rimuovere tutti gli osservatori, utile per la pulizia completa
    public void clearObservers() {
        observers.clear();
    }
}
