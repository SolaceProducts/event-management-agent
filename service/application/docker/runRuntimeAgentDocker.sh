#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "usage: runRuntimeAgentDocker <tag>"
    exit 1
fi

docker run -d -p 8180:8180 --name runtime-agent runtime-agent:$1