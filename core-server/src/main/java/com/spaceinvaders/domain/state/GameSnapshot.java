package com.spaceinvaders.domain.state;

public class GameSnapshot {

    private final PlayerState playerState;
    private final MatchStatus matchStatus;
    private final int playerCount;

    public GameSnapshot(PlayerState playerState, MatchStatus matchStatus) {
        this(playerState, matchStatus, 1);
    }

    public GameSnapshot(PlayerState playerState, MatchStatus matchStatus, int playerCount) {
        this.playerState = playerState;
        this.matchStatus = matchStatus;
        this.playerCount = playerCount;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public int getPlayerCount() {
        return playerCount;
    }
}