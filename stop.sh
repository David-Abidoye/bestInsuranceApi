#!/usr/bin/env bash
# Use this line if you want to stop the containers without removing the volumes.
docker-compose -p bi_docker -f docker/docker-compose.yml down

# Use this line if you want to stop the containers and remove the volumes. Comment out the first line if you use this.
# docker-compose -p bi_docker -f docker/docker-compose.yml down -v --remove-orphans