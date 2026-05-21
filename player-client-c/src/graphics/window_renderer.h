#ifndef WINDOW_RENDERER_H
#define WINDOW_RENDERER_H

#include "../model/structs.h"

void init_game_window(int spectatorMode);

void close_game_window();

int should_close_game_window();

void render_game_window(const ClientGameState *state, int spectatorMode);

int is_left_pressed();
int is_right_pressed();
int is_shoot_pressed();
int is_exit_pressed();

#endif