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
import com.spaceinvaders.domain.state.PlayerState;
import com.spaceinvaders.patterns.factory.DefaultEntityFactory;
import com.spaceinvaders.patterns.factory.EntityFactory;
import com.spaceinvaders.patterns.observer.ConsoleGameLogger;
import com.spaceinvaders.patterns.observer.GameEvent;
import com.spaceinvaders.patterns.observer.GameEventBus;
import com.spaceinvaders.patterns.observer.GameEventType;
import com.spaceinvaders.domain.state.AlienState;
import com.spaceinvaders.domain.state.ProjectileState;
import com.spaceinvaders.domain.state.BunkerState;
import com.spaceinvaders.domain.state.UfoState;

public class MatchController {

    private final GameList<PlayerShip> players;
    private final GameList<AlienUnit> aliens;
    private final GameList<Projectile> projectiles;
    private final GameList<ShieldBlock> bunkers;
    private final GameList<UfoBonus> ufos;

    private final EntityFactory entityFactory;
    private final GameEventBus eventBus;

    private final ProjectileSystem projectileSystem;
    private final AlienMovementSystem alienMovementSystem;
    private final ScoreSystem scoreSystem;
    private final CollisionSystem collisionSystem;

    private MatchStatus matchStatus;

    private int nextEntityId;

    public MatchController() {
        this.players = new GameList<>();
        this.aliens = new GameList<>();
        this.projectiles = new GameList<>();
        this.bunkers = new GameList<>();
        this.ufos = new GameList<>();

        this.entityFactory = new DefaultEntityFactory();
        this.eventBus = new GameEventBus();
        this.eventBus.register(new ConsoleGameLogger());

        this.projectileSystem = new ProjectileSystem();
        this.alienMovementSystem = new AlienMovementSystem();
        this.scoreSystem = new ScoreSystem();
        this.collisionSystem = new CollisionSystem(scoreSystem);

        this.matchStatus = MatchStatus.RUNNING;
        this.nextEntityId = 1;

        createInitialBunkers();
        createInitialAlienWave();
    }

    /**
     * @param playerId Identificador del jugador.
     */
    public synchronized void registerPlayer(int playerId) {
        PlayerShip existingPlayer = findPlayerById(playerId);

        if (existingPlayer == null) {
            players.add(new PlayerShip(playerId));

            eventBus.notify(new GameEvent(
                    GameEventType.PLAYER_REGISTERED,
                    "Jugador registrado con id " + playerId
            ));
        }
    }

    /**
     * @param playerId Identificador del jugador.
     * @param direction Dirección de movimiento.
     * @return Snapshot actualizado.
     */
    public synchronized GameSnapshot movePlayer(int playerId, Direction direction) {
        if (matchStatus == MatchStatus.GAME_OVER) {
            return buildSnapshot(playerId);
        }

        PlayerShip player = getOrCreatePlayer(playerId);

        player.move(direction);

        eventBus.notify(new GameEvent(
                GameEventType.PLAYER_MOVED,
                "Jugador " + playerId + " movido hacia " + direction
        ));

        return buildSnapshot(playerId);
    }

    /**
     * @param playerId Identificador del jugador.
     * @return Snapshot actualizado.
     */
    public synchronized GameSnapshot shoot(int playerId) {
        if (matchStatus == MatchStatus.GAME_OVER) {
            return buildSnapshot(playerId);
        }

        PlayerShip player = getOrCreatePlayer(playerId);

        Projectile projectile = entityFactory.createProjectile(
                generateEntityId(),
                ProjectileOwner.PLAYER,
                player.getX(),
                GameConfig.PLAYER_Y - 2,
                GameConfig.PLAYER_PROJECTILE_SPEED
        );

        projectiles.add(projectile);

        eventBus.notify(new GameEvent(
                GameEventType.PLAYER_SHOT,
                "Jugador " + playerId + " disparo un proyectil"
        ));

        return buildSnapshot(playerId);
    }

    public synchronized void updateGame() {
        if (matchStatus == MatchStatus.GAME_OVER) {
            return;
        }

        projectileSystem.updateProjectiles(projectiles);
        alienMovementSystem.updateAliens(aliens);
        alienMovementSystem.updateUfos(ufos);

        PlayerShip firstPlayer = getFirstPlayer();

        if (firstPlayer != null) {
            collisionSystem.processCollisions(
                    firstPlayer,
                    aliens,
                    projectiles,
                    bunkers,
                    ufos
            );

            verifyPlayerLives(firstPlayer);
        }

        verifyAlienArrival();
        verifyWaveCleared(firstPlayer);

        eventBus.notify(new GameEvent(
                GameEventType.GAME_UPDATED,
                "Motor del juego actualizado"
        ));
    }

    public synchronized void createAlien(AlienType type, int x, int y) {
        AlienUnit alien = entityFactory.createAlien(
                generateEntityId(),
                type,
                x,
                y
        );

        aliens.add(alien);

        eventBus.notify(new GameEvent(
                GameEventType.ALIEN_CREATED,
                "Alien " + type + " creado en (" + x + "," + y + ")"
        ));
    }

    /**
     * Crea un ovni de bonificación.
     */
    public synchronized void createUfo(int x, int points, Direction direction) {
        UfoBonus ufo = entityFactory.createUfo(
                generateEntityId(),
                x,
                GameConfig.UFO_Y,
                points,
                direction,
                GameConfig.INITIAL_UFO_SPEED
        );

        ufos.add(ufo);

        eventBus.notify(new GameEvent(
                GameEventType.UFO_CREATED,
                "OVNI creado con " + points + " puntos"
        ));
    }

