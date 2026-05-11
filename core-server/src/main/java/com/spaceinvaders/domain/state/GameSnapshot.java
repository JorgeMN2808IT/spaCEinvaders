package com.spaceinvaders.domain.state;

/**
 * Representa una fotografía del estado actual del juego.
 *
 * En esta fase contiene el estado del jugador, el estado de la partida
 * y los conteos de entidades almacenadas en listas propias.
 */
public class GameSnapshot {

    private final PlayerState playerState;
    private final MatchStatus matchStatus;

    private final int playerCount;
    private final int alienCount;
    private final int projectileCount;
    private final int bunkerCount;
    private final int ufoCount;

    public GameSnapshot(PlayerState playerState, MatchStatus matchStatus) {
        this(playerState, matchStatus, 1, 0, 0, 0, 0);
    }

    public GameSnapshot(
            PlayerState playerState,
            MatchStatus matchStatus,
            int playerCount,
            int alienCount,
            int projectileCount,
            int bunkerCount,
            int ufoCount
    ) {
        this.playerState = playerState;
        this.matchStatus = matchStatus;
        this.playerCount = playerCount;
        this.alienCount = alienCount;
        this.projectileCount = projectileCount;
        this.bunkerCount = bunkerCount;
        this.ufoCount = ufoCount;
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
}