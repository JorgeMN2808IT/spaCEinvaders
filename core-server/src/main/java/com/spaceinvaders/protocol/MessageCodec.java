package com.spaceinvaders.protocol;

import com.spaceinvaders.domain.state.GameSnapshot;
import com.spaceinvaders.domain.state.PlayerState;

public class MessageCodec {

    private static final String PREFIX = "SPC";

    /**
     * @param rawMessage Mensaje recibido desde un cliente.
     * @return true si el mensaje inicia con el prefijo SPC.
     */
    public boolean isValidMessage(String rawMessage) {
        return rawMessage != null && rawMessage.startsWith(PREFIX + "|");
    }

    public String buildWelcomeMessage(int clientId) {
        return "SPC|WELCOME|client=" + clientId;
    }

    public String buildRegisteredMessage(int clientId, String role) {
        return "SPC|REGISTERED|client=" + clientId + "|role=" + role;
    }

    public String buildPongMessage() {
        return "SPC|PONG";
    }

    public String buildMoveAcceptedMessage(String direction) {
        return "SPC|ACTION_OK|type=MOVE|dir=" + direction;
    }

    public String buildShotAcceptedMessage() {
        return "SPC|ACTION_OK|type=SHOT";
    }

    public String buildTickAcceptedMessage() {
        return "SPC|ACTION_OK|type=TICK";
    }

    public String buildByeMessage(int clientId) {
        return "SPC|BYE|client=" + clientId;
    }

    public String buildErrorMessage(String description) {
        return "SPC|ERROR|message=" + description;
    }

    public String buildServerMessage(String message) {
        return "SPC|SERVER_MESSAGE|message=" + message;
    }

    /**
     * Construye un mensaje snapshot con el estado actual del juego.
     *
     * @param snapshot Estado actual del juego.
     * @return Mensaje SPC con datos del juego.
     */
    public String buildSnapshotMessage(GameSnapshot snapshot) {
        PlayerState playerState = snapshot.getPlayerState();

        return "SPC|SNAPSHOT"
                + "|player=" + playerState.getPlayerId()
                + "|x=" + playerState.getX()
                + "|y=" + playerState.getY()
                + "|lives=" + playerState.getLives()
                + "|score=" + playerState.getScore()
                + "|status=" + snapshot.getMatchStatus()
                + "|players=" + snapshot.getPlayerCount()
                + "|aliens=" + snapshot.getAlienCount()
                + "|shots=" + snapshot.getProjectileCount()
                + "|bunkers=" + snapshot.getBunkerCount()
                + "|ufos=" + snapshot.getUfoCount()
                + "|playerData=" + buildPlayerData(snapshot)
                + "|alienData=" + buildAlienData(snapshot)
                + "|shotData=" + buildProjectileData(snapshot)
                + "|bunkerData=" + buildBunkerData(snapshot)
                + "|ufoData=" + buildUfoData(snapshot);
    }

    private String buildAlienData(GameSnapshot snapshot) {
        StringBuilder builder = new StringBuilder();

        snapshot.getAlienStates().forEach(alien -> {
            if (builder.length() > 0) {
                builder.append(";");
            }

            builder.append(alien.getId())
                    .append(",")
                    .append(alien.getX())
                    .append(",")
                    .append(alien.getY())
                    .append(",")
                    .append(alien.getType())
                    .append(",")
                    .append(alien.getPoints());
        });

        if (builder.length() == 0) {
            return "empty";
        }

        return builder.toString();
    }

    private String buildProjectileData(GameSnapshot snapshot) {
        StringBuilder builder = new StringBuilder();

        snapshot.getProjectileStates().forEach(projectile -> {
            if (builder.length() > 0) {
                builder.append(";");
            }

            builder.append(projectile.getId())
                    .append(",")
                    .append(projectile.getX())
                    .append(",")
                    .append(projectile.getY())
                    .append(",")
                    .append(projectile.getOwner());
        });

        if (builder.length() == 0) {
            return "empty";
        }

        return builder.toString();
    }

    private String buildBunkerData(GameSnapshot snapshot) {
        StringBuilder builder = new StringBuilder();

        snapshot.getBunkerStates().forEach(bunker -> {
            if (builder.length() > 0) {
                builder.append(";");
            }

            builder.append(bunker.getId())
                    .append(",")
                    .append(bunker.getX())
                    .append(",")
                    .append(bunker.getY())
                    .append(",")
                    .append(bunker.getHealth());
        });

        if (builder.length() == 0) {
            return "empty";
        }

        return builder.toString();
    }

    private String buildUfoData(GameSnapshot snapshot) {
        StringBuilder builder = new StringBuilder();

        snapshot.getUfoStates().forEach(ufo -> {
            if (builder.length() > 0) {
                builder.append(";");
            }

            builder.append(ufo.getId())
                    .append(",")
                    .append(ufo.getX())
                    .append(",")
                    .append(ufo.getY())
                    .append(",")
                    .append(ufo.getPoints())
                    .append(",")
                    .append(ufo.getDirection());
        });

        if (builder.length() == 0) {
            return "empty";
        }

        return builder.toString();
    }

    private String buildPlayerData(GameSnapshot snapshot) {
        StringBuilder builder = new StringBuilder();

        snapshot.getPlayerStates().forEach(player -> {
            if (builder.length() > 0) {
                builder.append(";");
            }

            builder.append(player.getPlayerId())
                    .append(",")
                    .append(player.getX())
                    .append(",")
                    .append(player.getY())
                    .append(",")
                    .append(player.getLives())
                    .append(",")
                    .append(player.getScore());
        });

        if (builder.length() == 0) {
            return "empty";
        }

        return builder.toString();
    }
    }