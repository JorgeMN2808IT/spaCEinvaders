package com.spaceinvaders.domain.entity;
import com.spaceinvaders.domain.state.ProjectileState;

public class Projectile extends GameEntity {

    private final ProjectileOwner owner;
    private final int speed;

    public Projectile(int id, ProjectileOwner owner, int x, int y, int speed) {
        super(id, x, y, 1, 2);
        this.owner = owner;
        this.speed = speed;
    }

    public ProjectileOwner getOwner() {
        return owner;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     */
    public void updatePosition() {
        if (owner == ProjectileOwner.PLAYER) {
            y -= speed;
        } else {
            y += speed;
        }
    }

    /**
     * Desactiva el proyectil si sale del área de juego.
     */
    public void deactivateIfOutOfBounds(int minY, int maxY) {
        if (y < minY || y > maxY) {
            deactivate();
        }
    }
    public ProjectileState toState() {
    return new ProjectileState(getId(), x, y, owner);
}
}