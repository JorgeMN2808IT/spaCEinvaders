#ifndef CLIENT_STATE_H
#define CLIENT_STATE_H

#include "structs.h"

void init_client_game_state(ClientGameState *state);

void clear_dynamic_entities(ClientGameState *state);

#endif