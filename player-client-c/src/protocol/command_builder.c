#include <stdio.h>
#include "command_builder.h"
#include "../model/constants.h"

/*
 * Construye el mensaje inicial de presentación del cliente.
 */
void build_hello_command(char *buffer, int bufferSize) {
    snprintf(buffer, bufferSize, "SPC|HELLO|role=%s\n", CLIENT_ROLE);
}

/*
 * Construye un mensaje PING.
 */
void build_ping_command(char *buffer, int bufferSize) {
    snprintf(buffer, bufferSize, "SPC|PING\n");
}

/*
 * Construye un mensaje para cerrar la conexión.
 */
void build_disconnect_command(char *buffer, int bufferSize) {
    snprintf(buffer, bufferSize, "SPC|DISCONNECT\n");
}