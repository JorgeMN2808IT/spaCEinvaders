package com.spaceinvaders.domain.entity;

/**
 */
public enum AlienType {

    SQUID(10),
    CRAB(20),
    OCTOPUS(40);

    private final int points;

    AlienType(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}