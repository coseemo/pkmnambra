package com.coseemo.pkmnambra.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventNotifier {
    // Utilizzo di CopyOnWriteArrayList per thread safety e sicurezza durante la modifica
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    // Metodo per registrare un osservatore
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    // Metodo per de-registrare un osservatore
    public void deregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    // Metodo per notificare tutti gli osservatori registrati
    public void notifyObservers(String eventType) {

        for (Observer observer : observers) {
            observer.update(eventType);
        }
    }

    // Metodo per rimuovere tutti gli osservatori, utile per la pulizia completa
    public void clearObservers() {
        observers.clear();
    }
}
