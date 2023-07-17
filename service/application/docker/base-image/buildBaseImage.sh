#!/bin/bash

if [ -z "$1" ]
  then
    echo "usage: buildBaseImage <tag>"
    exit 1
fi
docker build .  --platform linux/amd64  -t event-management-agent-base:$1
