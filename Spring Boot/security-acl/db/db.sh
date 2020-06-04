#!/usr/bin/env bash

export PGPASSWORD='123456'

docker-compose -f db.yml down -v
docker-compose -f db.yml up -d
sleep 5
psql -h localhost -p 5432 -f db.sql -U demo