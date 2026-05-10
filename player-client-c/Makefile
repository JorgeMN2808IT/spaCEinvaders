CC = gcc
CFLAGS = -Wall -Wextra -std=c11

SRC = src/main.c \
      src/app/client_app.c \
      src/net/socket_client.c \
      src/protocol/command_builder.c

OUT_DIR = bin
OUT = $(OUT_DIR)/player-client

ifeq ($(OS),Windows_NT)
	OUT = $(OUT_DIR)/player-client.exe
	LIBS = -lws2_32
else
	LIBS =
endif

all: $(OUT)

$(OUT): $(SRC)
	@mkdir -p $(OUT_DIR)
	$(CC) $(CFLAGS) $(SRC) -o $(OUT) $(LIBS)

run: all
	./$(OUT)

clean:
	rm -rf $(OUT_DIR)