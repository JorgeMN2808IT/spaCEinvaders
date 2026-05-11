package com.spaceinvaders.engine;

import com.spaceinvaders.collections.GameList;
import com.spaceinvaders.config.GameConfig;
import com.spaceinvaders.domain.entity.Projectile;

public class ProjectileSystem {

    /**
     * @param projectiles Lista de proyectiles activos.
     */
    public void updateProjectiles(GameList<Projectile> projectiles) {
        projectiles.forEach(projectile -> {
            if (projectile.isActive()) {
                projectile.updatePosition();
                projectile.deactivateIfOutOfBounds(GameConfig.MIN_Y, GameConfig.MAX_Y);
            }
        });

        removeInactiveProjectiles(projectiles);
    }

    /**
     * @param projectiles Lista de proyectiles.
     */
    private void removeInactiveProjectiles(GameList<Projectile> projectiles) {
        boolean removed;

        do {
            removed = projectiles.remove(projectile -> !projectile.isActive());
        } while (removed);
    }
}