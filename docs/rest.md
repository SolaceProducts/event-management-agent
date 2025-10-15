# REST interface

The Event Management Agent includes a REST API that allows the user to initiate a scan.

Each plugin requires its own custom set of authentication and identification attributes that must be supplied by the
user.

## Solace

The Solace plugin uses the SEMP API to collect configured and dynamic broker data such as information on queues,
clients and subscriptions.

## Kafka

The Kafka Confluent plugin uses the Kafka broker admin APIs to learn about topics and consumer groups.

## Confluent Schema Registry

The Confluent Schema Registry plugin uses the ConfluentSchemaRegistry API to learn about schemas.

## Initiating a scan request

Download the configuration file generated in the Event Portal or update the `plugins` section of the
`service/application/src/main/resources/application.yml` file with the details of the resource to scan. Values
that include `${SOME_VALUE}` are environment variables and should be exported in the shell before launching the EMA. An
example of exporting these values appears in the `Environment Variables` section.

The default application.yml file also includes examples of the plugin configurations.

### SEMP Secured Connection

The following environment variables need to be exported:

- ${SOLACE_SEMP_USERNAME}
- ${SOLACE_SEMP_PASSWORD}
- ${SOLACE_VPN_NAME}

```
plugins:
  resources:
    - id: <Solace event broker id>
      type: solace
      name: <Solace event broker name>
      connections:
        - name: <Connection name>
          url: <secure host and port> example:  https://<host>:<port>, https://xyz.messaging.solace.cloud:1943 
          authentication:
            - protocol: SEMP
              credentials:
                - source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
                  properties:
                    - name: username
                      value: ${SOLACE_SEMP_USERNAME}
                    - name: password
                      value: ${SOLACE_SEMP_PASSWORD}
          properties:
            - name: msgVpn
              value: ${SOLACE_VPN_NAME}
            - name: sempPageSize
              value: 100
```

### Kafka Management Connection - No Authentication

```
plugins:
  resources:
    - id: <Kafka event broker id>
      name: <Kafka event broker name>
      type: kafka
      connections:
        - name: <Kafka connection name>
          url: ${KAFKA_BOOTSTRAP_SERVERS:kafka1:12091,kafka2:12092}
```

### Kafka Management Connection - MTLS

The following environment variables need to be exported:

- ${TRUSTSTORE_LOCATION}
- ${TRUSTSTORE_PASSWORD}
- ${KEYSTORE_PASSWORD}
- ${KEYSTORE_LOCATION}
- ${KEY_PASSWORD}

```
plugins:
  resources:
    - id: <Kafka event broker id>
      name: <Kafka event broker name>
      type: kafka
      connections:
        - name: <Kafka connection name>
          url: ${KAFKA_BOOTSTRAP_SERVERS:kafka1:11091,kafka2:11092}
          authentication:
            - protocol: SSL
              credentials:
                - source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
                  properties:
                    - name: ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: ssl.truststore.password
                      value: ${TRUSTSTORE_PASSWORD}
                    - name: ssl.keystore.password
                      value: ${KEYSTORE_PASSWORD}
                    - name: ssl.keystore.location
                      value: ${KEYSTORE_LOCATION}
                    - name: ssl.key.password
                      value: ${KEY_PASSWORD}
```

### Kafka Management Connection - SASL SCRAM 256

The following environment variables need to be exported (You can also use environment variables for the JAAS config
username and password):

- ${TRUSTSTORE_LOCATION}
- ${TRUSTSTORE_PASSWORD}

```
plugins:
  resources:
    - id: <Kafka event broker id>
      name: <Kafka event broker name>
      type: kafka
      connections:
        - name: <Kafka connection name>
          url: ${KAFKA_BOOTSTRAP_SERVERS:kafka1:14091,kafka2:14092}
          authentication:
            - protocol: SASL_SSL
              credentials:
                - source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
                  properties:
                    - name: ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: ssl.truststore.password
                      value: ${TRUSTSTORE_PASSWORD}
                    - name: sasl.jaas.config
                      value: org.apache.kafka.common.security.scram.ScramLoginModule required username=<username> password=<password>;
                    - name: sasl.mechanism
                      value: SCRAM-SHA-256
```

### Kafka Management Connection - SASL Plain

