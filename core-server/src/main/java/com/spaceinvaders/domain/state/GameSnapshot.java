package com.spaceinvaders.domain.state;

public class GameSnapshot {

    private final PlayerState playerState;
    private final MatchStatus matchStatus;

    public GameSnapshot(PlayerState playerState, MatchStatus matchStatus) {
        this.playerState = playerState;
        this.matchStatus = matchStatus;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }
}