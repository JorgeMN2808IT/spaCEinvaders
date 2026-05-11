package com.spaceinvaders.engine;

import com.spaceinvaders.collections.GameList;
import com.spaceinvaders.config.GameConfig;
import com.spaceinvaders.domain.entity.AlienUnit;
import com.spaceinvaders.domain.entity.UfoBonus;

public class AlienMovementSystem {

    private int alienDirection;
    private int alienSpeed;

    public AlienMovementSystem() {
        this.alienDirection = 1;
        this.alienSpeed = GameConfig.INITIAL_ALIEN_SPEED;
    }

    /**
     * @param aliens Lista de aliens.
     */
    public void updateAliens(GameList<AlienUnit> aliens) {
        if (aliens.isEmpty()) {
            return;
        }

        boolean mustMoveDown = shouldMoveDown(aliens);

        aliens.forEach(alien -> {
            if (alien.isActive()) {
                if (mustMoveDown) {
                    alien.moveDown(GameConfig.ALIEN_DOWN_STEP);
                } else {
                    alien.moveHorizontal(alienDirection * alienSpeed);
                }
            }
        });

        if (mustMoveDown) {
            alienDirection *= -1;
        }
    }

    /**
     * @param ufos Lista de ovnis.
     */
    public void updateUfos(GameList<UfoBonus> ufos) {
        ufos.forEach(ufo -> {
            if (ufo.isActive()) {
                ufo.updatePosition();
                ufo.deactivateIfOutOfBounds(GameConfig.MIN_X, GameConfig.MAX_X);
            }
        });

        removeInactiveUfos(ufos);
    }
    public void increaseAlienSpeed() {
        alienSpeed++;
    }

    /**
     * @param alienSpeed Nueva velocidad.
     */
    public void setAlienSpeed(int alienSpeed) {
        if (alienSpeed > 0) {
            this.alienSpeed = alienSpeed;
        }
    }

    public int getAlienSpeed() {
        return alienSpeed;
    }

    /**
     * @param aliens Lista de aliens.
     * @return true si algún alien toca un borde.
     */
    private boolean shouldMoveDown(GameList<AlienUnit> aliens) {
        final boolean[] result = {false};

        aliens.forEach(alien -> {
            if (!alien.isActive()) {
                return;
            }

            int nextX = alien.getX() + (alienDirection * alienSpeed);

            if (nextX <= GameConfig.MIN_X || nextX + alien.getWidth() >= GameConfig.MAX_X) {
                result[0] = true;
            }
        });

        return result[0];
    }

    private void removeInactiveUfos(GameList<UfoBonus> ufos) {
        boolean removed;

        do {
            removed = ufos.remove(ufo -> !ufo.isActive());
        } while (removed);
    }
}