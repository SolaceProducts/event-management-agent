# Event Management Agent

The Event Management Agent is a tool for architects and developers working with Event-Driven Architectures (EDAs) to discover event streams flowing through event brokers as well as the related broker configuration information. It can also discover schemas from registries. The Event Management Agent can be used in two different ways:

* As the Event Management Agent component of the Solace’s PubSub+ Event Portal product to:
    - discover runtime event data from event brokers and schema registries
    - populate the Event Portal with the discovered runtime data to enable the management and reuse of EDA assets
    - audit the runtime data and flag discrepancies between the runtime and the design time intent for event data governance purposes, and ensure that the runtime and design time configurations stay in-sync
* As a standalone tool that discovers runtime event data from event or message brokers and schema registries.

Our plan is to open source the Event Management Agent to enable architects and developers to contribute to it as well a to build new plugins so that:

* the agent can discover data from different broker types
* existing plugins can be extended to discover additional data
* EDA data can be discovered from other systems, e.g. schemas from schema registries

At this stage (August 2023), the Event Management Agent is still in an active development phase.

## Available today:

* Users can discover Solace PubSub+ and Apache and Confluent Kafka brokers event flow data:
    - Users can discover Solace PubSub+ queues and subscriptions
    - Users can discover Apache and Confluent Kafka topics and consumer groups
* Users can discover Confluent Schema Registry schemas
* Users get discovered data in the form of JSON files separated by entity types
* The Event Management Agent architecture is currently in the form of Java packages

## On the roadmap:

* The Event Management Agent has an open source plugin framework
* Support additional Solace PubSub+ and Apache Kafka event broker authentication types in the form of plugins such as Kerberos, etc.
* Collection of topics from events flowing though Solace PubSub+ brokers
* Export discovered data as AsyncAPI specifications
* Addition of the infrastructure needed for the Event Management Agent to be a true open source project
* Discovery of Apache Kafka connectors
* Introduction of a UI for the Event Management Agent
* Additional support to more broker types
* Event Management Agent executables

# Running the Event Management Agent in connected mode

In this mode, the Event Management Agent connects to the Solace PubSub+ Event Portal. Scans are initiated from Event Portal and are automatically uploaded.

## Minimum hardware requirements

The Event Management Agent was tested to run with

* 1 CPU
* 1 GB RAM

## Prerequisites:

* Docker

## Downloading the Event Management Agent

The Event Management Agent must be installed in a location that have access to your event brokers and schema registries.

Pull the Event Management Agent Docker image from Docker Hub:
```
docker pull solace/event-management-agent
```

## Generating the Event Management Agent connection file

* Log in the Solace Cloud Console: https://console.solace.cloud/login/
* Follow the steps for generating the connection file described in the Solace Cloud documentation: https://docs.solace.com/Cloud/Event-Portal/event-portal-collect-runtime-data.htm#creating_connection_file
    - For security considerations, passwords should be configured as environment variables. Therefore, provide the environment variable names that will contain the passwords when generating the connection file.
* Download the connection file next to the Event Management Agent
* Create the environment variables containing the actual passwords you provided in place of the passwords when generating the connection file

## Running the Event Management Agent

Specify:
* the location of the connection file (e.g. /path/to/file/AcmeRetail.yml)
* the environment variables containing the authentication details of your event brokers and schema registries (e.g. KAFKA_PASSWORD)
* the name of the Event Management Agent container (e.g. event-managenement-agent)
* the Event Management Agent Docker image (e.g. solace/event-management-agent:latest)

```
docker run -d -p 8180:8180 -v /path/to/file/AcmeRetail.yml:/config/ema.yml --env KAFKA_PASSWORD --name event-management-agent solace/event-management-agent:latest
```

The Event Management Agent takes a couple of minutes to start. The Event Management Agent logs are available via Docker:

```
docker logs -f event-management-agent
```

## Running a Discovery scan

The Event Management Agent is now connected to the Solace Cloud Console.
Follow the steps in the documentation to run a Discovery scan: https://docs.solace.com/Cloud/Event-Portal/event-portal-collect-runtime-data.htm#collecting_runtime_data


