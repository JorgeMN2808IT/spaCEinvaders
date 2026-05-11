#include "raylib.h"
#include <string.h>

#include "window_renderer.h"
#include "../model/constants.h"

static int map_x(int worldX) {
    return GAME_AREA_X + ((worldX * GAME_AREA_WIDTH) / WORLD_WIDTH);
}

static int map_y(int worldY) {
    return GAME_AREA_Y + ((worldY * GAME_AREA_HEIGHT) / WORLD_HEIGHT);
}

void init_game_window() {
    InitWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "spaCEinvaders - Cliente Jugador");
    SetTargetFPS(60);
}

void close_game_window() {
    CloseWindow();
}

int should_close_game_window() {
    return WindowShouldClose();
}

int is_left_pressed() {
    return IsKeyPressed(KEY_A) || IsKeyPressed(KEY_LEFT);
}

int is_right_pressed() {
    return IsKeyPressed(KEY_D) || IsKeyPressed(KEY_RIGHT);
}

int is_shoot_pressed() {
    return IsKeyPressed(KEY_SPACE);
}

int is_exit_pressed() {
    return IsKeyPressed(KEY_ESCAPE);
}

static void draw_header(const ClientGameState *state) {
    DrawRectangle(0, 0, WINDOW_WIDTH, 80, (Color){18, 24, 38, 255});

    DrawText("spaCEinvaders", 30, 18, 28, RAYWHITE);

    DrawText(TextFormat("Score: %d", state->player.score), 300, 18, 22, RAYWHITE);
    DrawText(TextFormat("Vidas: %d", state->player.lives), 460, 18, 22, RAYWHITE);
    DrawText(TextFormat("Estado: %s", state->matchStatus), 590, 18, 22, RAYWHITE);

    DrawText(TextFormat("Aliens: %d", state->alienCount), 300, 48, 18, LIGHTGRAY);
    DrawText(TextFormat("Disparos: %d", state->projectileCount), 430, 48, 18, LIGHTGRAY);
    DrawText(TextFormat("Bunkers: %d", state->bunkerCount), 590, 48, 18, LIGHTGRAY);
    DrawText(TextFormat("Jugadores: %d", state->playerCount), 735, 48, 18, LIGHTGRAY);
}

static void draw_controls() {
    DrawText("Controles: A/← Izquierda | D/→ Derecha | Espacio Disparar | ESC Salir",
             40,
             WINDOW_HEIGHT - 32,
             18,
             DARKGRAY);
}

static void draw_game_area() {
    DrawRectangleLines(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_WIDTH, GAME_AREA_HEIGHT, DARKGRAY);
    DrawRectangle(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_WIDTH, GAME_AREA_HEIGHT, (Color){5, 8, 18, 255});
}

static void draw_players(const ClientGameState *state) {
    int index;

    for (index = 0; index < state->playerCount && index < MAX_PLAYERS; index++) {
        int x = map_x(state->players[index].x);
        int y = map_y(state->players[index].y);

        Color playerColor = SKYBLUE;

        if (state->players[index].id != state->player.id) {
            playerColor = GOLD;
        }

        DrawTriangle(
            (Vector2){x, y - 18},
            (Vector2){x - 18, y + 16},
            (Vector2){x + 18, y + 16},
            playerColor
        );

        DrawRectangle(x - 22, y + 12, 44, 8, BLUE);

        DrawText(
            TextFormat("P%d", state->players[index].id),
            x - 12,
            y + 24,
            14,
            RAYWHITE
        );
    }
}

static void draw_aliens(const ClientGameState *state) {
    int index;

    for (index = 0; index < state->alienCount && index < MAX_ALIENS; index++) {
        int x = map_x(state->aliens[index].x);
        int y = map_y(state->aliens[index].y);

        Color alienColor = GREEN;

        if (strcmp(state->aliens[index].type, "SQUID") == 0) {
            alienColor = LIME;
        } else if (strcmp(state->aliens[index].type, "CRAB") == 0) {
            alienColor = ORANGE;
        } else if (strcmp(state->aliens[index].type, "OCTOPUS") == 0) {
            alienColor = PURPLE;
        }

        DrawRectangleRounded(
            (Rectangle){x - 13, y - 10, 26, 20},
            0.35f,
            6,
            alienColor
        );

        DrawCircle(x - 6, y - 2, 2, BLACK);
        DrawCircle(x + 6, y - 2, 2, BLACK);
    }
}

static void draw_projectiles(const ClientGameState *state) {
    int index;

    for (index = 0; index < state->projectileCount && index < MAX_PROJECTILES; index++) {
        int x = map_x(state->projectiles[index].x);
        int y = map_y(state->projectiles[index].y);

        if (strcmp(state->projectiles[index].owner, "PLAYER") == 0) {
            DrawRectangle(x - 2, y - 12, 4, 18, YELLOW);
        } else {
            DrawRectangle(x - 2, y - 6, 4, 14, RED);
        }
    }
}

static void draw_bunkers(const ClientGameState *state) {
    int index;

    for (index = 0; index < state->bunkerCount && index < MAX_BUNKERS; index++) {
        int x = map_x(state->bunkers[index].x);
        int y = map_y(state->bunkers[index].y);

        Color bunkerColor = GREEN;

        if (state->bunkers[index].health <= 30) {
            bunkerColor = RED;
        } else if (state->bunkers[index].health <= 70) {
            bunkerColor = YELLOW;
        }

        DrawRectangleRounded(
            (Rectangle){x - 35, y - 12, 70, 28},
            0.25f,
            8,
            bunkerColor
        );

        DrawText(
            TextFormat("%d%%", state->bunkers[index].health),
            x - 18,
            y - 5,
            14,
            BLACK
        );
    }
}

static void draw_ufos(const ClientGameState *state) {
    int index;

    for (index = 0; index < state->ufoCount && index < MAX_UFOS; index++) {
        int x = map_x(state->ufos[index].x);
        int y = map_y(state->ufos[index].y);

        DrawEllipse(x, y, 32, 12, RED);
        DrawCircle(x, y - 6, 10, MAROON);
        DrawText(TextFormat("+%d", state->ufos[index].points), x - 16, y + 18, 14, RAYWHITE);
    }
}

void render_game_window(const ClientGameState *state) {
    BeginDrawing();

    ClearBackground((Color){235, 238, 245, 255});

    draw_header(state);
    draw_game_area();

    draw_ufos(state);
    draw_aliens(state);
    draw_projectiles(state);
    draw_bunkers(state);
    draw_players(state);

    draw_controls();

    EndDrawing();
}