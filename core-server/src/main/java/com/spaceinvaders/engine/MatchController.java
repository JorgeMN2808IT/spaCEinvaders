package com.spaceinvaders.engine;

import com.spaceinvaders.collections.GameList;
import com.spaceinvaders.domain.entity.PlayerShip;
import com.spaceinvaders.domain.state.Direction;
import com.spaceinvaders.domain.state.GameSnapshot;
import com.spaceinvaders.domain.state.MatchStatus;

/**
 * Controlador principal del estado inicial de la partida.
 *
 * Esta clase será la puerta de entrada para modificar el estado del juego.
 * En fases posteriores coordinará aliens, disparos, bunkers, ovnis y oleadas.
 */
public class MatchController {

    private final GameList<PlayerShip> players;
    private MatchStatus matchStatus;

    public MatchController() {
        this.players = new GameList<>();
        this.matchStatus = MatchStatus.RUNNING;
    }

    /**
     * Registra un jugador si todavía no existe.
     *
     * @param playerId Identificador del jugador.
     */
    public synchronized void registerPlayer(int playerId) {
        PlayerShip existingPlayer = findPlayerById(playerId);

        if (existingPlayer == null) {
            players.add(new PlayerShip(playerId));
        }
    }

    /**
     * Mueve un jugador en la dirección indicada.
     *
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
     * Procesa el disparo de un jugador.
     *
     * En esta fase todavía no crea proyectiles; solamente devuelve el estado.
     * En fases posteriores aquí se agregará la lógica de disparos.
     *
     * @param playerId Identificador del jugador.
     * @return Snapshot actualizado.
     */
    public synchronized GameSnapshot shoot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        return new GameSnapshot(player.toState(), matchStatus, players.size());
    }

    /**
     * Construye un snapshot del estado actual de un jugador.
     *
     * @param playerId Identificador del jugador.
     * @return Snapshot del juego.
     */
    public synchronized GameSnapshot buildSnapshot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        return new GameSnapshot(player.toState(), matchStatus, players.size());
    }

    /**
     * Retorna la cantidad de jugadores registrados.
     *
     * @return Número de jugadores.
     */
    public synchronized int getPlayerCount() {
        return players.size();
    }

    /**
     * Busca un jugador por su identificador.
     *
     * @param playerId Identificador buscado.
     * @return Jugador encontrado o null.
     */
    private PlayerShip findPlayerById(int playerId) {
        return players.find(player -> player.getPlayerId() == playerId);
    }

    /**
     * Obtiene un jugador existente o lo crea si todavía no existe.
     *
     * @param playerId Identificador del jugador.
     * @return Jugador encontrado o creado.
     */
    private PlayerShip getOrCreatePlayer(int playerId) {
        PlayerShip player = findPlayerById(playerId);

        if (player == null) {
            player = new PlayerShip(playerId);
            players.add(player);
        }

        return player;
    }
}