#!/bin/bash

docker run -it --rm \
        --name create-kafka-topic \
        --network local \
        wurstmeister/kafka \
        /bin/bash -c "/opt/kafka/bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor $1 --partitions $2 --topic $3"
