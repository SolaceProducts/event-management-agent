#!/usr/bin/env bash

set -Eeuo pipefail
trap cleanup SIGINT SIGTERM ERR EXIT

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P)

usage() {
  cat <<EOF
Usage: $(basename "${BASH_SOURCE[0]}") [-h] [-v] -t IMAGE_TAG [-bt docker_base_image_tag]

Creates a docker image of the event management agent. The event management agent docker image is made up
of a base image and the event-management-agent application layer. The base image tag can be specified
using the bt parameter. If the base image tag exists, it is used, if it does not exist, it
is built automatically before creating the application docker image.

Available options:

-h, --help      Print this help and exit
-v, --verbose   Print script debug info
-t, --tag      The docker container tag
EOF
  exit
}

cleanup() {
  trap - SIGINT SIGTERM ERR EXIT
  # script cleanup here
}

setup_colors() {
  if [[ -t 2 ]] && [[ -z "${NO_COLOR-}" ]] && [[ "${TERM-}" != "dumb" ]]; then
    NOFORMAT='\033[0m' RED='\033[0;31m' GREEN='\033[0;32m' ORANGE='\033[0;33m' BLUE='\033[0;34m' PURPLE='\033[0;35m' CYAN='\033[0;36m' YELLOW='\033[1;33m'
  else
    NOFORMAT='' RED='' GREEN='' ORANGE='' BLUE='' PURPLE='' CYAN='' YELLOW=''
  fi
}

msg() {
  echo >&2 -e "${1-}"
}

die() {
  local msg=$1
  local code=${2-1} # default exit status 1
  msg "$msg"
  exit "$code"
}

parse_params() {
  # default values of variables set from params
  flag=0
  param=''

  while :; do
    case "${1-}" in
    -h | --help) usage ;;
    -v | --verbose) set -x ;;
    --no-color) NO_COLOR=1 ;;
    -t | --tag)  # the docker tag
      IMAGE_TAG="${2-}"
      shift
      ;;
    -?*) die "Unknown option: $1" ;;
    *) break ;;
    esac
    shift
  done

  args=("$@")

  # check required params and arguments
  [[ -z "${IMAGE_TAG-}" ]] && die "Missing required parameter: tag"

  return 0
}

parse_params "$@"
setup_colors

msg "${GREEN}Building image:${YELLOW} event-management-agent:${IMAGE_TAG}\n${NOFORMAT}"

export BASE_IMAGE=eclipse-temurin:17.0.13_11-jre-alpine
export GITHASH=$(git rev-parse HEAD)
export GITBRANCH=$(git branch --show-current)
export BUILD_TIMESTAMP=$(date -u)
export JAR_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout --file ../pom.xml)
cp ../target/event-management-agent-${JAR_VERSION}.jar .

cd ..
docker build docker -t event-management-agent:${IMAGE_TAG} --platform linux/amd64 --build-arg BASE_IMAGE=${BASE_IMAGE}\
       --build-arg JAR_FILE=event-management-agent-${JAR_VERSION}.jar --build-arg GITHASH=${GITHASH}\
       --build-arg BUILD_TIMESTAMP="${BUILD_TIMESTAMP}" --build-arg GITBRANCH=${GITBRANCH}
cd ${script_dir}

# cleanup
rm event-management-agent-${JAR_VERSION}.jar
