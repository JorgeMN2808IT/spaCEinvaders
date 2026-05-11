package com.spaceinvaders.domain.state;

import com.spaceinvaders.domain.entity.AlienType;

public class AlienState {

    private final int id;
    private final int x;
    private final int y;
    private final AlienType type;
    private final int points;

    public AlienState(int id, int x, int y, AlienType type, int points) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.points = points;
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

    public AlienType getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }
}