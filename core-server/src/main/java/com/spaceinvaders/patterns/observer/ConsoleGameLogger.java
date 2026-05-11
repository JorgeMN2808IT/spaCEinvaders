package com.spaceinvaders.patterns.observer;

public class ConsoleGameLogger implements GameListener {

    @Override
    public void onGameEvent(GameEvent event) {
        System.out.println("[Evento Juego] "
                + event.getType()
                + " -> "
                + event.getDescription());
    }
}