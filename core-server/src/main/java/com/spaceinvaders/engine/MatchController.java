package com.spaceinvaders.engine;

import com.spaceinvaders.domain.entity.PlayerShip;
import com.spaceinvaders.domain.state.Direction;
import com.spaceinvaders.domain.state.GameSnapshot;
import com.spaceinvaders.domain.state.MatchStatus;

import java.util.HashMap;
import java.util.Map;

public class MatchController {

    private final Map<Integer, PlayerShip> players;
    private MatchStatus matchStatus;

    public MatchController() {
        this.players = new HashMap<>();
        this.matchStatus = MatchStatus.RUNNING;
    }

    /**
     * @param playerId Identificador del jugador.
     */
    public synchronized void registerPlayer(int playerId) {
        if (!players.containsKey(playerId)) {
            players.put(playerId, new PlayerShip(playerId));
        }
    }

    /**
     * @param playerId Identificador del jugador.
     * @param direction Dirección de movimiento.
     * @return Snapshot actualizado.
     */
    public synchronized GameSnapshot movePlayer(int playerId, Direction direction) {
        PlayerShip player = getOrCreatePlayer(playerId);

        player.move(direction);

        return buildSnapshot(playerId);
    }

    /**
     * @param playerId Identificador del jugador.
     * @return Snapshot actualizado.
     */
    public synchronized GameSnapshot shoot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        return new GameSnapshot(player.toState(), matchStatus);
    }

    /**
     * @param playerId Identificador del jugador.
     * @return Snapshot del juego.
     */
    public synchronized GameSnapshot buildSnapshot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        return new GameSnapshot(player.toState(), matchStatus);
    }

    /**
     * @param playerId Identificador del jugador.
     * @return Jugador encontrado o creado.
     */
    private PlayerShip getOrCreatePlayer(int playerId) {
        if (!players.containsKey(playerId)) {
            players.put(playerId, new PlayerShip(playerId));
        }

        return players.get(playerId);
    }
}