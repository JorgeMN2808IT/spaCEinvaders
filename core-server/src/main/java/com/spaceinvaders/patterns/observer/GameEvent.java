package com.spaceinvaders.patterns.observer;

public class GameEvent {

    private final GameEventType type;
    private final String description;

    public GameEvent(GameEventType type, String description) {
        this.type = type;
        this.description = description;
    }

    public GameEventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}