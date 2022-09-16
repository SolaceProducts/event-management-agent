# Event Management Agent


The Event Management Agent is a tool used by architects and developers working with Event-Driven Architectures  (EDAs) to discover event streams flowing through an event broker as well as the related broker configuration information.
The Event Management Agent can be used in two different ways:
* As a standalone tool that discovers runtime event data from event or message brokers in the runtime to retrieve EDA related data. This data can be exported as an AsyncAPI specification for the broker service to document the event flow information and be used with other tools supporting AsyncAPI specifications.
* As the Event Management Agent component of the Solace’s PubSub+ Event Portal product to:
  - discover runtime event data from event brokers
  - populate the Event Portal Designer and Catalog services with EDA data from the runtime enabling the management and reuse of EDA assets
  - audit the runtime data and flag discrepancies between the runtime and the design time intent for event data governance purposes, and ensure that the runtime and design time configurations stay in-sync


Our plan is to open source the Event Management Agent to enable architects and developers to contribute to it as well as to build new plugins so that:
* runtime data can be discovered from additional broker types
* existing plugins can be extended to discover additional data
* EDA data can be discovered from other systems, e.g. schemas from schema registries


At this stage (September 2022), the Event Management Agent is still in an active development
phase.

### Available today:
* Users can discover Solace PubSub+ and Apache Kafka brokers event flow data
  - Users can discover Solace PubSub+ queues and subscriptions
  - Users can discover Apache Kafka topics and consumer groups
* Users get discovered data in the form of JSON files separated by entity types
* The Event Management Agent architecture is currently in the form of Java packages

On the roadmap:
* Support for Confluent and MSK flavours of Apache Kafka
* The Event Management Agent has an open source plugin framework
* Support additional Solace PubSub+ and Apache Kafka event broker authentication types in the
form of plugins such as basic authentication, certificates, Kerberos, etc.
* Collection of topics from events flowing though Solace PubSub+ brokers
* Import discovered data into the Solace PubSub+ Event Portal
* Export discovered data as AsyncAPI specifications
* Addition of the infrastructure needed for the Event Management Agent to be a true open source
project
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
* Runtime Agent Region (for cloud mode)

### Minimum hardware requirements

The Runtime Agent was tested to run with

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

2. Save the code below to a yml file, then run `docker-compose up` against the file **Note**: For Macbook users with M1
   chip, add the property `platform: linux/x86_64` to the file

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

3. On a different terminal window, run the command

```
docker exec -it mysql8 /usr/bin/mysql -psecret
```

4. Create the `runtime_agent` database

```
create database if not exists runtime_agent;
```

5. Create an active profile named `mysql-dev` in Spring Boot Run Configurations

![Alt text](docs/images/run-configuration.png "run configuration")

6. Create new yml file in resources with the name `application-mysql-dev.yml`&nbsp;


7. Add the code below to the file

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

8. Start the application by running this class in Intellij

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

* SC Connected: The Event Management Agent connects to the runtime region and can be controlled remotely via Event Portal

* Stand-alone: The Event Management Agent is controlled via the REST API and results must be uploaded manually.

## Running a scan

### REST interface

The Event Management Agent includes a REST API that allows the user to initiate a scan. Each plugin requires its own custom set
of authentication and identification attributes that must be supplied by the user.

See [REST Documentation](docs/rest.md) for additional information

## Motivations

See [motivations](./docs/motivations.md)

## Contributions

Contributions are encouraged! If you have ideas to improve an existing plugin, create a new plugin, or improve/extend
the agent framework then please contribute!

## Contributors

@gregmeldrum @slunelsolace @AHabes @MichaelDavisSolace
