#!/bin/bash

docker run -it --rm \
        --name=kafka-console-consumer \
        --network local \
        wurstmeister/kafka /bin/bash -c "/kafka/bin/kafka-console-consumer.sh --zookeeper docker-kafka-storm-zookeeper-1:2181 --topic $1"
