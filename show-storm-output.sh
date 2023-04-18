#!/bin/bash

docker exec -it docker-kafka-storm-supervisor-1 tail -f /var/log/storm/worker-6702.log
