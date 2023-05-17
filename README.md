# Event Management Agent

The Event Management Agent is a tool used by architects and developers working with Event-Driven Architectures  (EDAs)
to discover event streams flowing through an event broker as well as the related broker configuration information. The
Event Management Agent can be used in two different ways:

* As a standalone tool that discovers runtime event data from event or message brokers in the runtime to retrieve EDA
  related data. This data can be exported as an AsyncAPI specification for the broker service to document the event flow
  information and be used with other tools supporting AsyncAPI specifications.
* As the Event Management Agent component of the Solace’s PubSub+ Event Portal product to:
    - discover runtime event data from event brokers
    - populate the Event Portal Designer and Catalog services with EDA data from the runtime enabling the management and
      reuse of EDA assets
    - audit the runtime data and flag discrepancies between the runtime and the design time intent for event data
      governance purposes, and ensure that the runtime and design time configurations stay in-sync

Our plan is to open source the Event Management Agent to enable architects and developers to contribute to it as well as
to build new plugins so that:

* runtime data can be discovered from additional broker types
* existing plugins can be extended to discover additional data
* EDA data can be discovered from other systems, e.g. schemas from schema registries

At this stage (May 2023), the Event Management Agent is still in an active development phase.

### Available today:

* Users can discover Solace PubSub+ and Apache Kafka brokers event flow data
    - Users can discover Solace PubSub+ queues and subscriptions
    - Users can discover Apache Kafka topics and consumer groups
* Users can discover Confluent Schema Registry schemas
* Users get discovered data in the form of JSON files separated by entity types
* The Event Management Agent architecture is currently in the form of Java packages

On the roadmap:

* Support for the Confluent flavour of Apache Kafka
* The Event Management Agent has an open source plugin framework
* Support additional Solace PubSub+ and Apache Kafka event broker authentication types in the form of plugins such as
  Kerberos, etc.
* Collection of topics from events flowing though Solace PubSub+ brokers
* Export discovered data as AsyncAPI specifications
* Addition of the infrastructure needed for the Event Management Agent to be a true open source project
* Discovery of Apache Kafka connectors
* Introduction of a UI for the Event Management Agent
* Additional support to more broker types
* Event Management Agent Docker images
* Event Management Agent executables

## Running the Event Management Agent in connected mode

### Minimum hardware requirements

The Event Management Agent was tested to run with

* 1 CPU
* 1 GB RAM

### Prerequisites:

* Java 11 (JDK 11.0.14+)
* Maven
* Docker (recommended)

### Cloning the Event Management Agent repository

The Event Management Agent must have access to the event broker(s).

```
git clone https://github.com/SolaceLabs/event-management-agent.git
```

### Generating the Event Management Agent connection file

* Log in the Solace Cloud Console: https://console.solace.cloud/login/
* Follow the steps for generating the connection file described in the Solace Cloud documentation: https://docs.solace.com/Cloud/Event-Portal/event-portal-collect-runtime-data.htm#creating_connection_file
    - For security considerations, passwords should be configured as environment variables. Therefore, provide the environment variables that will contain the passwords when generating the connection file.
* Download the connection file and add it to the Event Management Agent directory
    - Replace the application.yml present in the location: event-management-agent/service/application/src/main/resources with the connection file
    - Or place the connection file anywhere and pass its path to the agent when starting it
* Create the environment variables containing the passwords you provided when generating the connection file.

### Installing Maven dependencies

```
cd event-management-agent/service
mvn clean install
```

### Running the Event Management Agent as a process (recommended for testing and proof of concept purposes only)

Specify the location of the configuration file if not using the default location (e.g. configs/AcmeRetail.yml)

```
java -jar application/target/event-management-agent-1.0.0-SNAPSHOT.jar --spring.config.location=configs/AcmeRetail.yml
```

### Running the Event Management Agent as a Docker container (recommended)

#### Building the Event Management Agent

Provide a tag for the Docker image (e.g. v1)

```
cd event-management-agent/service/application/docker
./buildEventManagementAgentDocker.sh -t v1
```

NB: Specify the Docker OS base image to use if required by editing the event-management-agent/service/application/docker/base-image/Dockerfile file

#### Passing the environment variables containing the passwords

Edit the following script to add the environment variables containing the passwords to the Docker container

```
service/application/docker/runEventManagementAgentDocker.sh
```

#### Starting the Event Management Agent

Provide the Docker image tag (e.g. v1), the location of the configuration file (e.g. /tmp/configFiles/perf1-ema.yml)

```
./runEventManagementAgentDocker.sh v1 /tmp/configFiles/perf1-ema.yml
```

### Running a Discovery scan

The Event Management Agent is now connected to the Solace Cloud Console.
Follow the steps in the documentation to run a Discovery scan: https://docs.solace.com/Cloud/Event-Portal/event-portal-collect-runtime-data.htm#collecting_runtime_data


## Broker Plugins

The Event Management Agent comes with the following event or message broker plugins included:

* Apache Kafka
* Solace PubSub+
* Confluent Schema Registry
* MSK

The default application.yml provides various plugin examples. For Kafka, the properties section under credentials is
passthrough. For example a property in ConsumerConfig or SSLConfigs classes.

If using AWS IAM, the AWS Access Key Id and AWS Secret Access Key need to be present. This can be done via environment
or credentials file as shown below:

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

## Deployment

There are two main modes of deployment:

* Connected: The Event Management Agent connects to the Solace PubSub+ Event Portal.

* Standalone: The Event Management Agent is controlled via the REST API and results must be uploaded manually.

## Running a scan

### REST interface

The Event Management Agent includes a REST API that allows the user to initiate a scan. Each plugin requires its own
custom set of authentication and identification attributes that must be supplied by the user.

See [REST Documentation](docs/rest.md) for additional information

## Importing Scanned Data

To import scanned data into Event Portal:

1. Set up a new standalone Event Management Agent.
2. Run a scan according to the instructions here: [Running Scans](docs/rest.md#running-scans)
3. After the scan is complete, create a .zip file containing the scan files by sending a GET request to the
  endpoint `http://localhost:8180/api/v2/ema/resources/export/{scanId}/zip`
4. Locate the .zip file in the directory `data_collection\zip`. The .zip file is named as `{scanId}.zip`
5. Set up a second Event Management Agent that is connected to Event Portal.
6. Use a method approved by your organization's security policies to copy the .zip file to the second Event Management
  Agent.
7. Start the data import process by sending a POST request to the
  endpoint `http://localhost:8180/api/v2/ema/resources/import`. Add the .zip file to the body of the request
  using `file` as the key.
8. After sending the POST request, the Event Management Agent will start the import process.

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
