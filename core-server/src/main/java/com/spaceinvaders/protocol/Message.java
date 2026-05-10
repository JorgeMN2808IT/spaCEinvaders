package com.spaceinvaders.protocol;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private final MessageType type;
    private final String rawContent;
    private final Map<String, String> parameters;

    public Message(MessageType type, String rawContent) {
        this.type = type;
        this.rawContent = rawContent;
        this.parameters = new HashMap<>();
    }

    public MessageType getType() {
        return type;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}