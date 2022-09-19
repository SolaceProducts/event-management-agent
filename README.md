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

At this stage (September 2022), the Event Management Agent is still in an active development phase.

### Available today:

* Users can discover Solace PubSub+ and Apache Kafka brokers event flow data
    - Users can discover Solace PubSub+ queues and subscriptions
    - Users can discover Apache Kafka topics and consumer groups
* Users get discovered data in the form of JSON files separated by entity types
* The Event Management Agent architecture is currently in the form of Java packages

On the roadmap:

* Support for Confluent and MSK flavours of Apache Kafka
* The Event Management Agent has an open source plugin framework
* Support additional Solace PubSub+ and Apache Kafka event broker authentication types in the form of plugins such as
  basic authentication, certificates, Kerberos, etc.
* Collection of topics from events flowing though Solace PubSub+ brokers
* Import discovered data into the Solace PubSub+ Event Portal
* Export discovered data as AsyncAPI specifications
* Addition of the infrastructure needed for the Event Management Agent to be a true open source project
* Discovery of Apache Kafka connectors
* Discovery of schemas from schema registries
* Introduction of a UI for the Event Management Agent
* Additional support to more broker types
* Event Management Agent Docker images
* Event Management Agent executables

## Running the Event Management Agent

### Prerequisites:

