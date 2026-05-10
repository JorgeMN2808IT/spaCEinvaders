/* Clase principal del servidor. Aquí se inicia la aplicación y se pone en marcha el servidor.*/
package com.spaceinvaders;

import com.spaceinvaders.network.ServerGateway;

public class Launcher {

    public static void main(String[] args) {
        ServerGateway serverGateway = new ServerGateway();
        serverGateway.startServer();
    }
}