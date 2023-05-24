#!/usr/bin/env bash

if [ -f './init.gradle' ]; then
    docker build -t springk8s . --build-arg GRADLE_CONFIG='./init.gradle'
else
    docker build -t springk8s .
fi