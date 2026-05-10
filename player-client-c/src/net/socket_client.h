#ifndef SOCKET_CLIENT_H
#define SOCKET_CLIENT_H

#include "../model/structs.h"

void init_connection_config(ConnectionConfig *config);

int connect_to_server(ConnectionConfig config);

int send_message_to_server(int socketFd, const char *message);

int receive_message_from_server(int socketFd, char *buffer, int bufferSize);

void close_connection(int socketFd);

#endif