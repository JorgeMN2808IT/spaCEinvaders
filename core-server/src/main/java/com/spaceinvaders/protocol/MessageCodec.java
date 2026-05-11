package com.spaceinvaders.protocol;

import com.spaceinvaders.domain.state.GameSnapshot;
import com.spaceinvaders.domain.state.PlayerState;

public class MessageCodec {

    private static final String PREFIX = "SPC";

    /**2
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
     * @return Mensaje SPC con datos del jugador y estado de partida.
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
                + "|ufos=" + snapshot.getUfoCount();
    }
}