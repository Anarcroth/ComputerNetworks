# Assignment 1

### Summary

The client-server programs have been written in `java` and build with `maven`. In addition to this, the `log4j` logging library is used to output to the console, instead of the `System.out` build-in java library. Because the code is build with `maven`, both the *client* and the *server* can be run through their respective executable `jar`s. In two separate terminal emulators run:

``` bash
java -jar server-0.1.0-SNAPSHOT.jar
```

``` bash
java -jar client-0.1.0-SNAPSHOT.jar
```

The *server* is listening on port `9876`.