# Running the Event Management Agent in standalone mode

## Getting the Event Management Agent

The Event Management Agent must have access to your event brokers and schema registries.

* pull the Event Management Docker image
```
docker pull solace/event-management-agent
```

* clone the Event Management Agent Github repository
```
git clone https://github.com/SolaceLabs/event-management-agent.git
cd event-management-agent
```

## Generating the connection file

* duplicate the connect file found in:
```
service/application/src/main/resources/application.yml
```
* uncomment the sections relevant to your event brokers and schema registries
* provide the authentication details required to connect to your event brokers and schema registries

## Running the Event Management Agent

Specify:
* the location of the connection file (e.g. /path/to/file/AcmeRetail.yml)
* the name of the Event Management Agent container (e.g. event-managenement-agent)
* the Event Management Agent Docker image (e.g. solace/event-management-agent:latest)

```
docker run -d -p 8180:8180 -v /path/to/file/AcmeRetail.yml:/config/ema.yml --name event-management-agent solace/event-management-agent:latest
```

The Event Management Agent takes a couple of minutes to start. The Event Management Agent logs are available via Docker:

```
docker logs -f event-management-agent
```

## Running Discovery scans

### REST interface

The Event Management Agent includes REST APIs that allow users to interact with the agent. See [REST Documentation](docs/rest.md) for additional information.

To run a scan:
* provide the id of the event broker you specified in the connection file, e.g. mykafkacluster
* provide the scan type, e.g. KAFKA_ALL
* provide the destination: FILE_WRITER

```
curl -X 'POST' \
  'http://localhost:8180/api/v2/ema/resources/mykafkacluster/scan' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "scanTypes": [
    "KAFKA_ALL"
  ],
  "destinations": [
    "FILE_WRITER"
  ]
}'
```

The agent will return a scan id.

## Exporting Discovery scans:

The Event Management Agent can export Discovery scans as zip files containing the entity type JSON files.

* provide the scan id, e.g. ih9z9lwwv5r
* provide the name of the zip file, e.g. ih9z9lwwv5r.zip 

```
curl -X 'GET' \
  'http://localhost:8180/api/v2/ema/resources/export/ih9z9lwwv5r/zip' \
  -H 'accept: */*' \
  --output ih9z9lwwv5r.zip
```

# Manually uploading scans to Event Portal

