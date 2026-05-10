#ifndef STRUCTS_H
#define STRUCTS_H

/*
 * Estructura que representa la configuración de conexión del cliente.
 */
typedef struct {
    char serverIp[64];
    int serverPort;
} ConnectionConfig;

/*
 * Estructura que representa el estado básico del cliente jugador.
 * Más adelante se ampliará con puntaje, vidas, posición del cañón,
 * aliens visibles, bunkers y disparos.
 */
typedef struct {
    int socketFd;
    int connected;
    int playerId;
} PlayerClientState;

#endif