package com.spaceinvaders.config;

/**
 */
public class GameConfig {

    public static final int INITIAL_LIVES = 3;
    public static final int INITIAL_SCORE = 0;

    public static final int INITIAL_PLAYER_X = 50;
    public static final int PLAYER_Y = 90;

    public static final int PLAYER_MOVE_STEP = 5;

    public static final int MIN_X = 0;
    public static final int MAX_X = 100;

    public static final int MIN_Y = 0;
    public static final int MAX_Y = 100;

    public static final int PLAYER_PROJECTILE_SPEED = 4;
    public static final int ALIEN_PROJECTILE_SPEED = 2;

    public static final int INITIAL_ALIEN_SPEED = 2;
    public static final int ALIEN_DOWN_STEP = 5;

    public static final int INITIAL_UFO_SPEED = 3;
    public static final int UFO_Y = 5;

    public static final int BUNKER_Y = 75;
    public static final int BUNKER_INITIAL_HEALTH = 100;

    public static final String INITIAL_MATCH_STATUS = "RUNNING";

    private GameConfig() {
    }
}