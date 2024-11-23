package com.coseemo.pkmnambra.captureobserver;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class CaptureEventNotifierTest {

    // Sequenze ANSI per i colori
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    @Test
    void testRegisterAndNotifyObserver() {
        System.out.println(YELLOW + "Eseguendo testRegisterAndNotifyObserver()..." + RESET);

        CaptureEventNotifier notifier = new CaptureEventNotifier();
        AtomicBoolean notified = new AtomicBoolean(false);

        CaptureObserver mockObserver = eventType -> {
            if ("TEST_EVENT".equals(eventType)) {
                notified.set(true);
            }
        };

        notifier.registerObserver(mockObserver);
        notifier.notifyObservers("TEST_EVENT");

        assertTrue(notified.get(), RED + "L'osservatore non è stato notificato correttamente!" + RESET);
        System.out.println(GREEN + "Test completato con successo: testRegisterAndNotifyObserver()" + RESET);
    }

    @Test
    void testDeregisterObserver() {
        System.out.println(YELLOW + "Eseguendo testDeregisterObserver()..." + RESET);

        CaptureEventNotifier notifier = new CaptureEventNotifier();
        AtomicBoolean notified = new AtomicBoolean(false);

        CaptureObserver mockObserver = eventType -> notified.set(true);

        notifier.registerObserver(mockObserver);
        notifier.deregisterObserver(mockObserver);
        notifier.notifyObservers("TEST_EVENT");

        assertFalse(notified.get(), RED + "L'osservatore è stato notificato anche dopo la deregistrazione!" + RESET);
        System.out.println(GREEN + "Test completato con successo: testDeregisterObserver()" + RESET);
    }

    @Test
    void testNotifyMultipleObservers() {
        System.out.println(YELLOW + "Eseguendo testNotifyMultipleObservers()..." + RESET);

        CaptureEventNotifier notifier = new CaptureEventNotifier();
        final int[] notificationCount = {0};

        CaptureObserver observer1 = eventType -> notificationCount[0]++;
        CaptureObserver observer2 = eventType -> notificationCount[0]++;
        CaptureObserver observer3 = eventType -> notificationCount[0]++;

        notifier.registerObserver(observer1);
        notifier.registerObserver(observer2);
        notifier.registerObserver(observer3);

        notifier.notifyObservers("MULTI_EVENT");

        assertEquals(3, notificationCount[0], RED + "Non tutti gli osservatori sono stati notificati!" + RESET);
        System.out.println(GREEN + "Test completato con successo: testNotifyMultipleObservers()" + RESET);
    }

    @Test
    void testClearObservers() {
        System.out.println(YELLOW + "Eseguendo testClearObservers()..." + RESET);

        CaptureEventNotifier notifier = new CaptureEventNotifier();
        AtomicBoolean notified = new AtomicBoolean(false);

        CaptureObserver mockObserver = eventType -> notified.set(true);

        notifier.registerObserver(mockObserver);
        notifier.clearObservers();
        notifier.notifyObservers("CLEAR_EVENT");

        assertFalse(notified.get(), RED + "Un osservatore è stato notificato dopo la pulizia!" + RESET);
        System.out.println(GREEN + "Test completato con successo: testClearObservers()" + RESET);
    }
}
