#!/usr/bin/env bash

if [ -z "$2" ]
  then
    echo "usage: runEMADocker <tag> <configFile>"
    exit 1
fi

docker run -d -p 8180:8180 -v $2:/config/ema.yml --name event-management-agent event-management-agent:$1
