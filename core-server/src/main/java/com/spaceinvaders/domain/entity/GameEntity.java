package com.spaceinvaders.domain.entity;

/**
 */
public abstract class GameEntity implements Identifiable {

    private final int id;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean active;

    public GameEntity(int id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Verifica colisión rectangular simple entre dos entidades.
     *
     * @param other Otra entidad del juego.
     * @return true si ambas entidades se intersectan.
     */
    public boolean collidesWith(GameEntity other) {
        if (other == null || !active || !other.isActive()) {
            return false;
        }

        return x < other.getX() + other.getWidth()
                && x + width > other.getX()
                && y < other.getY() + other.getHeight()
                && y + height > other.getY();
    }
}