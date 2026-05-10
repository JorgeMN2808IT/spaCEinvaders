package com.spaceinvaders.protocol;


public class CommandParser {

    private static final String PREFIX = "SPC";

    /**
     * @param rawMessage 
     * @return 
     */
    public Message parse(String rawMessage) {
        if (rawMessage == null || rawMessage.isBlank()) {
            return new Message(MessageType.UNKNOWN, rawMessage);
        }

        String cleanMessage = rawMessage.trim();

        String[] parts = cleanMessage.split("\\|");

        if (parts.length < 2 || !parts[0].equals(PREFIX)) {
            return new Message(MessageType.UNKNOWN, cleanMessage);
        }

        MessageType messageType = getMessageType(parts[1]);
        Message message = new Message(messageType, cleanMessage);

        for (int index = 2; index < parts.length; index++) {
            String currentPart = parts[index];

            if (currentPart.contains("=")) {
                String[] keyValue = currentPart.split("=", 2);

                if (keyValue.length == 2) {
                    message.addParameter(keyValue[0], keyValue[1]);
                }
            }
        }

        return message;
    }

    /**
     * @param value 
     * @return 
     */
    private MessageType getMessageType(String value) {
        try {
            return MessageType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return MessageType.UNKNOWN;
        }
    }
}