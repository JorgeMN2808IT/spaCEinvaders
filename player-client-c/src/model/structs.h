#ifndef STRUCTS_H
#define STRUCTS_H

typedef struct {
    char serverIp[64];
    int serverPort;
} ConnectionConfig;

typedef struct {
    int socketFd;
    int connected;
    int playerId;
} PlayerClientState;

#endif