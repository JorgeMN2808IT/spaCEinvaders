
/*Clase encargada de codificar y decodificar mensajes del protocolo SPC.*/

package com.spaceinvaders.protocol;
public class MessageCodec {

    private static final String PREFIX = "SPC";

    /** Verifica si un mensaje pertenece al protocolo SPC.
     @param rawMessage *Mensaje recibido desde un cliente.
     @return *true si el mensaje inicia con el prefijo SPC.
     */
    public boolean isValidMessage(String rawMessage) {
        return rawMessage != null && rawMessage.startsWith(PREFIX + "|");
    }

    /** Construye un mensaje de bienvenida para un cliente conectado.
     @param clientId *Identificador asignado al cliente.
     @return *Mensaje de bienvenida en formato SPC.
     */
    public String buildWelcomeMessage(int clientId) {
        return "SPC|WELCOME|client=" + clientId;
    }

    /**Construye un mensaje de respuesta tipo PONG.
     @return Mensaje PONG en formato SPC.*/
    public String buildPongMessage() {
        return "SPC|PONG";
    }

    /**
     * Construye un mensaje de error.
     @param description Descripción del error.
     @return Mensaje de error en formato SPC.
     */
    public String buildErrorMessage(String description) {
        return "SPC|ERROR|message=" + description;
    }

    /**Construye un mensaje genérico del servidor.
     @param message
     @return 
     */
    public String buildServerMessage(String message) {
        return "SPC|SERVER_MESSAGE|message=" + message;
    }
}