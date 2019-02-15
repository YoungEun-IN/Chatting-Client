# Communicator server

Client part of an app written for "Object programming" classes.
Communicator is constructed in a way to enable converations of multiple people in specific "rooms" (like channels on irc).

Person can either join to an existing room or create a new one.
There is no limit on number of people in one room.

Communication is implemented using sockets.

## Getting Started

```
$ git clone https://github.com/Morzan3/CommunicatorClient
```

## Running the app

We run the app as normal java application

```
$ java /src/pl/slusarczyk/ignacy/CommunicatorClient/CommunicatorClient.java

```

By default client is connecting to the server listening on localhost on port 5000 (for demonstrating purposes).

After connection is established user can join to existing room (identified by its name) or create a new one.

## Authors

* **Ignacy Ślusarczyk** - [Morzan3](https://github.com/Morzan3)
