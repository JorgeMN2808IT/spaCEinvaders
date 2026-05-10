#include <stdio.h>
#include <string.h>

#ifdef _WIN32
    #include <winsock2.h>
    #include <ws2tcpip.h>
#else
    #include <unistd.h>
    #include <arpa/inet.h>
    #include <sys/socket.h>
#endif

#include "socket_client.h"
#include "../model/constants.h"

/*
 * Inicializa la configuración de conexión con valores por defecto.
 */
void init_connection_config(ConnectionConfig *config) {
    strncpy(config->serverIp, SERVER_IP, sizeof(config->serverIp) - 1);
    config->serverIp[sizeof(config->serverIp) - 1] = '\0';
    config->serverPort = SERVER_PORT;
}

/*
 * Crea un socket TCP y se conecta al servidor Java.
 */
int connect_to_server(ConnectionConfig config) {
    int socketFd;
    struct sockaddr_in serverAddress;

#ifdef _WIN32
    WSADATA wsaData;

    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        printf("[Cliente C] Error al inicializar Winsock.\n");
        return -1;
    }
#endif

    socketFd = socket(AF_INET, SOCK_STREAM, 0);

    if (socketFd < 0) {
        printf("[Cliente C] Error al crear el socket.\n");
        return -1;
    }

    memset(&serverAddress, 0, sizeof(serverAddress));

    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(config.serverPort);

    serverAddress.sin_addr.s_addr = inet_addr(config.serverIp);

    if (serverAddress.sin_addr.s_addr == INADDR_NONE) {
        printf("[Cliente C] Direccion IP invalida: %s\n", config.serverIp);
        close_connection(socketFd);
        return -1;
    }

    if (connect(socketFd, (struct sockaddr *)&serverAddress, sizeof(serverAddress)) < 0) {
        printf("[Cliente C] No se pudo conectar al servidor %s:%d\n",
               config.serverIp,
               config.serverPort);
        close_connection(socketFd);
        return -1;
    }

    printf("[Cliente C] Conexion establecida con %s:%d\n",
           config.serverIp,
           config.serverPort);

    return socketFd;
}
/*
 * Envía un mensaje al servidor.
 */
int send_message_to_server(int socketFd, const char *message) {
#ifdef _WIN32
    int result = send(socketFd, message, (int)strlen(message), 0);
#else
    int result = send(socketFd, message, strlen(message), 0);
#endif

    if (result < 0) {
        printf("[Cliente C] Error al enviar mensaje.\n");
        return -1;
    }

    return 0;
}

/*
 * Recibe un mensaje desde el servidor.
 */
int receive_message_from_server(int socketFd, char *buffer, int bufferSize) {
    int bytesRead;

    memset(buffer, 0, bufferSize);

#ifdef _WIN32
    bytesRead = recv(socketFd, buffer, bufferSize - 1, 0);
#else
    bytesRead = recv(socketFd, buffer, bufferSize - 1, 0);
#endif

    if (bytesRead < 0) {
        printf("[Cliente C] Error al recibir mensaje.\n");
        return -1;
    }

    if (bytesRead == 0) {
        printf("[Cliente C] El servidor cerro la conexion.\n");
        return 0;
    }

    buffer[bytesRead] = '\0';

    return bytesRead;
}

/*
 * Cierra la conexión del cliente.
 */
void close_connection(int socketFd) {
#ifdef _WIN32
    closesocket(socketFd);
    WSACleanup();
#else
    close(socketFd);
#endif
}