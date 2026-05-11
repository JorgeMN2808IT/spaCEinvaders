package com.spaceinvaders.engine;

import com.spaceinvaders.domain.entity.AlienUnit;
import com.spaceinvaders.domain.entity.PlayerShip;
import com.spaceinvaders.domain.entity.UfoBonus;

public class ScoreSystem {

    /**
     * @param player Jugador que obtiene los puntos.
     * @param alien Alien destruido.
     */
    public void addAlienScore(PlayerShip player, AlienUnit alien) {
        if (player == null || alien == null) {
            return;
        }

        player.addScore(alien.getPoints());
    }

    /**
     * @param player Jugador que obtiene los puntos.
     * @param ufo Ovni destruido.
     */
    public void addUfoScore(PlayerShip player, UfoBonus ufo) {
        if (player == null || ufo == null) {
            return;
        }

        player.addScore(ufo.getBonusPoints());
    }

    /**

     * @param player Jugador.
     * @param points Puntos.
     */
    public void addBonus(PlayerShip player, int points) {
        if (player == null || points <= 0) {
            return;
        }

        player.addScore(points);
    }
}