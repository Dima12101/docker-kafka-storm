#!/bin/bash

docker exec -it supervisor tail -f /var/log/storm/worker-6702.log