You can also use environment variables for the JAAS config username and password.

```
plugins:
  resources:
    - id: <Kafka event broker id>
      name: <Kafka event broker name>
      type: kafka
      connections:
        - name: <Kafka connection name>
          url: ${KAFKA_BOOTSTRAP_SERVERS:kafka1:9091,kafka2:9092}
          authentication:
            - protocol: SASL_PLAINTEXT
              credentials:
                - source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
                  properties:
                    - name: sasl.mechanism
                      value: PLAIN
                    - name: sasl.jaas.config
                      value: org.apache.kafka.common.security.plain.PlainLoginModule required username="<username>>" password="<password>";
```

### Kafka Management Connection - SASL Plain over SSL

The following environment variable needs to be exported (You can also use environment variables for the JAAS config
username and password):

- ${TRUSTSTORE_LOCATION}

```
plugins:
  resources:
    - id:  <Kafka event broker id>
      name: <Kafka event broker name>
      type: kafka
      connections:
        - name: <Kafka connection name>
          url: ${KAFKA_BOOTSTRAP_SERVERS:kafka1:13091,kafka2:13092}
          authentication:
            - protocol: SASL_SSL
              credentials:
                - source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
                  properties:
                    - name: ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: sasl.mechanism
                      value: PLAIN
                    - name: sasl.jaas.config
                      value: org.apache.kafka.common.security.plain.PlainLoginModule required username="<username>>" password="<password>";
```

### Kafka Management Connection - AWS IAM

The following environment variable needs to be exported:

- ${TRUSTSTORE_LOCATION}

```
plugins:
  resources:
    - id: <Kafka event broker id>
      name: <Kafka event broker name>
      type: kafka
      connections:
        - name: <Kafka connection name>
          url: ${KAFKA_BOOTSTRAP_SERVERS:awsservers:9098}
          authentication:
            - protocol: SASL_SSL
              credentials:
                - source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
                  properties:
                    - name: ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: security.protocol
                      value: SASL_SSL
                    - name: sasl.mechanism
                      value: AWS_MSK_IAM
                    - name: sasl.jaas.config
                      value: software.amazon.msk.auth.iam.IAMLoginModule required;
                    - name: sasl.client.callback.handler.class
                      value: software.amazon.msk.auth.iam.IAMClientCallbackHandler
```

### Confluent Schema Registry Connection - No Authentication

```
plugins:
  resources:
    - id: <Confluent Schema Registry resource id>
      name: <Confluent Schema Registry name>
      type: confluent_schema_registry
      relatedServices: list of kafka services linked to this schema registry, example: [ id1,id2,id3 ]
      connections:
        - name: <Confluent Schema Registry connection name>
          url: <host and port> example:  http://<host>:<port>
```

### Confluent Schema Registry Connection - Basic Authentication

The following environment variable needs to be exported:

- ${CONFLUENT_USERNAME}
- ${CONFLUENT_PASSWORD}


```
plugins:
  resources:
    - id: <Confluent Schema Registry resource id>
      name: <Confluent Schema Registry name>
      type: confluent_schema_registry
      relatedServices: list of kafka services linked to this schema registry, example: [ id1,id2,id3 ]
      connections:
        - name: <Confluent Schema Registry connection name>
          url: <host and port> example:  http://<host>:<port>
          authentication:
            - protocol: BASIC
              credentials:
                - properties:
                    - name: username
                      value: ${CONFLUENT_USERNAME}
                    - name: password
                      value: ${CONFLUENT_PASSWORD}
                  source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
```

### Confluent Schema Registry Connection - Basic Authentication With SSL

The following environment variable needs to be exported:

- ${CONFLUENT_USERNAME}
- ${CONFLUENT_PASSWORD}
- ${TRUSTSTORE_LOCATION}
- ${TRUSTSTORE_PASSWORD}


```
plugins:
  resources:
    - id: <Confluent Schema Registry resource id>
      name: <Confluent Schema Registry name>
      type: confluent_schema_registry
      relatedServices: list of kafka services linked to this schema registry, example: [ id1,id2,id3 ]
      connections:
        - name: <Confluent Schema Registry connection name>
          url: <host and port> example:  https://<host>:<port>
          authentication:
            - protocol: BASIC_SSL
              credentials:
                - properties:
                    - name: username
                      value: ${CONFLUENT_USERNAME}
                    - name: password
                      value: ${CONFLUENT_PASSWORD}
                    - ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: ssl.truststore.password
                      value: ${TRUSTSTORE_PASSWORD}
                  source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
```

