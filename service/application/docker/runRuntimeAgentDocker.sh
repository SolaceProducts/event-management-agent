#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "usage: runEMADocker <tag>"
    exit 1
fi

docker run -d -p 8180:8180 --name event-management-agent event-management-agent:$1