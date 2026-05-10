#ifndef SOCKET_CLIENT_H
#define SOCKET_CLIENT_H

#include "../model/structs.h"

/*
 * Inicializa la configuración de conexión del cliente.
 */
void init_connection_config(ConnectionConfig *config);

/*
 * Crea el socket y conecta el cliente con el servidor.
 * Retorna el descriptor del socket o -1 si ocurre un error.
 */
int connect_to_server(ConnectionConfig config);

/*
 * Envía un mensaje al servidor.
 * Retorna 0 si se envió correctamente o -1 si hubo error.
 */
int send_message_to_server(int socketFd, const char *message);

/*
 * Recibe un mensaje del servidor.
 * Retorna la cantidad de bytes leídos o -1 si hubo error.
 */
int receive_message_from_server(int socketFd, char *buffer, int bufferSize);

/*
 * Cierra el socket del cliente.
 */
void close_connection(int socketFd);

#endif