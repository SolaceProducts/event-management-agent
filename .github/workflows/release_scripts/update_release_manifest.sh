#!/bin/bash

##The Variables below are required!

#Dynamo DB URL: https://us-east-1.console.aws.amazon.com/dynamodbv2/home?region=us-east-1#item-explorer?initialTagKey=&table=solace-cloud-manifest
#Example:
# export AWS_ACCESS_KEY_ID=xxxxxx
# export AWS_SECRET_ACCESS_KEY=xxxxx
# export release_version="v100.100"
# export version="v100"
# export image_tag="rc-abcdef"
# export chart_version="n/a"
# export sha="abcdef"
# export squad='mission-control'
# export repository='maas-cloud-agent-k8s'
# export release_tag='rc'

aws dynamodb update-item \
    --table-name solace-cloud-manifest \
    --key "{\"squad\":{\"S\":\"${squad}\"},\"repository\": {\"S\": \"${repository}\"} }"\
    --update-expression "SET ${release_tag} = :r" \
    --expression-attribute-values \
    """{\":r\":
            {\"M\":
                {
                  \"version\":{\"S\":\"${version}\"},
                  \"image_tag\":{\"S\":\"${image_tag}\"},
                  \"chart_version\": {\"S\": \"${chart_version}\"},
                  \"sha\": {\"S\": \"${sha}\"},
                  \"release_version\": {\"S\": \"${release_version}\"}

                }
            }
        }""" \
    --return-values ALL_NEW