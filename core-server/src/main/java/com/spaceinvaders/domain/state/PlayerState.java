package com.spaceinvaders.domain.state;

public class PlayerState {

    private final int playerId;
    private final int x;
    private final int y;
    private final int lives;
    private final int score;

    public PlayerState(int playerId, int x, int y, int lives, int score) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.lives = lives;
        this.score = score;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }
}