#!/bin/bash

docker run -it --rm \
        -e TOPOLOGY_NAME=$1 \
        -e NIMBUS_HOST=storm-nimbus-1 \
        -e NIMBUS_THRIFT_PORT=6627 \
        --name topology \
        --network local \
        storm-topology \
        "kill"