    /**
     * @param healthPercentage Nuevo porcentaje de salud.
     */
    public synchronized void updateAllBunkers(int healthPercentage) {
        bunkers.forEach(bunker -> bunker.setHealthPercentage(healthPercentage));

        eventBus.notify(new GameEvent(
                GameEventType.BUNKERS_UPDATED,
                "Bunkers actualizados a " + healthPercentage + "%"
        ));
    }
    private GameList<AlienState> buildAlienStates() {
    GameList<AlienState> states = new GameList<>();

    aliens.forEach(alien -> {
        if (alien.isActive()) {
            states.add(alien.toState());
        }
    });

    return states;
}

    private GameList<ProjectileState> buildProjectileStates() {
        GameList<ProjectileState> states = new GameList<>();

        projectiles.forEach(projectile -> {
            if (projectile.isActive()) {
                states.add(projectile.toState());
            }
        });

        return states;
    }

    private GameList<BunkerState> buildBunkerStates() {
        GameList<BunkerState> states = new GameList<>();

        bunkers.forEach(bunker -> {
            if (bunker.isActive()) {
                states.add(bunker.toState());
            }
        });

        return states;
    }

    private GameList<UfoState> buildUfoStates() {
        GameList<UfoState> states = new GameList<>();

        ufos.forEach(ufo -> {
            if (ufo.isActive()) {
                states.add(ufo.toState());
            }
        });

        return states;
    }

    /**
     * @param playerId Identificador del jugador.
     * @return Snapshot del juego.
     */
    public synchronized GameSnapshot buildSnapshot(int playerId) {
        PlayerShip player = getOrCreatePlayer(playerId);

        GameList<PlayerState> playerStates = buildPlayerStates();
        GameList<AlienState> alienStates = buildAlienStates();
        GameList<ProjectileState> projectileStates = buildProjectileStates();
        GameList<BunkerState> bunkerStates = buildBunkerStates();
        GameList<UfoState> ufoStates = buildUfoStates();

        GameSnapshot snapshot = new GameSnapshot(
                player.toState(),
                matchStatus,
                players.size(),
                aliens.size(),
                projectiles.size(),
                bunkers.size(),
                ufos.size(),
                playerStates,
                alienStates,
                projectileStates,
                bunkerStates,
                ufoStates
        );

        eventBus.notify(new GameEvent(
                GameEventType.SNAPSHOT_CREATED,
                "Snapshot generado para jugador " + playerId
        ));

        return snapshot;
    }
    private GameList<PlayerState> buildPlayerStates() {
        GameList<PlayerState> states = new GameList<>();

        players.forEach(player -> states.add(player.toState()));

        return states;
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

    private void createInitialBunkers() {
        bunkers.add(entityFactory.createBunker(generateEntityId(), 15, GameConfig.BUNKER_Y));
        bunkers.add(entityFactory.createBunker(generateEntityId(), 35, GameConfig.BUNKER_Y));
        bunkers.add(entityFactory.createBunker(generateEntityId(), 60, GameConfig.BUNKER_Y));
        bunkers.add(entityFactory.createBunker(generateEntityId(), 80, GameConfig.BUNKER_Y));
    }

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

    private void verifyPlayerLives(PlayerShip player) {
        if (player.getLives() <= 0) {
            matchStatus = MatchStatus.GAME_OVER;

            eventBus.notify(new GameEvent(
                    GameEventType.GAME_OVER,
                    "El jugador se quedo sin vidas"
            ));
        }
    }

    private void verifyAlienArrival() {
        final boolean[] alienReachedPlayer = {false};

        aliens.forEach(alien -> {
            if (alien.isActive() && alien.getY() + alien.getHeight() >= GameConfig.PLAYER_Y) {
                alienReachedPlayer[0] = true;
            }
        });

        if (alienReachedPlayer[0]) {
            matchStatus = MatchStatus.GAME_OVER;

            eventBus.notify(new GameEvent(
                    GameEventType.GAME_OVER,
                    "Los aliens llegaron al canon del jugador"
            ));
        }
    }

    private void verifyWaveCleared(PlayerShip player) {
        if (aliens.isEmpty() && matchStatus != MatchStatus.GAME_OVER) {
            if (player != null) {
                player.addLife();
                scoreSystem.addBonus(player, GameConfig.SCORE_PER_WAVE);
            }

            alienMovementSystem.increaseAlienSpeed();
            createInitialAlienWave();

            eventBus.notify(new GameEvent(
                    GameEventType.ALIENS_CLEARED,
                    "Oleada completada. Se crea una nueva oleada con mayor velocidad"
            ));
        }
    }

    private PlayerShip findPlayerById(int playerId) {
        return players.find(player -> player.getPlayerId() == playerId);
    }

    private PlayerShip getOrCreatePlayer(int playerId) {
        PlayerShip player = findPlayerById(playerId);

        if (player == null) {
            player = new PlayerShip(playerId);
            players.add(player);

            eventBus.notify(new GameEvent(
                    GameEventType.PLAYER_REGISTERED,
                    "Jugador creado automaticamente con id " + playerId
            ));
        }

        return player;
    }

    private PlayerShip getFirstPlayer() {
        if (players.isEmpty()) {
            return null;
        }

        return players.get(0);
    }

    private int generateEntityId() {
        int generatedId = nextEntityId;
        nextEntityId++;
        return generatedId;
    }
}