# COS 440 - Computer Networks
*Fall 2018*

## Assignment 2

*Martin Nestorov - 100138312*

#### Summary

For Assignment 2, I have successfully implemented very feature from the requirements. This includes both *client* and *server* commands, error handling on both sides, full implementation of the needed protocol, input validation on both sides, etc.

The client-server programs have been written in `java` and build with `maven`. In addition to this, the `log4j` logging library is used to output to the console, instead of the `System.out` build-in java library. Because the code is build with `maven`, both the *client* and the *server* can be run through their respective executable `jar`s. In two separate terminal emulators run:

``` bash
java -jar server-0.1.0-SNAPSHOT.jar
```

``` bash
java -jar client-0.1.0-SNAPSHOT.jar
```

The *server* is listening on port `9876`.

#### Assumptions

In order for the project to make sense, I have made some assumptions towards how the client might use this *ATMServer*. These are just unspoken rules on which I am counting on that all the users know and are following.

- All users have unique **PIN** numbers.
- All users know their **PIN**.
- It's not expected that the server can delete or add new users. The original 5 are the only ones.
- This is not a secure connection and more than one user can log in at the same time.
- There are no real upper or lower limit to the amount of money one user can have.
- **Every time** a client connects to the sever **for the first time**, the client issues a `START` command. This is done, because implementing a safety feature for this project would be too cumbersome and time consuming.
- The main way for the Server to recognize which user is issuing a command is through the incoming **port numbers**.

#### Description

**Server Side** - The `ATMServer` implements a `TCP` connection with each and every client that knows the *Servers* port number. The Server knows the commands, as specified in the assignment, but it recognizes when an incorrect command comes in.

The Server also has a list of all of the know users, found under the `clients.txt` file (found either in the root directory in the project or in the `resources` folder of the source code).

If the Server is shutdown, then it saves the current state of the clients to the `clients.txt` file!

The Server also has a state about each `Account` that is maintained in the memory of the application. As per the wanted requirements, each `Account` has the following:

- 4 digit **PIN** number.
- 32 bit `UUID` account number (It is both easy and recommended to generate random numbers as `UUID`s).
- Current account balance.
- Name of account holder.
- Port number of the current client.
- State of the current client if he/she is authorized.

The behavior of the *Server* to each command is as follows:

`START` - This command just tells the Server side that there is a connection from an incoming client on a specific `connectionSocket.getPort()` number and sends an *"OK"* to the client.

`CLOSE` - This just tells the Server side that a client from a specific port number is about to close the connection.

`AUTH` - This starts a series of exchanges between the server and the client in order to setup a authentication. The server receives a **PIN** number and then checks that there is such a client with that number. If not, a *"NOTOK"* is issued. If there is, then an *"OK"* is sent and also the client is further authorized to continue using the *ATM service*. No more authorizations are needed if a client has passed this step.

`BALANCE` - This just sends the clients data back to them. The client is recognized through the incoming port number, received from the `Socket` connection that is established. The client receives his *Name*, *ID number*, and *Current Balance*.

`DEBIT` - This command also engages into a series of exchanges between the server and the client. First the server sends an *"OK"* to the client and then awaits for an amount that would be withdrawn from the users account. Then, after a simple check if the amount is no larger than the current balance of the user, the money is taken away and an *"OK"* is issued. The server continues to remember this change and if a user re-logs in, or want's another `BALANCE` check, he will see the changes set in place.

`CREDIT` - This command acts similarly to the `DEBIT` command. In fact, it does the exact same thing, but instead of taking away money from the users account, it adds money and doesn't check how much it adds, since there are no upper limits (check the presumptions section).

In general, the hard part of the *Server* implementation is the fact that there must be a lot of exception handling done for the clients.

**Client Side** - The `Client` implements a `TCP` connection with the server. The client has a view of a simple text menu, that states all of the possible commands, that the client can issue.

The behavior of the *Client* to each command is as follows:

`START` - This command just opens the connection between the server and the client, initializing the `I/O Streams` for communication.

`CLOSE` - This command tells the server that the client will close the connection, then it does exactly that and exits the program.

`AUTH` - This command prompts the user to input their **PIN** number and then sends that information to the server. Then it waits for a response and if it is verified, that user is also verified to continue issuing more commands. If the user skips this step, then each other command will prompt the user to authenticate first and then continue working normally with the other commands.

`BALANCE` - This command just issues the server to get the current users information. The user receives his data. Simple as that.

`DEBIT` - This command issues the server to withdraw some money from the current clients bank account. After an authorization step and input validation, the *Client* can receive either an *"OK"* or *"NOTOK"* response. At the end, the client sees his/her balance.

`CREDIT` - This command issues the server to add some money to the current clients bank account. It works in the same way the `DEBIT` command works.

In general, the hard part of the *Client* implementation is the fact that the user must be validated with his/her input.

#### Difficulties

The most difficult parts of the whole project, is that the sending over `TCP` is not as easy as it is with `UDP`. Sometimes, over the course of development, either the *Client* or the *Server* side would hang or would become unresponsive.

I had some difficulties with the fact that the client/server was sending data that that might have been too large for the other side to handle. This was annoying to handle, but the key was in how things were initialized.

#### Bonuses

There are some small additions to the original requirements, such as the list of clients that is updated and saved from the server side.

Also a `PING` command. This command is the simplest of all. The *Client* sends a **PING** and waits for a **PONG**. This is to make sure that the client can "talk" to the server.

And Exception handling. This project has a lot of exception handling! :)
