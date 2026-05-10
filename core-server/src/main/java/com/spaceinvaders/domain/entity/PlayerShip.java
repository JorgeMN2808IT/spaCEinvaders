package com.spaceinvaders.domain.entity;

import com.spaceinvaders.config.GameConfig;
import com.spaceinvaders.domain.state.Direction;
import com.spaceinvaders.domain.state.PlayerState;

public class PlayerShip {

    private final int playerId;

    private int x;
    private int y;
    private int lives;
    private int score;

    public PlayerShip(int playerId) {
        this.playerId = playerId;
        this.x = GameConfig.INITIAL_PLAYER_X;
        this.y = GameConfig.PLAYER_Y;
        this.lives = GameConfig.INITIAL_LIVES;
        this.score = GameConfig.INITIAL_SCORE;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    /**
     * Mueve el jugador en la dirección indicada.
     *
     * @param direction Dirección solicitada.
     */
    public void move(Direction direction) {
        if (direction == Direction.LEFT) {
            moveLeft();
        } else if (direction == Direction.RIGHT) {
            moveRight();
        }
    }

    /**
     * Mueve la nave hacia la izquierda sin salir del límite permitido.
     */
    private void moveLeft() {
        x = x - GameConfig.PLAYER_MOVE_STEP;

        if (x < GameConfig.MIN_X) {
            x = GameConfig.MIN_X;
        }
    }

    /**
     * Mueve la nave hacia la derecha sin salir del límite permitido.
     */
    private void moveRight() {
        x = x + GameConfig.PLAYER_MOVE_STEP;

        if (x > GameConfig.MAX_X) {
            x = GameConfig.MAX_X;
        }
    }

    /**
     * Disminuye una vida al jugador.
     */
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    /**
     * Aumenta una vida al jugador.
     */
    public void addLife() {
        lives++;
    }

    /**
     * Agrega puntos al jugador.
     *
     * @param points Puntos obtenidos.
     */
    public void addScore(int points) {
        if (points > 0) {
            score += points;
        }
    }

    /**
     * Retorna una copia del estado actual del jugador.
     *
     * @return Estado del jugador.
     */
    public PlayerState toState() {
        return new PlayerState(playerId, x, y, lives, score);
    }
}