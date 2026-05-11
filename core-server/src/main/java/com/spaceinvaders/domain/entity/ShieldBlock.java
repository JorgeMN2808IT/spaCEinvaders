package com.spaceinvaders.domain.entity;
import com.spaceinvaders.domain.state.BunkerState;

public class ShieldBlock extends GameEntity {

    private int healthPercentage;

    public ShieldBlock(int id, int x, int y) {
        super(id, x, y, 10, 5);
        this.healthPercentage = 100;
    }

    public int getHealthPercentage() {
        return healthPercentage;
    }

    /**
     * Aplica daño al bunker.
     *
     * @param damagePercentage Porcentaje de daño recibido.
     */
    public void applyDamage(int damagePercentage) {
        if (damagePercentage <= 0) {
            return;
        }

        healthPercentage -= damagePercentage;

        if (healthPercentage <= 0) {
            healthPercentage = 0;
            deactivate();
        }
    }

    /**
     */
    public void setHealthPercentage(int healthPercentage) {
        if (healthPercentage < 0) {
            this.healthPercentage = 0;
        } else if (healthPercentage > 100) {
            this.healthPercentage = 100;
        } else {
            this.healthPercentage = healthPercentage;
        }

        if (this.healthPercentage == 0) {
            deactivate();
        }
    }
    public BunkerState toState() {
    return new BunkerState(getId(), x, y, healthPercentage);
}
}