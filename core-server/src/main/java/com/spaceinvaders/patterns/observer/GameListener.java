package com.spaceinvaders.patterns.observer;

public interface GameListener {

    void onGameEvent(GameEvent event);
}