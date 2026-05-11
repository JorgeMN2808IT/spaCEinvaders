package com.spaceinvaders.domain.state;

public class UfoState {

    private final int id;
    private final int x;
    private final int y;
    private final int points;
    private final Direction direction;

    public UfoState(int id, int x, int y, int points, Direction direction) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.points = points;
        this.direction = direction;
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

    public int getPoints() {
        return points;
    }

    public Direction getDirection() {
        return direction;
    }
}