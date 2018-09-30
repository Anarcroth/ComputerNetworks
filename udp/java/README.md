# COS 440 - Computer Networks
*Fall 2018*

## Assignment 1

*Martin Nestorov - 100138312*

#### Summary

For Assignment 1, I have successfully implemented every feature from the requirements. This includes both *client* commands, *server side* error handling, full implementation of the specified protocol, *client side* error handling, input validation, etc.

The client-server programs have been written in `java` and build with `maven`. In addition to this, the `log4j` logging library is used to output to the console, instead of the `System.out` build-in java library. Because the code is build with `maven`, both the *client* and the *server* can be run through their respective executable `jar`s. In two separate terminal emulators run:

``` bash
java -jar server-0.1.0-SNAPSHOT.jar
```

``` bash
java -jar client-0.1.0-SNAPSHOT.jar
```

The *server* is listening on port `9876`.

#### Description

The program implements a `UDP` `client-server` connection, with a simple 2 commands protocol. The commands that are understood by the client and the server are respectively - `ADD` and `GET`.

`GET` - This commands allows the client to connect to the server and receive a random book name from a *pre-defined* list of books, that are found in the server side file system. Upon receiving this command, the server sends back a status code - `OK` and a new line with a randomly selected book.

`ADD` - This commands allows for the client to communicate with the server for an extended period of time, where the client, if authorized, can add more books to the already existing list. When the client wants to add a book, he/she must provide a username (delimited with a space from the `ADD` command), afterwards, a list of books (separated on new lines) to be added. The server checks if the passed username is valid, from a list of *pre-defined* users, located on the server side file system - `usersList.txt`. If it is, then the server waits for the list of books and appends them to the `booksList.txt`. Afterwards, both the server and the client can continue exchanging data and commands.

If the server does not authenticate the client, it sends an error status code, causing the client to disconnect and stop.

**General Structure of the code** - Both the client and server side code tries to be as efficient as possible. Although there isn't any concrete design pattern used, since the scope of the project is too small for such things, both the client and the server have a general structure. That is - they have some basic methods (which can be seen as very simple interfaces) that can be re-used - such as the `send()`, `receive()`, `listen()`, `quit()`, `parse()`. But there are also some helper methods that try to do some extra work - for instance, the server has some extra code that loads, reads, and writes from files. Because these helped methods are so small, and the scope of the project is limited, the whole `BookServer` class implements them, instead of being separated into some `interface` + `implementation` + `helper classes`.

**Server** - The server is implemented as a simple class that waits for an incoming response, then based on that response, the server returns some status code. As the server is initialized, it loads up the users and books it has available into `ArrayList`s. Since the data with which the server is working isn't complex, nor does it need any performance boosts, nor does it represent complex interactions with any clients, a simple dynamical vector does to job pretty well. Another benefit if the `ArrayList` in `java` is that it is a fast data structure. Even when a list of books is ~ 1000 lines, the server is quick to respond. For now performance is not an issue.

The server uses the standard `DatagramSocket` and `DatagramPacket` classes, which are used for `UDP` connections. The `DatagramSocket` opens a socket at port number `9876` and through that it can `receive()` and `send()` data to anyone who is willing to accept it. The `DatagramPacket` class is used to get any incoming data. The received data is in `byte[]`, which then the server has to transform into something more readable. After that, with the same packet class, we are able to send a response (using the `receivedPacket.getAddress()` and `receivedPacket.getPort()` methods telling us where the client is).

**Client** - The client is implemented a s simple class that waits for some user input, then sends that input to the server and waits for a response. The client does some very basic data validation upon input, but in general, it lets the server deal with whatever it throws at it. Things like `help`, `?`, `quit`, etc. are parsed by the client side, but that is because they are directly connected to the client interaction. If the server doesn't recognize a command, it tells the client that with a return code - `ERR Bad Request`.

---

[screenshots](./screenshots)
