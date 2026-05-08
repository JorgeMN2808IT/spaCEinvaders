package com.spaceinvaders.network;

import com.spaceinvaders.protocol.MessageCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*Clase encargada de manejar la comunicación individual con un cliente.Cada vez que un cliente se conecta al servidor, se crea una sesiónindependiente. Esta sesión se ejecuta en su propio hilo mediante Runnable. */
public class ClientSession implements Runnable {

    private final int clientId;
    private final Socket clientSocket;
    private final MessageCodec messageCodec;

    private BufferedReader input;
    private PrintWriter output;

    private boolean running;

    /**
     * Constructor de la sesión de cliente.
     * @param clientId Identificador único asignado al cliente.
     * @param clientSocket Socket de comunicación con el cliente.
     */
    public ClientSession(int clientId, Socket clientSocket) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.messageCodec = new MessageCodec();
        this.running = true;
    }

    /**
     * Método principal del hilo de la sesión. Se encarga de escuchar mensajes enviados por el cliente.
     */
    @Override
    public void run() {
        try {
            initializeStreams();
            sendMessage(messageCodec.buildWelcomeMessage(clientId));

            System.out.println("[Servidor] Cliente " + clientId + " conectado desde "
                    + clientSocket.getInetAddress().getHostAddress());

            listenClientMessages();

        } catch (IOException exception) {
            System.out.println("[Servidor] Error con el cliente " + clientId + ": "
                    + exception.getMessage());
        } finally {
            closeSession();
        }
    }

    /**
     * Inicializa los flujos de entrada y salida del socket.
     * @throws IOException Si ocurre un error al obtener los flujos.
     */
    private void initializeStreams() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    /**
     * Escucha continuamente los mensajes enviados por el cliente.
     * @throws IOException Si ocurre un error de lectura.
     */
    private void listenClientMessages() throws IOException {
        String receivedMessage;

        while (running && (receivedMessage = input.readLine()) != null) {
            System.out.println("[Cliente " + clientId + "] " + receivedMessage);

            processMessage(receivedMessage);
        }
    }

    /**
     * Procesa un mensaje recibido desde el cliente. Por ahora se manejan mensajes básicos. Luego esta lógica se moverá a clases más específicas del protocolo y del motor del juego.
     * @param message Mensaje recibido.
     */
    private void processMessage(String message) {
        if (!messageCodec.isValidMessage(message)) {
            sendMessage(messageCodec.buildErrorMessage("Formato de mensaje invalido"));
            return;
        }

        if (message.equals("SPC|PING")) {
            sendMessage(messageCodec.buildPongMessage());
            return;
        }

        if (message.startsWith("SPC|HELLO")) {
            sendMessage(messageCodec.buildServerMessage("Cliente registrado correctamente"));
            return;
        }

        if (message.equals("SPC|DISCONNECT")) {
            sendMessage(messageCodec.buildServerMessage("Desconexion solicitada"));
            running = false;
            return;
        }

        sendMessage(messageCodec.buildServerMessage("Mensaje recibido"));
    }

    /**
     * Envía un mensaje al cliente.
     * @param message
     */
    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    /** Cierra la sesión del cliente y libera los recursos asociados.
     */
    private void closeSession() {
        running = false;

        try {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }

            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }

            System.out.println("[Servidor] Cliente " + clientId + " desconectado.");

        } catch (IOException exception) {
            System.out.println("[Servidor] Error al cerrar cliente " + clientId + ": "
                    + exception.getMessage());
        }
    }
}