### Confluent Schema Registry Connection - Basic Authentication With MTLS

The following environment variable needs to be exported:

- ${CONFLUENT_USERNAME}
- ${CONFLUENT_PASSWORD}
- ${TRUSTSTORE_LOCATION}
- ${TRUSTSTORE_PASSWORD}
- ${KEYSTORE_LOCATION}
- ${KEYSTORE_PASSWORD}
- ${KEY_PASSWORD}


```
plugins:
  resources:
    - id: <Confluent Schema Registry resource id>
      name: <Confluent Schema Registry name>
      type: confluent_schema_registry
      relatedServices: list of kafka services linked to this schema registry, example: [ id1,id2,id3 ]
      connections:
        - name: <Confluent Schema Registry connection name>
          url: <host and port> example:  https://<host>:<port>
          authentication:
            - protocol: BASIC_MTLS
              credentials:
                - properties:
                    - name: username
                      value: ${CONFLUENT_USERNAME}
                    - name: password
                      value: ${CONFLUENT_PASSWORD}
                    - ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: ssl.truststore.password
                      value: ${TRUSTSTORE_PASSWORD}
                    - name: ssl.keystore.password
                      value: ${KEYSTORE_PASSWORD}
                    - name: ssl.keystore.location
                      value: ${KEYSTORE_LOCATION}
                    - name: ssl.key.password
                      value: ${KEY_PASSWORD}
                  source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
```

### Confluent Schema Registry Connection - SSL with TrustStore

The following environment variable needs to be exported:

- ${TRUSTSTORE_LOCATION}
- ${TRUSTSTORE_PASSWORD}


```
plugins:
  resources:
    - id: <Confluent Schema Registry resource id>
      name: <Confluent Schema Registry name>
      type: confluent_schema_registry
      relatedServices: list of kafka services linked to this schema registry, example: [ id1,id2,id3 ]
      connections:
        - name: <Confluent Schema Registry connection name>
          url: <host and port> example:  https://<host>:<port>
          authentication:
            - protocol: TLS
              credentials:
                - properties:
                    - ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: ssl.truststore.password
                      value: ${TRUSTSTORE_PASSWORD}
                  source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
```

### Confluent Schema Registry Connection - SSL with TrustStore and KeyStore

The following environment variable needs to be exported:

- ${TRUSTSTORE_LOCATION}
- ${TRUSTSTORE_PASSWORD}
- ${KEYSTORE_LOCATION}
- ${KEYSTORE_PASSWORD}
- ${KEY_PASSWORD}


```
plugins:
  resources:
    - id: <Confluent Schema Registry resource id>
      name: <Confluent Schema Registry name>
      type: confluent_schema_registry
      relatedServices: list of kafka services linked to this schema registry, example: [ id1,id2,id3 ]
      connections:
        - name: <Confluent Schema Registry connection name>
          url: <host and port> example:  https://<host>:<port>
          authentication:
            - protocol: TLS
              credentials:
                - properties:
                    - ssl.truststore.location
                      value: ${TRUSTSTORE_LOCATION}
                    - name: ssl.truststore.password
                      value: ${TRUSTSTORE_PASSWORD}
                    - name: ssl.keystore.password
                      value: ${KEYSTORE_PASSWORD}
                    - name: ssl.keystore.location
                      value: ${KEYSTORE_LOCATION}
                    - name: ssl.key.password
                      value: ${KEY_PASSWORD}
                  source: ENVIRONMENT_VARIABLE
                  operations:
                    - name: ALL
```


### Environment Variables

When using environment variables, they must all be exported into the environment, otherwise the value will be empty.
Solace recommends storing usernames and passwords in environment variables to avoid storing them in the Event Portal.

```
export SOLACE_SEMP_USERNAME=solace-username
export SOLACE_SEMP_PASSWORD=solace-password
...
```

KAFKA_BOOTSTRAP_SERVERS can optionally be overridden

## Building And Executing The EMA

