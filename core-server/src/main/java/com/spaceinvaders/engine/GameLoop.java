package com.spaceinvaders.engine;

import com.spaceinvaders.config.GameConfig;

public class GameLoop implements Runnable {

    private final MatchController matchController;
    private boolean running;

    public GameLoop(MatchController matchController) {
        this.matchController = matchController;
        this.running = false;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            matchController.updateGame();

            try {
                Thread.sleep(GameConfig.GAME_LOOP_DELAY_MS);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    public void stop() {
        running = false;
    }
}