package com.coseemo.pkmnambra.util.states;
import com.coseemo.pkmnambra.util.EventNotifier;

public class EventManager {
    private final EventNotifier eventNotifier;

    public EventManager() {
        this.eventNotifier = new EventNotifier();
    }

    public EventNotifier getEventNotifier() {
        return eventNotifier;
    }
}

