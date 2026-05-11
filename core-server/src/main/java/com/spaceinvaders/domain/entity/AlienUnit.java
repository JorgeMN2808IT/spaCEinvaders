package com.spaceinvaders.domain.entity;
import com.spaceinvaders.domain.state.AlienState;



public class AlienUnit extends GameEntity {

    private final AlienType alienType;
    private final int points;

    public AlienUnit(int id, AlienType alienType, int x, int y) {
        super(id, x, y, 4, 3);
        this.alienType = alienType;
        this.points = alienType.getPoints();
    }

    public AlienType getAlienType() {
        return alienType;
    }

    public int getPoints() {
        return points;
    }

    public void moveHorizontal(int deltaX) {
        this.x += deltaX;
    }

    public void moveDown(int deltaY) {
        this.y += deltaY;
    }

    public void destroy() {
        deactivate();
    }
    public AlienState toState() {
    return new AlienState(getId(), x, y, alienType, points);
}
}

