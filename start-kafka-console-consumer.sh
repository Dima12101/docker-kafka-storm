#!/bin/bash

docker run -it --rm \
        --name=kafka-console-consumer \
        --network local \
        ches/kafka /bin/bash -c "/kafka/bin/kafka-console-consumer.sh --zookeeper zookeeper:2181 --topic $1"
