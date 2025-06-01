#!/usr/bin/env bash
docker-compose -p bi_docker -f docker/docker-compose.yml down -v --remove-orphans
docker rmi davidabidoye/best-insurance-oauth2-server:latest davidabidoye/best-insurance-api:latest
./mvnw spring-boot:build-image && docker-compose -p bi_docker -f docker/docker-compose.yml up --build -d