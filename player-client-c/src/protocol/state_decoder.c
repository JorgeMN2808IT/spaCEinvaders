#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "state_decoder.h"
#include "../model/constants.h"
#include "../model/client_state.h"

static int extract_value(const char *message, const char *key, char *output, int outputSize) {
    char pattern[64];
    char *start;
    char *end;
    int length;

    snprintf(pattern, sizeof(pattern), "|%s=", key);

    start = strstr((char *)message, pattern);

    if (start == 0) {
        return 0;
    }

    start += strlen(pattern);
    end = strchr(start, '|');

    if (end == 0) {
        end = start + strlen(start);
    }

    length = (int)(end - start);

    if (length >= outputSize) {
        length = outputSize - 1;
    }

    strncpy(output, start, length);
    output[length] = '\0';

    return 1;
}

int is_snapshot_message(const char *message) {
    if (message == 0) {
        return 0;
    }

    return strncmp(message, "SPC|SNAPSHOT", 12) == 0;
}

static void decode_player_data(const char *message, ClientGameState *state) {
    char value[128];

    if (extract_value(message, "player", value, sizeof(value))) {
        state->player.id = atoi(value);
    }

    if (extract_value(message, "x", value, sizeof(value))) {
        state->player.x = atoi(value);
    }

    if (extract_value(message, "y", value, sizeof(value))) {
        state->player.y = atoi(value);
    }

    if (extract_value(message, "lives", value, sizeof(value))) {
        state->player.lives = atoi(value);
    }

    if (extract_value(message, "score", value, sizeof(value))) {
        state->player.score = atoi(value);
    }

    if (extract_value(message, "status", value, sizeof(value))) {
        strncpy(state->matchStatus, value, sizeof(state->matchStatus) - 1);
        state->matchStatus[sizeof(state->matchStatus) - 1] = '\0';
    }

    if (extract_value(message, "players", value, sizeof(value))) {
        state->playerCount = atoi(value);
    }

    if (extract_value(message, "aliens", value, sizeof(value))) {
        state->alienCount = atoi(value);
    }

    if (extract_value(message, "shots", value, sizeof(value))) {
        state->projectileCount = atoi(value);
    }

    if (extract_value(message, "bunkers", value, sizeof(value))) {
        state->bunkerCount = atoi(value);
    }

    if (extract_value(message, "ufos", value, sizeof(value))) {
        state->ufoCount = atoi(value);
    }
}

static void decode_all_players_data(const char *message, ClientGameState *state) {
    char data[1024];
    char *token;
    int index = 0;

    if (!extract_value(message, "playerData", data, sizeof(data))) {
        return;
    }

    if (strcmp(data, "empty") == 0) {
        state->playerCount = 0;
        return;
    }

    token = strtok(data, ";");

    while (token != 0 && index < MAX_PLAYERS) {
        sscanf(
            token,
            "%d,%d,%d,%d,%d",
            &state->players[index].id,
            &state->players[index].x,
            &state->players[index].y,
            &state->players[index].lives,
            &state->players[index].score
        );

        index++;
        token = strtok(0, ";");
    }

    state->playerCount = index;
}

static void decode_alien_data(const char *message, ClientGameState *state) {
    char data[4096];
    char *token;
    int index = 0;

    if (!extract_value(message, "alienData", data, sizeof(data))) {
        return;
    }

    if (strcmp(data, "empty") == 0) {
        state->alienCount = 0;
        return;
    }

    token = strtok(data, ";");

    while (token != 0 && index < MAX_ALIENS) {
        sscanf(
            token,
            "%d,%d,%d,%31[^,],%d",
            &state->aliens[index].id,
            &state->aliens[index].x,
            &state->aliens[index].y,
            state->aliens[index].type,
            &state->aliens[index].points
        );

        index++;
        token = strtok(0, ";");
    }

    state->alienCount = index;
}

static void decode_projectile_data(const char *message, ClientGameState *state) {
    char data[2048];
    char *token;
    int index = 0;

    if (!extract_value(message, "shotData", data, sizeof(data))) {
        return;
    }

    if (strcmp(data, "empty") == 0) {
        state->projectileCount = 0;
        return;
    }

    token = strtok(data, ";");

    while (token != 0 && index < MAX_PROJECTILES) {
        sscanf(
            token,
            "%d,%d,%d,%31[^,]",
            &state->projectiles[index].id,
            &state->projectiles[index].x,
            &state->projectiles[index].y,
            state->projectiles[index].owner
        );

        index++;
        token = strtok(0, ";");
    }

    state->projectileCount = index;
}

static void decode_bunker_data(const char *message, ClientGameState *state) {
    char data[1024];
    char *token;
    int index = 0;

    if (!extract_value(message, "bunkerData", data, sizeof(data))) {
        return;
    }

    if (strcmp(data, "empty") == 0) {
        state->bunkerCount = 0;
        return;
    }

    token = strtok(data, ";");

    while (token != 0 && index < MAX_BUNKERS) {
        sscanf(
            token,
            "%d,%d,%d,%d",
            &state->bunkers[index].id,
            &state->bunkers[index].x,
            &state->bunkers[index].y,
            &state->bunkers[index].health
        );

        index++;
        token = strtok(0, ";");
    }

    state->bunkerCount = index;
}

static void decode_ufo_data(const char *message, ClientGameState *state) {
    char data[1024];
    char *token;
    int index = 0;

    if (!extract_value(message, "ufoData", data, sizeof(data))) {
        return;
    }

    if (strcmp(data, "empty") == 0) {
        state->ufoCount = 0;
        return;
    }

    token = strtok(data, ";");

    while (token != 0 && index < MAX_UFOS) {
        sscanf(
            token,
            "%d,%d,%d,%d,%31[^,]",
            &state->ufos[index].id,
            &state->ufos[index].x,
            &state->ufos[index].y,
            &state->ufos[index].points,
            state->ufos[index].direction
        );

        index++;
        token = strtok(0, ";");
    }

    state->ufoCount = index;
}

int decode_snapshot_message(const char *message, ClientGameState *state) {
    if (!is_snapshot_message(message) || state == 0) {
        return 0;
    }

    clear_dynamic_entities(state);

    decode_player_data(message, state);
    decode_all_players_data(message, state);
    decode_alien_data(message, state);
    decode_projectile_data(message, state);
    decode_bunker_data(message, state);
    decode_ufo_data(message, state);

    return 1;
}