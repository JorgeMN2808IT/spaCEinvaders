package com.spaceinvaders.domain.state;

public class BunkerState {

    private final int id;
    private final int x;
    private final int y;
    private final int health;

    public BunkerState(int id, int x, int y, int health) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }
}