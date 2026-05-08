package com.spaceinvaders.config;

/**
 * Clase encargada de almacenar la configuración básica del servidor.
 * 
 * En esta clase se definen constantes relacionadas con la comunicación
 * por sockets, como el puerto del servidor y la cantidad máxima sugerida
 * de clientes.
 */
public class ServerConfig {

    public static final int SERVER_PORT = 5000;

    public static final int MAX_PLAYERS = 2;

    public static final int MAX_SPECTATORS = 10;

    public static final String SERVER_NAME = "spaCEinvaders Core Server";

    private ServerConfig() {
        /*
         * Constructor privado para evitar la creación de objetos de esta clase,
         * ya que únicamente contiene constantes de configuración.
         */
    }
}