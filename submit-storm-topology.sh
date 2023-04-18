#!/bin/bash

docker run -it --rm \
        -e MAINCLASS=$1 \
        -e TOPOLOGY_NAME=$2 \
        -e TOPIC=$3 \
        -e ZK_HOST=docker-kafka-storm-zookeeper-1 \
        -e ZK_PORT=2181 \
        -e NIMBUS_HOST=docker-kafka-storm-nimbus-1 \
        -e NIMBUS_THRIFT_PORT=6627 \
        --name topology-3 \
        --network local \
        storm-topology \
        /bin/bash /usr/src/storm-topology/start.sh \
        "submit"
