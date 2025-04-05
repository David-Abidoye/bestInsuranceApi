#!/usr/bin/env bash
docker-compose -f docker/docker-compose.yml down --remove-orphans
docker rmi davidabidoye/best-insurance-api:latest
./mvnw spring-boot:build-image && docker-compose -p bi_docker -f docker/docker-compose.yml up --build