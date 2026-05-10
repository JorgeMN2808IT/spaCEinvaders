# spaCEinvaders

Commands:

Initialize Java Server (Path: \spaCEinvaders): 
java -cp core-server/out com.spaceinvaders.Launcher
Initialize Launcher Client from C (Path:spaCEinvaders\player-client-c):
gcc -Wall -Wextra -std=c11 src/main.c src/app/client_app.c src/net/socket_client.c src/protocol/command_builder.c -o bin/player-client.exe -lws2_32
.\bin\player-client.exe