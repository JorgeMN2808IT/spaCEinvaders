#include <stdio.h>
#include <string.h>

#include "client_app.h"
#include "../model/constants.h"
#include "../model/structs.h"
#include "../net/socket_client.h"
#include "../protocol/command_builder.h"

/*
 * Muestra el menú temporal del cliente jugador.
 * Este menú será reemplazado más adelante por el sistema gráfico.
 */
static void show_menu() {
    printf("\n=========== Cliente Jugador C ===========\n");
    printf("1. Enviar PING al servidor\n");
    printf("2. Mover canon a la izquierda\n");
    printf("3. Mover canon a la derecha\n");
    printf("4. Disparar\n");
    printf("5. Enviar mensaje personalizado\n");
    printf("6. Desconectar\n");
    printf("Seleccione una opcion: ");
}

/*
 * Lee y limpia el buffer de entrada estándar.
 */
static void clean_input_buffer() {
    int character;

    while ((character = getchar()) != '\n' && character != EOF) {
        /*
         * Limpieza del buffer.
         */
    }
}

/*
 * Lee la respuesta del servidor y la muestra en consola.
 */
static int read_server_response(int socketFd) {
    char response[BUFFER_SIZE];
    int bytesRead;

    bytesRead = receive_message_from_server(socketFd, response, BUFFER_SIZE);

    if (bytesRead <= 0) {
        return -1;
    }

    printf("[Servidor] %s", response);

    if (response[strlen(response) - 1] != '\n') {
        printf("\n");
    }

    return 0;
}

/*
 * Envía un comando al servidor y luego lee su respuesta.
 */
static int send_command_and_read_response(int socketFd, const char *command) {
    printf("[Cliente C] Enviando: %s", command);

    if (send_message_to_server(socketFd, command) != 0) {
        return -1;
    }

    return read_server_response(socketFd);
}

/*
 * Envía el mensaje inicial HELLO al servidor.
 */
static int send_initial_hello(int socketFd) {
    char command[COMMAND_SIZE];

    build_hello_command(command, COMMAND_SIZE);

    printf("[Cliente C] Enviando: %s", command);

    if (send_message_to_server(socketFd, command) != 0) {
        return -1;
    }
    if (read_server_response(socketFd) != 0) {
        return -1;
    }

    if (read_server_response(socketFd) != 0) {
        return -1;
    }

    return 0;
}

/*
 * Ejecuta la aplicación principal del cliente.
 */
void run_client_app() {
    ConnectionConfig config;
    PlayerClientState clientState;

    char command[COMMAND_SIZE];
    char customMessage[COMMAND_SIZE];

    int selectedOption;
    int running = 1;

    init_connection_config(&config);

    clientState.socketFd = connect_to_server(config);
    clientState.connected = clientState.socketFd >= 0;
    clientState.playerId = TEMP_PLAYER_ID;

    if (!clientState.connected) {
        printf("[Cliente C] No fue posible iniciar el cliente jugador.\n");
        return;
    }

    if (read_server_response(clientState.socketFd) != 0) {
        close_connection(clientState.socketFd);
        return;
    }

    if (send_initial_hello(clientState.socketFd) != 0) {
        close_connection(clientState.socketFd);
        return;
    }

    while (running) {
        show_menu();

        if (scanf("%d", &selectedOption) != 1) {
            printf("[Cliente C] Opcion invalida.\n");
            clean_input_buffer();
            continue;
        }

        clean_input_buffer();

        switch (selectedOption) {
            case OPTION_PING:
                build_ping_command(command, COMMAND_SIZE);

                if (send_command_and_read_response(clientState.socketFd, command) != 0) {
                    running = 0;
                }

                break;

            case OPTION_MOVE_LEFT:
                build_move_command(command, COMMAND_SIZE, clientState.playerId, "LEFT");

                if (send_command_and_read_response(clientState.socketFd, command) != 0) {
                    running = 0;
                }

                break;

            case OPTION_MOVE_RIGHT:
                build_move_command(command, COMMAND_SIZE, clientState.playerId, "RIGHT");

                if (send_command_and_read_response(clientState.socketFd, command) != 0) {
                    running = 0;
                }

                break;

            case OPTION_SHOOT:
                build_shot_command(command, COMMAND_SIZE, clientState.playerId);

                printf("[Cliente C] Enviando: %s", command);

                if (send_message_to_server(clientState.socketFd, command) != 0) {
                    running = 0;
                    break;
                }
                if (read_server_response(clientState.socketFd) != 0) {
                    running = 0;
                    break;
                }

                if (read_server_response(clientState.socketFd) != 0) {
                    running = 0;
                }

                break;

            case OPTION_SEND_CUSTOM_MESSAGE:
                printf("Digite mensaje SPC completo: ");

                if (fgets(customMessage, COMMAND_SIZE, stdin) == NULL) {
                    printf("[Cliente C] No se pudo leer el mensaje.\n");
                    break;
                }

                if (send_command_and_read_response(clientState.socketFd, customMessage) != 0) {
                    running = 0;
                }

                break;

            case OPTION_DISCONNECT:
                build_disconnect_command(command, COMMAND_SIZE);

                send_command_and_read_response(clientState.socketFd, command);

                running = 0;
                break;

            default:
                printf("[Cliente C] Opcion no reconocida.\n");
                break;
        }
    }

    close_connection(clientState.socketFd);

    printf("[Cliente C] Cliente finalizado.\n");
}