package com.spaceinvaders.patterns.observer;

import com.spaceinvaders.collections.GameList;

public class GameEventBus {

    private final GameList<GameListener> listeners;

    public GameEventBus() {
        this.listeners = new GameList<>();
    }

    /**
     * @param listener Observador a registrar.
     */
    public void register(GameListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * @param event Evento ocurrido.
     */
    public void notify(GameEvent event) {
        listeners.forEach(listener -> listener.onGameEvent(event));
    }
}