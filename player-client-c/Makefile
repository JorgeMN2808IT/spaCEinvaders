CC = gcc
CFLAGS = -Wall -Wextra -std=c11

SRC = src/main.c \
      src/app/client_app.c \
      src/net/socket_client.c \
      src/protocol/command_builder.c \
      src/protocol/state_decoder.c \
      src/model/client_state.c \
      src/graphics/window_renderer.c

OUT_DIR = bin
OUT = $(OUT_DIR)/player-client

ifeq ($(OS),Windows_NT)
	OUT = $(OUT_DIR)/player-client.exe
	LIBS = -lraylib -lopengl32 -lgdi32 -lwinmm -lws2_32
else
	LIBS = -lraylib -lm -ldl -lpthread -lGL
endif

all: $(OUT)

$(OUT): $(SRC)
	@mkdir -p $(OUT_DIR)
	$(CC) $(CFLAGS) $(SRC) -o $(OUT) $(LIBS)

run: all
	./$(OUT)

clean:
	rm -rf $(OUT_DIR)