#include <string.h>
#include "app/client_app.h"

int main(int argc, char *argv[]) {
    int spectatorMode = 0;

    if (argc > 1 && strcmp(argv[1], "spectator") == 0) {
        spectatorMode = 1;
    }

    run_client_app(spectatorMode);

    return 0;
}