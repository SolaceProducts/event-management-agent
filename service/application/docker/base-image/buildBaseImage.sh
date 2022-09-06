#!/bin/bash

if [ -z "$1" ]
  then
    echo "usage: buildBaseImage <tag>"
    exit 1
fi
docker build . -t runtime-agent-base:$1
