#!/bin/bash

# was trying to solve timeout issue, nothing helped, waiting solves nothing
# but at least its a good example of ENTRYPOINT script
sleep 3

exec "$@"