Rebuild the Event Management Agent:

```
mvn clean install
```

Re-run the agent:

```
java -jar application/target/event-management-agent-0.0.1-SNAPSHOT.jar
```

# Running Scans

## Scan Types

Different scan types are available. Each will collect specific items. Scan types are passed on in the body of API calls, e.g.:
```
"scanTypes": ["SOLACE_ALL"]
```

Solace:

- SOLACE_QUEUE_CONFIG: Solace queue configurations
- SOLACE_QUEUE_LISTING: Solace queue listing
- SOLACE_SUBSCRIPTION_CONFIG: Solace subscription configurations
- SOLACE_ALL: All of the above

Kafka:

- KAFKA_BROKER_CONFIGURATION: Kafka broker configurations
- KAFKA_CLUSTER_CONFIGURATION: Kafka cluster configurations
- KAFKA_CONSUMER_GROUPS: Kafka consumer groups
- KAFKA_CONSUMER_GROUPS_CONFIGURATION: Kafka consumer group configurations
- KAFKA_FEATURES: Kafka feature
- KAFKA_PRODUCERS: Kafka producers
- KAFKA_TOPIC_CONFIGURATION: Kafka topic configurations
- KAFKA_TOPIC_CONFIGURATION_FULL: Kafka topic configurations full
- KAFKA_TOPIC_LISTING: Kafka topic listing
- KAFKA_TOPIC_OVERRIDE_CONFIGURATION: Kafka topic override configurations
- KAFKA_ALL: All of the above

Confuelt Schema Registry:
- CONFLUENT_SCHEMA_REGISTRY_SCHEMA: Confluent Registry schemas

## Destinations

There are two destinations: EVENT_PORTAL and FILE_WRITER. FILE_WRITER writes the data to a file and EVENT_PORTAL sends
data to Event Portal.

## Scanning

To scan a Solace broker:

```
curl -H "Content-Type: application/json" -X POST http://localhost:8180/api/v2/ema/resources/{resource id}/scan -d '{"scanTypes": ["SOLACE_ALL"], "destinations":["FILE_WRITER"]}'
```

* Solace event broker with id 'y80bvdnyokl'

To scan an Apache Kafka broker:

```
curl -H "Content-Type: application/json" -X POST http://localhost:8180/api/v2/ema/resources/{resource id}/scan -d '{"scanTypes": ["KAFKA_ALL"], "destinations":["FILE_WRITER"]}'
```

* Kafka event broker with id `bcvch3xfrq0`

A scan's output files can be found under 'service/data_collection/[group id]/[scan id]/'

Sample of output files (for Solace):

```
brokerConfiguration.json
clusterConfiguration.json
consumerGroupConfiguration.json
consumerGroups.json
overrideTopicConfiguration.json
topicConfiguration.json
topicListing.json
```

## To get the Event Management Agent Swagger specification

Make sure the following section is in the 'service/application/src/main/resources/application.yml' file:

```
springdoc:
  packages-to-scan: com.solace.maas.ep.event.management.agent.scanManager.rest
  api-docs:
    path: /docs/event-management-agent
  swagger-ui:
    path: /event-management-agent/swagger-ui.html
```

Rebuild and rerun the agent if needed.

Get the swagger specification:

```
curl -H "Content-Type: application/json" -X GET http://localhost:8180/docs/event-management-agent
```

## To list scans

This returns a list of items that have been scanned.

```
curl -H "Content-Type: application/json" -X GET http://localhost:8180/api/v2/ema/scan
```

Output

```
[
  {
    "id": "bgjg5ajz8vo",
    "messagingServiceId": "solaceDefaultService",
    "messagingServiceName": "staging service",
    "messagingServiceType": "SOLACE",
    "createdAt": "2023-01-10T16:40:10.972787Z"
  }
]
```

## Manual import

1. Export endpoint:

```
curl -H "Content-Type: application/json" -X GET http://localhost:8180/api/v2/ema/resources/export/fi8q4sjotjf/zip --output {file_name.zip}
```

Output

A new file with the following naming convention

```
{scanId}.zip
```

2. Import endpoint:

```
curl --location --request POST 'http://localhost:8180/api/v2/ema/resources/import' \
--form 'file=@"{full path to the .zip file}'
```
