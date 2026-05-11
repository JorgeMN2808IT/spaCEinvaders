package com.spaceinvaders.domain.entity;

import com.spaceinvaders.domain.state.Direction;
import com.spaceinvaders.domain.state.UfoState;

public class UfoBonus extends GameEntity {

    private final int bonusPoints;
    private final Direction direction;
    private final int speed;

    public UfoBonus(int id, int x, int y, int bonusPoints, Direction direction, int speed) {
        super(id, x, y, 6, 3);
        this.bonusPoints = bonusPoints;
        this.direction = direction;
        this.speed = speed;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void updatePosition() {
        if (direction == Direction.LEFT) {
            x -= speed;
        } else {
            x += speed;
        }
    }

    public void deactivateIfOutOfBounds(int minX, int maxX) {
        if (x < minX || x > maxX) {
            deactivate();
        }
    }
    public UfoState toState() {
    return new UfoState(getId(), x, y, bonusPoints, direction);
    }
}