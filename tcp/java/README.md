# TCP Client/Server

This is a simple *Client/Server* example, to show how to connect two processes, using `TCP`, on a local machine. Both the client and the server run on `localhost`.

You can run this example by following these steps:

``` bash
# 1. Navigate to the tcp directory
cd ComputerNetworks/tcp/java

# 2. Compile the java code
javac TCPServer.java
javac TCPClient.java

# 3. Run the code in two separate terminal emulators

# term 1
java TCPServer

# term 2
java TCPClient
```

The expected result would be similar to this one:

``` bash
# Start Client
java TCPClient
# Input from Client
hello
FROM SERVER:HELLO

# Start Server
java TCPServer
# Response from Server
RECEIVED: hello
```
