#!/bin/bash

docker cp WordCountTopology.jar nimbus:/WordCountTopology.jar
case "$1" in
        local)
            docker exec -it nimbus storm jar /WordCountTopology.jar $2
            ;;

        kafka)
            docker cp kafka-clients-2.8.1.jar nimbus:/apache-storm-2.4.0/lib/
            docker exec -it nimbus \
                storm jar /WordCountTopology.jar $2 $3 zookeeper 2181 $4 \
                --jars "/apache-storm-2.4.0/lib/kafka-clients-2.8.1.jar"
            ;;

        *)
            echo $"Usage: $0 {submit|kill}"
            exit 1

esac