You can manually upload Discovery scan zip files to Event Portal. To do so:
* Scan and export Discovery scans following with a first Event Management Agent running in standalone mode: [Running the Event Management Agent in standalone mode](#running-the-event-management-agent-in-standalone-mode)
* Set up a second agent in connected mode following: [Running the Event Management Agent in connected mode](#running-the-event-management-agent-in-connected-mode)
* Upload your Discovery zip file with the following curl command, providing the name of the zip file, e.g. scan.zip:
```
curl -X 'POST' \
  'http://localhost:8180/api/v2/ema/resources/import' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@scan.zip;type=application/zip'
```

# Building the Event Management Agent

## Prerequisites

* Java 11 (JDK 11.0.14+)
* Maven

## Cloning the Github Event Management Agent repository

```
git@github.com:SolaceLabs/event-management-agent.git
```

### Installing Maven dependencies and building the Event Management Agent jar file

```
cd event-management-agent/service
mvn clean install
```

The generated Event Management Agent jar file is found in:

```
application/target/event-management-agent-1.0.0-SNAPSHOT.jar
```

### Running the Event Management Agent as a process

You can run the Event Management Agent as a process in both connected and standalone modes.

Specify:
* the Event Management Agent jar file (e.g. event-management-agent-1.0.0-SNAPSHOT.jar)
* the location of the connection file (e.g. /path/to/file/AcmeRetail.yml) - See [Generating the Event Management Agent connection file](#generating-the-event-management-agent-connection-file) to generate a connection file

```
java -jar application/target/event-management-agent-1.0.0-SNAPSHOT.jar --spring.config.location=/path/to/file/AcmeRetail.yml
```

The Event Management Agent is ready.

# Broker Plugins

The Event Management Agent comes with the following event or message broker plugins included:

* Apache Kafka
* Solace PubSub+
* Confluent Schema Registry
* MSK

The default application.yml provides various plugin examples. For Kafka, the properties section under credentials is passthrough. For example a property in ConsumerConfig or SSLConfigs classes.

If using AWS IAM, the AWS Access Key Id and AWS Secret Access Key need to be present. This can be done via a credentials file or environment variables as shown below:

Put a file with the following contents into a ~/.aws/credentials file

```
[default]
aws_access_key_id = <aws_access_key>
aws_secret_access_key = <aws_secret_key>
```

Can alternatively make these environment variables (these will also override the credentials file if present)

```
export AWS_ACCESS_KEY_ID=<aws_access_key>
export AWS_SECRET_ACCESS_KEY=<aws_secret_key>
```

## Motivations

See [motivations](./docs/motivations.md)

## Testing

There are several interesting scenarios to test the Event Management Agent. These scenarios can be divided into two main
categories according to the deployment mode.

* Testing the Event Management Agent as standalone service (stand-alone deployment).
* Testing the end-to-end flow in Solace PubSub+ Console connected mode (From the frontend to the Event Portal, then to
  the Event Management Agent)

### Testing the Event Management Agent in standalone mode

The most important test in standalone mode is to ensure that the Event Management Agent runs and collects data properly.
To that end, the test includes the steps below:

1. Update the `plugins` section of the `application.yml` with the details of the resource you want to scan.
2. Start the Event Management Agent either from the IDE or by running the JAR file.
3. Examine the on-console logs for a log from `ResourceConfig` class indicating that the resource(s)
   has been created. **Note**: The logs may differ according to the resource(s) specified in the
   `application.yml` file.

```
c.s.m.e.r.a.c.ResourceConfig : Created [kafka] resource with id:[sakdjf] and name: [some-name1]
c.s.m.e.r.a.c.ResourceConfig : Created [solace] resource with id:[hdfgkdjf] and name: [some-name2]
```

4. View the local Swagger documentation to learn about the available REST endpoints for the Event Management Agent. To access
   the Swagger documentation, use the link `http://localhost:8180/event-management-agent/swagger-ui/index.html` (Note:
   The Event Management Agent is under continuous development. Therefore, please check the Swagger documentation to make
   sure that you are using the recent endpoint schema).
5. Initiate a scan against a resource by sending a POST request to the endpoint that triggers the data
   collection `/api/v2/ema/resources/{resourceId}/scan`. The request can be sent using either Postman or
   a curl command.
6. Ensure that the `destinations` in the request body contains `FILE_WRITER`, i.e., `"destinations":["FILE_WRITER"]`,
   then send the request.
7. Confirm that you receive a scan id, e.g., `3a41a0f5-cd85-455c-a863-9636f69dc7b2`
8. Examine the Event Management Agent console logs to make sure that individual scan types are complete. e.g.,
   `Route subscriptionConfiguration completed for scanId 3a41a0f5-cd85-455c-a863-9636f69dc7b2`
9. Examine the collected data by browsing to the directory `data_collection`. This directory is organized as
   {schedule_id}/{scan_id}/{scan_type.json}
10. Verify that the collected data contains a separate JSON file for each scan type.
11. Verify the contents of each JSON file.
12. Check the logs by browsing to `data_collection/logs/{scan_id}.log` and `general-logs.log` to make sure no exceptions
    or errors occurred.
13. Finally, if you have added the `EVENT_PORTAL` as a destination, check the Event Portal tables to confirm they
    contain the scanned data.

## Contributions

Contributions are encouraged! If you have ideas to improve an existing plugin, create a new plugin, or improve/extend
the agent framework then please contribute!

## License 

Event Management Agent is available under the Apache License V2.


## Contributors

@gregmeldrum @slunelsolace @AHabes @MichaelDavisSolace @helrac @moodiRealist
