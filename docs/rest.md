# REST interface

The Runtime Agent includes a REST API that allows the user to initiate a scan.

Each plugin requires its own custom set of authentication and identification attributes that must be supplied by the
user.

## Solace PubSub+

The PubSub+ plugin uses the SEMP API to collect configured and dynamic broker data such as information on queues,
clients and subscriptions.

## Kafka

The Kafka Confluent plugin uses the kafka broker admin APIs to learn about topics and consumer groups.

## Initiating a scan request

Update the `plugins` section of the `service/application/src/main/resources/application.yml` with the details of the messaging service to scan.

#### Semp Secured Connection

```
id: <Solace messaging service id>
name: <Solace messaging service name>
management:
  connections:
    - name: <Connection name>
      authenticationType: <Authentication type - basicAuthentication>
      url: <secure host and port> example:  https://<host>:<port>, https://xyz.messaging.solace.cloud:1943 
      msgVpn: <your vpn>
      users:
        - name: <semp user>
          username: <semp user name>
          password: <semp password> 
```

#### Kafka Management Connection - No Authentication

```
id: <Kafka messaging service id>
name: <Kafka messaging service name>
management:
  connections:
    - name: <Connection name>
      authenticationType: <Authentication type - NO_AUTH>
      bootstrapServer: kafka1:9091,kafka2:9091
      users:
        - name: <user name> 
```

Rebuild the Event Management Agent:

```
mvn clean install
```

Re-run the agent:

```
java -jar application/target/runtime-agent-0.0.1-SNAPSHOT.jar
```

To scan a PubSub+ Solace broker:

```
curl -H "Content-Type: application/json" -X POST http://localhost:8180/api/v2/runtime/messagingServices/{messaging service id}/scan -d '{"scanType": "SOLACE_ALL", "entityTypes": ["SOLACE_ALL"], "destinations":["FILE_WRITER"]}'
```

* PubSub+ messaging service with id 'y80bvdnyokl'

To scan a Apache Kafka broker:

```
curl -H "Content-Type: application/json" -X POST http://localhost:8180/api/v2/runtime/messagingServices/{messaging service id}/scan -d '{"scanType": "KAKFA_ALL", "entityTypes": ["KAFKA_ALL"], "destinations":["FILE_WRITER"]}'
```

* Kafka messaging service with id `bcvch3xfrq0`

The scan's output files can be found under 'service/data_collection/[group id]/[scan id]/'

```
queueConfiguration.json
queueListing.json
subscriptionConfiguration.json
```


## To get the Event Management Agent Swagger specification

Make sure the following section is in the 'service/application/src/main/resources/application.yml' file:

```
springdoc:
  packages-to-scan: com.solace.maas.ep.runtime.agent.scanManager.rest
  api-docs:
    path: /docs/runtime-agent
  swagger-ui:
    path: runtime-agent/swagger-ui.html
```

Rebuild and rerun the agent if needed.

Get the swagger specification:

```
curl -H "Content-Type: application/json" -X GET http://localhost:8180/docs/runtime-agent
```
