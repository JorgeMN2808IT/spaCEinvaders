#ifndef STRUCTS_H
#define STRUCTS_H

#include "constants.h"

typedef struct {
    char serverIp[64];
    int serverPort;
} ConnectionConfig;

typedef struct {
    int socketFd;
    int connected;
    int playerId;
    int spectatorMode;
} PlayerClientState;

typedef struct {
    int id;
    int x;
    int y;
    int lives;
    int score;
} PlayerView;

typedef struct {
    int id;
    int x;
    int y;
    int points;
    char type[32];
} AlienView;

typedef struct {
    int id;
    int x;
    int y;
    char owner[32];
} ProjectileView;

typedef struct {
    int id;
    int x;
    int y;
    int health;
} BunkerView;

typedef struct {
    int id;
    int x;
    int y;
    int points;
    char direction[32];
} UfoView;

typedef struct {
    PlayerView player;
    PlayerView players[MAX_PLAYERS];
    AlienView aliens[MAX_ALIENS];
    ProjectileView projectiles[MAX_PROJECTILES];
    BunkerView bunkers[MAX_BUNKERS];
    UfoView ufos[MAX_UFOS];

    int playerCount;
    int alienCount;
    int projectileCount;
    int bunkerCount;
    int ufoCount;

    char matchStatus[32];
} ClientGameState;

#endif