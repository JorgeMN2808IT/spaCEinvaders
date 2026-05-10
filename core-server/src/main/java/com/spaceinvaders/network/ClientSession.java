package com.spaceinvaders.network;

import com.spaceinvaders.protocol.CommandParser;
import com.spaceinvaders.protocol.Message;
import com.spaceinvaders.protocol.MessageCodec;
import com.spaceinvaders.protocol.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
    * Clase que representa la sesión de un cliente conectado al servidor.
 */
public class ClientSession implements Runnable {

    private final int clientId;
    private final Socket clientSocket;
    private final MessageCodec messageCodec;
    private final CommandParser commandParser;

    private BufferedReader input;
    private PrintWriter output;

    private boolean running;
    private String clientRole;

    /**
      * Constructor de la sesión del cliente.
      *
     * @param clientId Identificador único asignado al cliente.
     * @param clientSocket Socket de comunicación con el cliente.
     */
    public ClientSession(int clientId, Socket clientSocket) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.messageCodec = new MessageCodec();
        this.commandParser = new CommandParser();
        this.running = true;
        this.clientRole = "UNDEFINED";
    }

    /**
      * Método principal del hilo que maneja la sesión del cliente.
      *
      * Este método se encarga de:
      * - Inicializar los flujos de comunicación.
      * - Enviar un mensaje de bienvenida al cliente.
      * - Escuchar y procesar los mensajes recibidos del cliente.
      * - Manejar la desconexión y limpieza de recursos al finalizar la sesión.
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
     * @throws IOException Si ocurre un error al obtener los flujos.
     */
    private void initializeStreams() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    /**
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
     * @param rawMessage Mensaje recibido.
     */
    private void processMessage(String rawMessage) {
        Message message = commandParser.parse(rawMessage);

        if (message.getType() == MessageType.UNKNOWN) {
            sendMessage(messageCodec.buildErrorMessage("Comando no reconocido o formato invalido"));
            return;
        }

        switch (message.getType()) {
            case HELLO:
                processHelloMessage(message);
                break;

            case PING:
                sendMessage(messageCodec.buildPongMessage());
                break;

            case MOVE:
                processMoveMessage(message);
                break;

            case SHOT:
                processShotMessage(message);
                break;

            case DISCONNECT:
                processDisconnectMessage();
                break;

            default:
                sendMessage(messageCodec.buildErrorMessage("Mensaje no soportado"));
                break;
        }
    }

    /**
     */
    private void processHelloMessage(Message message) {
        if (!message.hasParameter("role")) {
            sendMessage(messageCodec.buildErrorMessage("Falta parametro role"));
            return;
        }

        String role = message.getParameter("role").toUpperCase();

        if (!role.equals("PLAYER") && !role.equals("SPECTATOR")) {
            sendMessage(messageCodec.buildErrorMessage("Rol no soportado"));
            return;
        }

        this.clientRole = role;

        sendMessage(messageCodec.buildRegisteredMessage(clientId, clientRole));
    }

    /**
     */
    private void processMoveMessage(Message message) {
        if (!message.hasParameter("player")) {
            sendMessage(messageCodec.buildErrorMessage("Falta parametro player"));
            return;
        }

        if (!message.hasParameter("dir")) {
            sendMessage(messageCodec.buildErrorMessage("Falta parametro dir"));
            return;
        }

        String direction = message.getParameter("dir").toUpperCase();

        if (!direction.equals("LEFT") && !direction.equals("RIGHT")) {
            sendMessage(messageCodec.buildErrorMessage("Direccion no soportada"));
            return;
        }

        sendMessage(messageCodec.buildMoveAcceptedMessage(direction));
    }

    /**
     */
    private void processShotMessage(Message message) {
        if (!message.hasParameter("player")) {
            sendMessage(messageCodec.buildErrorMessage("Falta parametro player"));
            return;
        }

        sendMessage(messageCodec.buildShotAcceptedMessage());
    }

    /**
     */
    private void processDisconnectMessage() {
        sendMessage(messageCodec.buildByeMessage(clientId));
        running = false;
    }

    /**
     * @param message Mensaje que será enviado.
     */
    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

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