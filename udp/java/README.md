# UDP Client/Server

This is a simple *Client/Server* example, to show how to connect two processes, using `UDP`, on a local machine. Both the client and the server run on `localhost`.

You can run this example by following these steps:

``` bash
# 1. Navigate to the udp directory
cd ComputerNetworks/udp/java

# 2. Compile the java code
javac UDPServer.java
javac UDPClient.java

# 3. Run the code in two separate terminal emulators

# term 1
java UDPServer

# term 2
java UDPClient
```

The expected result would be similar to this one:

``` bash
# Start Client
java UDPClient
# Input from Client
hello
FROM SERVER:HELLO

# Start Server
java UDPServer
# Response from Server
RECEIVED: hello
```
