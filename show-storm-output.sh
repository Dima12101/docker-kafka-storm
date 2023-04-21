#!/bin/bash

# docker exec -it supervisor tail -f /var/log/storm/worker-6702.log
docker exec -it supervisor tail -f cat /logs/workers-artifacts/worker-6702.log