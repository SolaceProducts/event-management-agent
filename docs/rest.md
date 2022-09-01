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

A scan is initiated with a POST to a fixed URL:

```
POST http://localhost:8120/api/v0/event-discovery-agent/local/app/operation

POST http://localhost:8180/api/v2/runtime/messagingServices/{messaging service Id}/scan
```

with a header of `Content-Type: application/json`

Update the `plugins` section of the `application.yml` with the details of the messaging service to scan.

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

### Example

To initiate a scan against

* Solace messaging service with id `solaceDefaultService`

```
POST http://localhost:8180/api/v2/runtime/messagingServices/solaceDefaultService/scan
{
	"scanType": "one-time",
	"entityTypes": ["SOLACE_ALL"],
	"destinations":["DATA_COLLECTION_FILE_WRITER"]
}
```

* Kafka messaging service with id `kafkaDefaultService`

```
POST  http://localhost:8180/api/v2/runtime/messagingServices/kafkaDefaultService/scan

{
	"scanType": "one-time",
	"entityTypes": ["KAFKA_ALL"],
	"destinations":["DATA_COLLECTION_FILE_WRITER"]
}
```
