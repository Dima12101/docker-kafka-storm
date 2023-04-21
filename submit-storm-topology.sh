#!/bin/bash

docker cp WordCountTopology.jar nimbus:/WordCountTopology.jar
docker exec -it nimbus \
        storm jar /WordCountTopology.jar $1 $2 zookeeper 2181 $3
