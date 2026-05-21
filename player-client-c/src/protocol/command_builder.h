#ifndef COMMAND_BUILDER_H
#define COMMAND_BUILDER_H

void build_hello_command(char *buffer, int bufferSize, const char *role);

void build_ping_command(char *buffer, int bufferSize);

void build_move_command(char *buffer, int bufferSize, int playerId, const char *direction);

void build_shot_command(char *buffer, int bufferSize, int playerId);

void build_tick_command(char *buffer, int bufferSize);

void build_disconnect_command(char *buffer, int bufferSize);

#endif