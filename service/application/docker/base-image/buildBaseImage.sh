#!/bin/bash

if [ -z "$1" ]
  then
    echo "usage: buildBaseImage <tag>"
    exit 1
fi
docker build . -t 868978040651.dkr.ecr.us-east-1.amazonaws.com/runtime-agent-base:$1
#docker push 868978040651.dkr.ecr.us-east-1.amazonaws.com/runtime-agent-base:$1