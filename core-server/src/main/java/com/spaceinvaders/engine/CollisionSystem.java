package com.spaceinvaders.engine;

import com.spaceinvaders.collections.GameList;
import com.spaceinvaders.config.GameConfig;
import com.spaceinvaders.domain.entity.AlienUnit;
import com.spaceinvaders.domain.entity.PlayerShip;
import com.spaceinvaders.domain.entity.Projectile;
import com.spaceinvaders.domain.entity.ProjectileOwner;
import com.spaceinvaders.domain.entity.ShieldBlock;
import com.spaceinvaders.domain.entity.UfoBonus;


public class CollisionSystem {

    private final ScoreSystem scoreSystem;

    public CollisionSystem(ScoreSystem scoreSystem) {
        this.scoreSystem = scoreSystem;
    }

    /**
     * @param player Jugador actual.
     * @param aliens Lista de aliens.
     * @param projectiles Lista de disparos.
     * @param bunkers Lista de bunkers.
     * @param ufos Lista de ovnis.
     */
    public void processCollisions(
            PlayerShip player,
            GameList<AlienUnit> aliens,
            GameList<Projectile> projectiles,
            GameList<ShieldBlock> bunkers,
            GameList<UfoBonus> ufos
    ) {
        processPlayerProjectiles(player, aliens, projectiles, bunkers, ufos);
        processAlienProjectiles(player, projectiles, bunkers);

        removeInactiveAliens(aliens);
        removeInactiveProjectiles(projectiles);
        removeInactiveBunkers(bunkers);
        removeInactiveUfos(ufos);
    }


    private void processPlayerProjectiles(
            PlayerShip player,
            GameList<AlienUnit> aliens,
            GameList<Projectile> projectiles,
            GameList<ShieldBlock> bunkers,
            GameList<UfoBonus> ufos
    ) {
        projectiles.forEach(projectile -> {
            if (!projectile.isActive() || projectile.getOwner() != ProjectileOwner.PLAYER) {
                return;
            }

            AlienUnit alienHit = aliens.find(alien ->
                    alien.isActive() && projectile.collidesWith(alien)
            );

            if (alienHit != null) {
                alienHit.destroy();
                projectile.deactivate();
                scoreSystem.addAlienScore(player, alienHit);
                return;
            }

            UfoBonus ufoHit = ufos.find(ufo ->
                    ufo.isActive() && projectile.collidesWith(ufo)
            );

            if (ufoHit != null) {
                ufoHit.deactivate();
                projectile.deactivate();
                scoreSystem.addUfoScore(player, ufoHit);
                return;
            }

            ShieldBlock bunkerHit = bunkers.find(bunker ->
                    bunker.isActive() && projectile.collidesWith(bunker)
            );

            if (bunkerHit != null) {
                bunkerHit.applyDamage(GameConfig.BUNKER_DAMAGE_BY_PROJECTILE);
                projectile.deactivate();
            }
        });
    }

    private void processAlienProjectiles(
            PlayerShip player,
            GameList<Projectile> projectiles,
            GameList<ShieldBlock> bunkers
    ) {
        projectiles.forEach(projectile -> {
            if (!projectile.isActive() || projectile.getOwner() != ProjectileOwner.ALIEN) {
                return;
            }

            ShieldBlock bunkerHit = bunkers.find(bunker ->
                    bunker.isActive() && projectile.collidesWith(bunker)
            );

            if (bunkerHit != null) {
                bunkerHit.applyDamage(GameConfig.BUNKER_DAMAGE_BY_PROJECTILE);
                projectile.deactivate();
                return;
            }

            if (projectile.getY() >= GameConfig.PLAYER_Y) {
                player.loseLife();
                projectile.deactivate();
            }
        });
    }

    private void removeInactiveAliens(GameList<AlienUnit> aliens) {
        boolean removed;

        do {
            removed = aliens.remove(alien -> !alien.isActive());
        } while (removed);
    }

    private void removeInactiveProjectiles(GameList<Projectile> projectiles) {
        boolean removed;

        do {
            removed = projectiles.remove(projectile -> !projectile.isActive());
        } while (removed);
    }

    private void removeInactiveBunkers(GameList<ShieldBlock> bunkers) {
        boolean removed;

        do {
            removed = bunkers.remove(bunker -> !bunker.isActive());
        } while (removed);
    }

    private void removeInactiveUfos(GameList<UfoBonus> ufos) {
        boolean removed;

        do {
            removed = ufos.remove(ufo -> !ufo.isActive());
        } while (removed);
    }
}