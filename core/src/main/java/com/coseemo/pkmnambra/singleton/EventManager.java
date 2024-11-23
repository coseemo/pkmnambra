package com.coseemo.pkmnambra.singleton;
import com.coseemo.pkmnambra.captureobserver.CaptureEventNotifier;

public class EventManager {
    private final CaptureEventNotifier eventNotifier;

    public EventManager() {
        this.eventNotifier = new CaptureEventNotifier();
    }

    public CaptureEventNotifier getEventNotifier() {
        return eventNotifier;
    }
}

