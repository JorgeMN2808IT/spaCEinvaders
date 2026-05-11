package com.spaceinvaders.engine;

import com.spaceinvaders.collections.GameList;
import com.spaceinvaders.config.GameConfig;
import com.spaceinvaders.domain.entity.AlienType;
import com.spaceinvaders.domain.entity.AlienUnit;
import com.spaceinvaders.domain.entity.PlayerShip;
import com.spaceinvaders.domain.entity.Projectile;
import com.spaceinvaders.domain.entity.ProjectileOwner;
import com.spaceinvaders.domain.entity.ShieldBlock;
import com.spaceinvaders.domain.entity.UfoBonus;
import com.spaceinvaders.domain.state.Direction;
import com.spaceinvaders.domain.state.GameSnapshot;
import com.spaceinvaders.domain.state.MatchStatus;

/**
 * Controlador principal del estado inicial de la partida.
 *
 * Esta clase coordina jugadores y entidades principales del juego.
 */
public class MatchController {

    private final GameList<PlayerShip> players;
    private final GameList<AlienUnit> aliens;
    private final GameList<Projectile> projectiles;
    private final GameList<ShieldBlock> bunkers;
    private final GameList<UfoBonus> ufos;

    private MatchStatus matchStatus;

    private int nextEntityId;

    public MatchController() {
        this.players = new GameList<>();
        this.aliens = new GameList<>();
        this.projectiles = new GameList<>();
        this.bunkers = new GameList<>();
        this.ufos = new GameList<>();

        this.matchStatus = MatchStatus.RUNNING;
        this.nextEntityId = 1;

        createInitialBunkers();
        createInitialAlienWave();
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
     * Crea un disparo del jugador.
     *
     * @param playerId Identificador del jugador.
     * @return Snapshot actualizado.
     */
    public synchronized GameSnapshot shoot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        Projectile projectile = new Projectile(
                generateEntityId(),
                ProjectileOwner.PLAYER,
                player.getX(),
                GameConfig.PLAYER_Y - 2,
                GameConfig.PLAYER_PROJECTILE_SPEED
        );

        projectiles.add(projectile);

        return buildSnapshot(playerId);
    }

    /**
     * Crea un alien de forma manual.
     *
     * Este método servirá después para procesar mensajes tipo:
     * SPC|SPAWN_ALIEN|x=10|y=20|type=SQUID
     */
    public synchronized void createAlien(AlienType type, int x, int y) {
        aliens.add(new AlienUnit(generateEntityId(), type, x, y));
    }

    /**
     * Crea un ovni de bonificación.
     */
    public synchronized void createUfo(int x, int points, Direction direction) {
        UfoBonus ufo = new UfoBonus(
                generateEntityId(),
                x,
                GameConfig.UFO_Y,
                points,
                direction,
                GameConfig.INITIAL_UFO_SPEED
        );

        ufos.add(ufo);
    }

    /**
     * Cambia el estado de todos los bunkers.
     *
     * @param healthPercentage Nuevo porcentaje de salud.
     */
    public synchronized void updateAllBunkers(int healthPercentage) {
        bunkers.forEach(bunker -> bunker.setHealthPercentage(healthPercentage));
    }

    /**
     * Construye un snapshot del estado actual de un jugador.
     *
     * @param playerId Identificador del jugador.
     * @return Snapshot del juego.
     */
    public synchronized GameSnapshot buildSnapshot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        return new GameSnapshot(
                player.toState(),
                matchStatus,
                players.size(),
                aliens.size(),
                projectiles.size(),
                bunkers.size(),
                ufos.size()
        );
    }

    public synchronized int getPlayerCount() {
        return players.size();
    }

    public synchronized int getAlienCount() {
        return aliens.size();
    }

    public synchronized int getProjectileCount() {
        return projectiles.size();
    }

    public synchronized int getBunkerCount() {
        return bunkers.size();
    }

    public synchronized int getUfoCount() {
        return ufos.size();
    }

    /**
     * Crea bunkers iniciales.
     */
    private void createInitialBunkers() {
        bunkers.add(new ShieldBlock(generateEntityId(), 15, GameConfig.BUNKER_Y));
        bunkers.add(new ShieldBlock(generateEntityId(), 35, GameConfig.BUNKER_Y));
        bunkers.add(new ShieldBlock(generateEntityId(), 60, GameConfig.BUNKER_Y));
        bunkers.add(new ShieldBlock(generateEntityId(), 80, GameConfig.BUNKER_Y));
    }

    /**
     * Crea una oleada inicial reducida de aliens.
     *
     * Más adelante esta lógica se moverá a WaveSystem.
     */
    private void createInitialAlienWave() {
        int startX = 10;
        int startY = 15;
        int separationX = 8;
        int separationY = 6;

        for (int row = 0; row < 3; row++) {
            AlienType type = getAlienTypeByRow(row);

            for (int column = 0; column < 5; column++) {
                int x = startX + (column * separationX);
                int y = startY + (row * separationY);

                createAlien(type, x, y);
            }
        }
    }

    private AlienType getAlienTypeByRow(int row) {
        if (row == 0) {
            return AlienType.SQUID;
        }

        if (row == 1) {
            return AlienType.CRAB;
        }

        return AlienType.OCTOPUS;
    }

    private PlayerShip findPlayerById(int playerId) {
        return players.find(player -> player.getPlayerId() == playerId);
    }

    private PlayerShip getOrCreatePlayer(int playerId) {
        PlayerShip player = findPlayerById(playerId);

        if (player == null) {
            player = new PlayerShip(playerId);
            players.add(player);
        }

        return player;
    }

    private int generateEntityId() {
        int generatedId = nextEntityId;
        nextEntityId++;
        return generatedId;
    }
}