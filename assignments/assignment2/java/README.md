# Assignment 2

### Summary

The client-server programs have been written in `java` and build with `maven`. In addition to this, the `log4j` logging library is used to output to the console, instead of the `System.out` build-in java library. Because the code is build with `maven`, both the *client* and the *server* can be run through their respective executable `jar`s. In two separate terminal emulators run:

``` bash
java -jar server-0.1.0-SNAPSHOT.jar
```

``` bash
java -jar client-0.1.0-SNAPSHOT.jar
```

The *server* is listening on port `9876`.

#### Assumptions

In order for the project to make sense, some assumptions are made towards how the client might use the *ATMServer*.

- All users have unique **PIN** numbers.
- All users know their **PIN**.
- It's not expected that the server can delete or add new users. The original 5 are the only ones.
- This is not a secure connection and more than one user can log in at the same time.
- There are no real upper or lower limit to the amount of money one user can have.
- **Every time** a client connects to the sever **for the first time**, the client issues a `START` command. This is done, because implementing a safety feature for this project would be too cumbersome and time consuming.
- The main way for the Server to recognize which user is issuing a command is through the incoming **port numbers**.
