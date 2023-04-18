#!/bin/bash

docker run -it --rm \
        -e BROKER_HOST=docker-kafka-storm-kafka-1 \
        -e BROKER_PORT=9092 \
        -e TOPIC=$1 \
        -e COUNTER_END=$2 \
        -e SLEEP_TIME_IN_MILLIS=$3 \
        --name producer \
        --network local \
        kafka-producer
