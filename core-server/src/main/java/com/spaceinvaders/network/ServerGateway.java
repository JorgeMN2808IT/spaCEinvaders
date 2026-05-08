package com.spaceinvaders.network;

import com.spaceinvaders.config.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase encargada de abrir el servidor y aceptar conexiones entrantes esta clase representa la puerta de entrada del servidor, cada cliente aceptado se maneja mediante una instancia de ClientSession.
 */
public class ServerGateway {

    private ServerSocket serverSocket;
    private boolean running;
    private int clientCounter;

    /**
     * Constructor del servidor.
     */
    public ServerGateway() {
        this.running = false;
        this.clientCounter = 0;
    }

    /**
     * Inicia el servidor en el puerto configurado.
     */
    public void startServer() {
        try {
            serverSocket = new ServerSocket(ServerConfig.SERVER_PORT);
            running = true;

            System.out.println("======================================");
            System.out.println(" " + ServerConfig.SERVER_NAME);
            System.out.println(" Puerto: " + ServerConfig.SERVER_PORT);
            System.out.println(" Estado: esperando clientes...");
            System.out.println("======================================");

            acceptClients();

        } catch (IOException exception) {
            System.out.println("[Servidor] No se pudo iniciar el servidor: "
                    + exception.getMessage());
        } finally {
            stopServer();
        }
    }

    /**
     * Acepta clientes de forma continua mientras el servidor esté activo.
     * @throws IOException Si ocurre un error al aceptar clientes.
     */
    private void acceptClients() throws IOException {
        while (running) {
            Socket clientSocket = serverSocket.accept();

            clientCounter++;

            ClientSession clientSession = new ClientSession(clientCounter, clientSocket);
            Thread clientThread = new Thread(clientSession);

            clientThread.start();
        }
    }

    /**Detiene el servidor y cierra el socket principal.
     */
    public void stopServer() {
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            System.out.println("[Servidor] Servidor detenido.");

        } catch (IOException exception) {
            System.out.println("[Servidor] Error al detener el servidor: "
                    + exception.getMessage());
        }
    }
}