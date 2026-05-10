package com.spaceinvaders.protocol;

public class MessageCodec {

    private static final String PREFIX = "SPC";

    /**
     * @param rawMessage
     * @return 
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
}