package com.spaceinvaders.domain.state;

import com.spaceinvaders.collections.GameList;

/**
 * Representa una fotografía del estado actual del juego.
 *
 * Contiene el estado del jugador principal y las entidades visibles del juego.
 */
public class GameSnapshot {

    private final PlayerState playerState;
    private final MatchStatus matchStatus;

    private final int playerCount;
    private final int alienCount;
    private final int projectileCount;
    private final int bunkerCount;
    private final int ufoCount;

    private final GameList<PlayerState> playerStates;
    private final GameList<AlienState> alienStates;
    private final GameList<ProjectileState> projectileStates;
    private final GameList<BunkerState> bunkerStates;
    private final GameList<UfoState> ufoStates;

    public GameSnapshot(PlayerState playerState, MatchStatus matchStatus) {
        this(
                playerState,
                matchStatus,
                1,
                0,
                0,
                0,
                0,
                new GameList<>(),
                new GameList<>(),
                new GameList<>(),
                new GameList<>(),
                new GameList<>()
        );
    }

    public GameSnapshot(
            PlayerState playerState,
            MatchStatus matchStatus,
            int playerCount,
            int alienCount,
            int projectileCount,
            int bunkerCount,
            int ufoCount,
            GameList<PlayerState> playerStates,
            GameList<AlienState> alienStates,
            GameList<ProjectileState> projectileStates,
            GameList<BunkerState> bunkerStates,
            GameList<UfoState> ufoStates
    ) {
        this.playerState = playerState;
        this.matchStatus = matchStatus;
        this.playerCount = playerCount;
        this.alienCount = alienCount;
        this.projectileCount = projectileCount;
        this.bunkerCount = bunkerCount;
        this.ufoCount = ufoCount;
        this.playerStates = playerStates;
        this.alienStates = alienStates;
        this.projectileStates = projectileStates;
        this.bunkerStates = bunkerStates;
        this.ufoStates = ufoStates;
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

    public int getAlienCount() {
        return alienCount;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public int getBunkerCount() {
        return bunkerCount;
    }

    public int getUfoCount() {
        return ufoCount;
    }

    public GameList<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public GameList<AlienState> getAlienStates() {
        return alienStates;
    }

    public GameList<ProjectileState> getProjectileStates() {
        return projectileStates;
    }

    public GameList<BunkerState> getBunkerStates() {
        return bunkerStates;
    }

    public GameList<UfoState> getUfoStates() {
        return ufoStates;
    }
}