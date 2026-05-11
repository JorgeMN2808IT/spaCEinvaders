#include <stdio.h>
#include <string.h>
#include "raylib.h"

#include "client_app.h"
#include "../model/constants.h"
#include "../model/structs.h"
#include "../model/client_state.h"
#include "../net/socket_client.h"
#include "../protocol/command_builder.h"
#include "../protocol/state_decoder.h"
#include "../graphics/window_renderer.h"

static int read_server_response(int socketFd, ClientGameState *gameState) {
    char response[BUFFER_SIZE];
    int bytesRead;

    bytesRead = receive_message_from_server(socketFd, response, BUFFER_SIZE);

    if (bytesRead <= 0) {
        return -1;
    }

    printf("[Servidor] %s\n", response);

    if (is_snapshot_message(response)) {
        decode_snapshot_message(response, gameState);
    }

    return 0;
}

static int send_command(int socketFd, const char *command) {
    return send_message_to_server(socketFd, command);
}

static int send_command_and_read_one_response(
        int socketFd,
        const char *command,
        ClientGameState *gameState
) {
    if (send_command(socketFd, command) != 0) {
        return -1;
    }

    return read_server_response(socketFd, gameState);
}

static int send_command_and_read_two_responses(
        int socketFd,
        const char *command,
        ClientGameState *gameState
) {
    if (send_command(socketFd, command) != 0) {
        return -1;
    }

    if (read_server_response(socketFd, gameState) != 0) {
        return -1;
    }

    return read_server_response(socketFd, gameState);
}

static int send_initial_hello(int socketFd, ClientGameState *gameState) {
    char command[COMMAND_SIZE];

    build_hello_command(command, COMMAND_SIZE);

    return send_command_and_read_two_responses(socketFd, command, gameState);
}

static void process_player_input(int socketFd, ClientGameState *gameState, int *running) {
    char command[COMMAND_SIZE];

    if (is_left_pressed()) {
        build_move_command(command, COMMAND_SIZE, gameState->player.id, "LEFT");
        send_command_and_read_one_response(socketFd, command, gameState);
    }

    if (is_right_pressed()) {
        build_move_command(command, COMMAND_SIZE, gameState->player.id, "RIGHT");
        send_command_and_read_one_response(socketFd, command, gameState);
    }

    if (is_shoot_pressed()) {
        build_shot_command(command, COMMAND_SIZE, gameState->player.id);

        send_command_and_read_two_responses(socketFd, command, gameState);
    }

    if (is_exit_pressed()) {
        build_disconnect_command(command, COMMAND_SIZE);
        send_command_and_read_one_response(socketFd, command, gameState);
        *running = 0;
    }
}

static void process_automatic_tick(int socketFd, ClientGameState *gameState) {
    static double lastTickTime = 0.0;
    double currentTime = GetTime();
    char command[COMMAND_SIZE];

    if (currentTime - lastTickTime >= 0.25) {
        build_tick_command(command, COMMAND_SIZE);

        send_command_and_read_two_responses(socketFd, command, gameState);

        lastTickTime = currentTime;
    }
}

void run_client_app() {
    ConnectionConfig config;
    PlayerClientState clientState;
    ClientGameState gameState;

    int running = 1;

    init_connection_config(&config);
    init_client_game_state(&gameState);

    clientState.socketFd = connect_to_server(config);
    clientState.connected = clientState.socketFd >= 0;
    clientState.playerId = TEMP_PLAYER_ID;

    if (!clientState.connected) {
        printf("[Cliente C] No fue posible iniciar el cliente jugador.\n");
        return;
    }

    init_game_window();

    if (read_server_response(clientState.socketFd, &gameState) != 0) {
        close_game_window();
        close_connection(clientState.socketFd);
        return;
    }

    if (send_initial_hello(clientState.socketFd, &gameState) != 0) {
        close_game_window();
        close_connection(clientState.socketFd);
        return;
    }

    while (running && !should_close_game_window()) {
        process_player_input(clientState.socketFd, &gameState, &running);
        process_automatic_tick(clientState.socketFd, &gameState);

        render_game_window(&gameState);
    }

    close_game_window();

    close_connection(clientState.socketFd);

    printf("[Cliente C] Cliente finalizado.\n");
}