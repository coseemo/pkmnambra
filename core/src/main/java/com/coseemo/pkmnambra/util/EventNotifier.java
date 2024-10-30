package com.coseemo.pkmnambra.util;

import java.util.ArrayList;
import java.util.List;

public class EventNotifier {
    private List<Observer> observers = new ArrayList<>();

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(String eventType) {
        for (Observer observer : observers) {
            observer.update(eventType);
        }
    }
}
