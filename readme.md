## Requirements
* [Docker](https://www.docker.com/get-started)
* [JAVA](https://www.oracle.com/java/technologies/javase-jdk13-downloads.html)

## What is  it
This is simplified implementation of **Black Jack** card game made by using spring-boot and hibernate.
Tried used Domain Driven Design Architecture - by splitting code into three packages:
* *domain* - where is **application logic** - in this package only core JAVA is allowed with some exceptions (*lombok, apache commons*)
* *infrastructure* - in this package is everything what is required by domain to operation (saving and getting data), so
here is all part responsible for integration with database or external services (**all output of application and all application dependencies**)
* *interfaces* - this package is responsible for providing commands to domain. Here should be placed all integrations with
everything which can give and order to application: REST Controllers, JMS queues, KAFKA topics (**all input to application**)

## How to start

* `docker-compose up` - setup container with database for local development
* `gradlew flywayMigrate -i` - run db migrations
* run application: `com.trzewik.spring.BlackJackApp`

## Build

* check that `docker` is running on your machine (it is required by tests - `postgres testcontainers`)
* `gradlew clean build` - for building application and running all tests
* `gradlew pitest` - runs mutation tests **WARNING** currently [pitest](https://pitest.org) mutate all tests
(unit, integration and functional) and this operation takes a lot of time (around 10 minutes)


### TODO
* add logs
* add swagger
* add [archunit](https://www.archunit.org) for checking DDD rules
* add [feign client](https://github.com/OpenFeign/feign) for example: checking player account balance
* ...

