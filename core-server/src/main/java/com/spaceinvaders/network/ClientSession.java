package com.spaceinvaders.network;

import com.spaceinvaders.domain.state.Direction;
import com.spaceinvaders.domain.state.GameSnapshot;
import com.spaceinvaders.engine.MatchController;
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
 */
public class ClientSession implements Runnable {

    private final int clientId;
    private final Socket clientSocket;
    private final MessageCodec messageCodec;
    private final CommandParser commandParser;
    private final MatchController matchController;

    private BufferedReader input;
    private PrintWriter output;

    private boolean running;
    private String clientRole;

    /**
     * Constructor de la sesión de cliente.
     *
     * @param clientId Identificador único asignado al cliente.
     * @param clientSocket Socket de comunicación con el cliente.
     * @param matchController Controlador compartido de la partida.
     */
    public ClientSession(int clientId, Socket clientSocket, MatchController matchController) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.matchController = matchController;
        this.messageCodec = new MessageCodec();
        this.commandParser = new CommandParser();
        this.running = true;
        this.clientRole = "UNDEFINED";
    }
    private void processTickMessage() {
        if (!clientRole.equals("PLAYER")) {
            sendMessage(messageCodec.buildErrorMessage("Solo los jugadores pueden avanzar el juego"));
            return;
        }

        matchController.updateGame();

        GameSnapshot snapshot = matchController.buildSnapshot(clientId);

        sendMessage(messageCodec.buildTickAcceptedMessage());
        sendMessage(messageCodec.buildSnapshotMessage(snapshot));
    }

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
     * Escucha continuamente los mensajes enviados por el cliente.
     *
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

            case TICK:
                processTickMessage();
                break;

            case DISCONNECT:
                processDisconnectMessage();
                break;

            default:
                sendMessage(messageCodec.buildErrorMessage("Mensaje no soportado"));
                break;
        }
    }
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

        if (clientRole.equals("PLAYER")) {
            matchController.registerPlayer(clientId);
        }

        sendMessage(messageCodec.buildRegisteredMessage(clientId, clientRole));

        if (clientRole.equals("PLAYER")) {
            GameSnapshot snapshot = matchController.buildSnapshot(clientId);
            sendMessage(messageCodec.buildSnapshotMessage(snapshot));
        }
    }

    private void processMoveMessage(Message message) {
        if (!clientRole.equals("PLAYER")) {
            sendMessage(messageCodec.buildErrorMessage("Solo los jugadores pueden moverse"));
            return;
        }

        if (!message.hasParameter("dir")) {
            sendMessage(messageCodec.buildErrorMessage("Falta parametro dir"));
            return;
        }

        String directionText = message.getParameter("dir").toUpperCase();

        Direction direction;

        if (directionText.equals("LEFT")) {
            direction = Direction.LEFT;
        } else if (directionText.equals("RIGHT")) {
            direction = Direction.RIGHT;
        } else {
            sendMessage(messageCodec.buildErrorMessage("Direccion no soportada"));
            return;
        }

        GameSnapshot snapshot = matchController.movePlayer(clientId, direction);

        sendMessage(messageCodec.buildSnapshotMessage(snapshot));
    }

    private void processShotMessage(Message message) {
        if (!clientRole.equals("PLAYER")) {
            sendMessage(messageCodec.buildErrorMessage("Solo los jugadores pueden disparar"));
            return;
        }

        GameSnapshot snapshot = matchController.shoot(clientId);

        sendMessage(messageCodec.buildShotAcceptedMessage());
        sendMessage(messageCodec.buildSnapshotMessage(snapshot));
    }

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