* Java 11 (AdoptOpenJDK 11.0.14+ https://adoptium.net/temurin/releases)
* Maven
* Docker
* Event Management Agent Region (for cloud mode)

### Minimum hardware requirements

The Event Management Agent was tested to run with

* 1 CPU
* 1 GB RAM

### Spring-boot properties

These properties are required to run this spring boot application in cloud mode. Update the following properties in the
application.yml file

```
eventPortal.gateway.messaging.connections.url = <secure smf host and port> example  tcps://<host>:<port>
eventPortal.gateway.messaging.connections.msgVpn = <your vpn>
eventPortal.gateway.messaging.connections.users.name= <your name>
eventPortal.gateway.messaging.connections.users.username = <your username>
eventPortal.gateway.messaging.connections.users.password = <your password>
eventPortal.gateway.messaging.connections.users.clientName= <your client name>
```

### Cloning and Building

#### Steps to build and run the service

1. Clone the runtime-agent repository

```
git clone git@github.com:SolaceLabs/runtime-agent.git
```

2. Install maven dependencies

```
cd runtime-agent/service
mvn clean install
```

3. Start the Event Management Agent

```
java -jar application/target/runtime-agent-0.0.1-SNAPSHOT.jar 
```

Alternatively, to build and run the service in IDE

1. Clone the runtime-agent repository

```
git clone git@github.com:SolaceLabs/runtime-agent.git
```

2. The Event Management Agent uses H2 database by default. The H2 console is available at `http://localhost:8180/h2`.
   The database is available at `jdbc:h2:file:./data/cache`.

3. The connection details for the H2 database are specified using the following properties in the application.yml file

```
spring.datasource.url
spring.datasource.username
spring.datasource.password
spring.datasource.driver-class-name
spring.h2.console.path
```

4. Alternatively, to use MySql database

    1. save the code below to a yml file, then run `docker-compose up` against the file. **
       Note**: For Macbook users with M1 chip, add the property `platform: linux/x86_64` to the file

   ```
   version: '3.1'

   services:
    db:
      image: mysql
      container_name: mysql8
      command: --default-authentication-plugin=mysql_native_password
      restart: always
      ports:
        - "3308:3306"
      environment:
        MYSQL_ROOT_PASSWORD: secret
      volumes:
        - ./my-datavolume:/var/lib/mysql 
     ```

    2. Create the `runtime_agent` database

    ```
    create database if not exists runtime_agent;
    ```

    3. Create an active profile named `mysql-dev` in Spring Boot Run Configurations

   ![Alt text](docs/images/run-configuration.png "run configuration")

    4. Create new yml file in resources with the name `application-mysql-dev.yml`&nbsp;

    5. Add the code below to the file

    ```
    spring:
      datasource:
        url: jdbc:mysql://localhost:3308/runtime_agent
        username: root
        password: secret
        driver-class-name: com.mysql.jdbc.Driver
      jpa:
        database-platform: org.hibernate.dialect.MySQL8Dialect
        hibernate:
          ddl-auto: create
    ```

5. Start the application by running this class in Intellij

```
service/application/src/main/java/com/solace/maas/ep/runtime/agent/RuntimeApplication.java
```

## Broker Plugins

The Event Management Agent comes with the following event or message broker plugins included:

* Apache Kafka
* Solace PubSub+
* Confluent
* MSK

## Deployment

There are essentially 2 main modes of deployment:

* SC Connected: The Event Management Agent connects to the event management region and can be controlled remotely via
  Event Portal

* Stand-alone: The Event Management Agent is controlled via the REST API and results must be uploaded manually.

## Running a scan

### REST interface

The Event Management Agent includes a REST API that allows the user to initiate a scan. Each plugin requires its own
custom set of authentication and identification attributes that must be supplied by the user.

See [REST Documentation](docs/rest.md) for additional information

## Motivations

See [motivations](./docs/motivations.md)

## Testing

There are several interesting scenarios to test the Event Management Agent. These scenarios can be divided into two main
categories according to the deployment mode.

* Testing the Event Management Agent as standalone service (stand-alone deployment).
* Testing the end-to-end flow in SC connected mode (From the frontend to the Event Portal, then to the Event Management
  Agent)

### Testing the Event Management Agent in standalone mode

The most important test in standalone mode is to ensure that the Event Management Agent runs and collects data properly.
To that end, the test includes the steps below:

1. Update the `plugins` section of the `application.yml` with the details of the messaging service you want to scan.
2. Start the Event Management Agent either from IntelliJ or by running the JAR file.
3. Examine the on-console logs for a log from `RuntimeAgentConfig` class indicating that the messaging service(s) has
   been created.

```
c.s.m.e.r.a.config.RuntimeAgentConfig : Created Kafka messaging service: kafkaDefaultService confluent kafka cluster
c.s.m.e.r.a.config.RuntimeAgentConfig : Created Solace messaging service: solaceDefaultService staging service
```

4. View the Swagger documentation to learn about the available REST endpoints for the Event Management Agent. To access
   the Swagger documentation, use the link `http://localhost:8180/runtime-agent/swagger-ui/index.html` (Note:
   The Event Management Agent is under continuous development. Therefore, please check the Swagger documentation to make
   sure that you are using the recent endpoint schema).
5. Initiate a scan against a messaging service by sending a POST request to the endpoint that triggers the data
   collection `/api/v2/runtime/messagingServices/{messagingServiceId}/scan`. The request can be sent using either
   Postman or a curl command.
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

### Testing the Event Management Agent in SC mode

The most important test is SC mode is to verify that the entire end-to-end flow works properly. That is, from utilizing
the front end to initiate the scan all the way to receiving the scan data in the Event Portal database.

1. Start the Frontend by running the `maas-ui` locally, then visiting `http://localhost:9000/`
2. Sign in, then enable the option `New Event Portal 2.0 : On`
3. Navigate to the `Runtime Event Manager`, then either choose an existing `Modeled Event Mesh` or create a new one.
4. Open the `Modeled Event Mesh` view, then navigate to the `Runtime` tab.
5. Add a messaging service either by selecting a new or existing one.
6. Fill in the messaging service details, i.e., `Name`, `SEMP Username`, `SEMP Password`, `SEMP URL`, and `Message VPN`.
   (You can retrieve these details from your messaging service in Cluster Manager).
7. After associating a messaging service to the modeled event mesh, navigate to `Runtime Event Manager`
   then `Runtime Agents`.
8. Set up the Event Management Agent's connection and add the messaging service, then save and create the connection
   file.
9. Update the `application.yml` file with the details from the connection file.
10. Start the Event Management Agent either from IntelliJ or by running the JAR file.
11. Examine the on-console logs for a log from `RuntimeAgentConfig` class indicating that the messaging service(s) has
    been created.

```
c.s.m.e.r.a.config.RuntimeAgentConfig : Created Kafka messaging service: kafkaDefaultService confluent kafka cluster
c.s.m.e.r.a.config.RuntimeAgentConfig : Created Solace messaging service: solaceDefaultService staging service
```

12. Refresh the frontend and make sure that the badge next to the Event Management Agent shows `Connected`.
13. Navigate to `Runtime Event Manager`, then select the modeled event mesh and navigate to the `Runtime` tab.
14. Select the messaging service and click `Collect Data`.
15. Examine the Event Management Agent console logs to make sure that the individual scan types are complete. e.g.,
    `Route subscriptionConfiguration completed for scanId 3a41a0f5-cd85-455c-a863-9636f69dc7b2`
16. Examine the collected data by browsing to the directory `data_collection`. This directory is organized as
    {schedule_id}/{scan_id}/{scan_type.json}
17. Verify that the collected data contains a separate JSON file for each scan type.
18. Verify the contents of each JSON file.
19. Check the logs by browsing to `data_collection/logs/{scan_id}.log` and `general-logs.log` to make sure no exceptions
    or errors occurred.
20. Finally, check the Event Portal tables to confirm they contain the scanned data.

## Contributions

Contributions are encouraged! If you have ideas to improve an existing plugin, create a new plugin, or improve/extend
the agent framework then please contribute!

## Contributors

@gregmeldrum @slunelsolace @AHabes @MichaelDavisSolace
