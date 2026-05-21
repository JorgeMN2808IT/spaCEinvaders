#include <stdio.h>
#include "command_builder.h"

void build_hello_command(char *buffer, int bufferSize, const char *role) {
    snprintf(buffer, bufferSize, "SPC|HELLO|role=%s\n", role);
}

void build_ping_command(char *buffer, int bufferSize) {
    snprintf(buffer, bufferSize, "SPC|PING\n");
}

void build_move_command(char *buffer, int bufferSize, int playerId, const char *direction) {
    snprintf(buffer, bufferSize, "SPC|MOVE|player=%d|dir=%s\n", playerId, direction);
}

void build_shot_command(char *buffer, int bufferSize, int playerId) {
    snprintf(buffer, bufferSize, "SPC|SHOT|player=%d\n", playerId);
}


void build_tick_command(char *buffer, int bufferSize) {
    snprintf(buffer, bufferSize, "SPC|TICK\n");
}


void build_disconnect_command(char *buffer, int bufferSize) {
    snprintf(buffer, bufferSize, "SPC|DISCONNECT\n");
}