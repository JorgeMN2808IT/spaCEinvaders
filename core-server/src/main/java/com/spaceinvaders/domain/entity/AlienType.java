package com.spaceinvaders.domain.entity;

/**
 * Tipos de extraterrestres disponibles en el juego.
 *
 * Cada tipo tiene una puntuación asociada según la descripción del proyecto.
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