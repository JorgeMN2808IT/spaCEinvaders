#ifndef STATE_DECODER_H
#define STATE_DECODER_H

#include "../model/structs.h"

int is_snapshot_message(const char *message);

int decode_snapshot_message(const char *message, ClientGameState *state);

#endif