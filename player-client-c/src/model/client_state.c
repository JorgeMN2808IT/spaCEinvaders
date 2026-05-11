#include <string.h>
#include "client_state.h"

void init_client_game_state(ClientGameState *state) {
    int index;

    if (state == 0) {
        return;
    }

    state->player.id = -1;
    state->player.x = 50;
    state->player.y = 90;
    state->player.lives = 3;
    state->player.score = 0;

    for (index = 0; index < MAX_PLAYERS; index++) {
    state->players[index].id = -1;
    state->players[index].x = 0;
    state->players[index].y = 0;
    state->players[index].lives = 0;
    state->players[index].score = 0;
}

    state->playerCount = 0;
    state->alienCount = 0;
    state->projectileCount = 0;
    state->bunkerCount = 0;
    state->ufoCount = 0;

    strcpy(state->matchStatus, "WAITING");

    for (index = 0; index < MAX_ALIENS; index++) {
        state->aliens[index].id = -1;
        strcpy(state->aliens[index].type, "");
    }

    for (index = 0; index < MAX_PROJECTILES; index++) {
        state->projectiles[index].id = -1;
        strcpy(state->projectiles[index].owner, "");
    }

    for (index = 0; index < MAX_BUNKERS; index++) {
        state->bunkers[index].id = -1;
    }

    for (index = 0; index < MAX_UFOS; index++) {
        state->ufos[index].id = -1;
        strcpy(state->ufos[index].direction, "");
    }
}

void clear_dynamic_entities(ClientGameState *state) {
    int index;

    if (state == 0) {
        return;
    }

    for (index = 0; index < MAX_ALIENS; index++) {
        state->aliens[index].id = -1;
        state->aliens[index].x = 0;
        state->aliens[index].y = 0;
        state->aliens[index].points = 0;
        strcpy(state->aliens[index].type, "");
    }

    for (index = 0; index < MAX_PROJECTILES; index++) {
        state->projectiles[index].id = -1;
        state->projectiles[index].x = 0;
        state->projectiles[index].y = 0;
        strcpy(state->projectiles[index].owner, "");
    }
    
    for (index = 0; index < MAX_PLAYERS; index++) {
    state->players[index].id = -1;
    state->players[index].x = 0;
    state->players[index].y = 0;
    state->players[index].lives = 0;
    state->players[index].score = 0;
    }

    for (index = 0; index < MAX_BUNKERS; index++) {
        state->bunkers[index].id = -1;
        state->bunkers[index].x = 0;
        state->bunkers[index].y = 0;
        state->bunkers[index].health = 0;
    }

    for (index = 0; index < MAX_UFOS; index++) {
        state->ufos[index].id = -1;
        state->ufos[index].x = 0;
        state->ufos[index].y = 0;
        state->ufos[index].points = 0;
        strcpy(state->ufos[index].direction, "");
    }
}