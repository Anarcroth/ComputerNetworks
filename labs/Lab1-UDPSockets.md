# Lab 01 - UDP Socket Programming

> Assist. Prof. Vladimir Georgiev

# Introduction

This programming exercise involves writing a client-server pair of networked applications, which are used for exchanging messages between each other. The functionality provided by the server is simply converting the letters in the *client’s* message to *upper-case*, returning the converted string back to the client. The client should be using the loopback address (`127.0.0.1`) to connect to the server, running on the same host.

## 1. Creating the Server
Create a `C#` console app project named `UDPServer`.

Include the `System`, `System.Net`, `System.Net.Sockets` and `System.Text` *namespaces*.

Add a private member in your main class for the port number which will be used by the server:

``` C#
private const int port = 8272;
```

In your main function, add the following code that will create the UDP socket, to be used by the server for accepting and sending data:

``` C#
UdpClient listener = new UdpClient(port);
Console.WriteLine("Started listening...");
```

To continuously receive a message from the socket, process it and send a reply back to the client, you will need to add a series of statements inside the body of a while loop, repeating forever. The first statement would need to declare an `IPEndPoint` object, which will be used to represent the client that sent the specific request:

``` C#
IPEndPoint remoteEP = new IPEndPoint(IPAddress.Any, port);
```

Then add the following code which receives a message from the listening socket, sent from any remote endpoint:

``` C#
byte[] receivedByteArr = listener.Receive(ref remoteEP);
string message = Encoding.ASCII.GetString(receivedByteArr);
Console.WriteLine("Received: {0} from {1}", message, remoteEP.ToString());
```

The remaining code converts all letters to uppercase and sends the resulting string as a reply to the client that originally sent the message:

``` C#
string reply = message.ToUpper();
byte[] replyByteArr = Encoding.ASCII.GetBytes(reply);
listener.Send(replyByteArr, replyByteArr.Length, remoteEP);
Console.WriteLine("Sent a reply: {0} to {1}", reply, remoteEP.ToString());
```

Your class’s code should look something like this:

``` C#
private const int port = 8272;

static void Main(string[] args)
{
    UdpClient listener = new UdpClient(port);
    Console.WriteLine("Started listening...");

    try
    {
        while(true)
        {
            IPEndPoint remoteEP = new IPEndPoint(IPAddress.Any, port);

            byte[] receivedByteArr = listener.Receive(ref remoteEP);
            string message = Encoding.ASCII.GetString(receivedByteArr);
            Console.WriteLine("Received: {0} from {1}", message, remoteEP.ToString());

            string reply = message.ToUpper();
            byte[] replyByteArr = Encoding.ASCII.GetBytes(reply);
            listener.Send(replyByteArr, replyByteArr.Length, remoteEP);
            Console.WriteLine("Sent a reply: {0} to {1}", reply, remoteEP.ToString());
        }
    }
    catch (Exception e)
    {
        Console.WriteLine(e.ToString());
    }

    listener.Close();
}
```

Compile and run the server application. You should see the `"Started listening…"` message.

## 2. Creating the Client

Create a another `C#` console app project named `UDPClient`.

Add two constant members that store the *server’s* `IP` address and `port`. Those will be used to uniquely identify and connect to the server’s socket:

``` C#
private const int serverPort = 8272;
private const string serverAddress = "127.0.0.1";
```

**NOTE**: Make sure that the port matches the port used when creating the server!

Create the UDP socket and connect it to the server’s socket:

``` C#
UdpClient client = new UdpClient();
IPEndPoint ep = new IPEndPoint(IPAddress.Parse(serverAddress), serverPort);
client.Connect(ep);
```

Add a while loop to repeat infinitely, which contains the groups of statements mentioned throughout the end of this section. Read a line of text from the user and if empty, stop executing the loop:

``` C#
Console.WriteLine("Enter text to send, blank line to quit");
string text = Console.ReadLine();

if (text.Length == 0)
{
    break;
}
```

The code that sends the text to the server should look like this:

``` C#
byte[] sendBuffer = Encoding.ASCII.GetBytes(text);
client.Send(sendBuffer, sendBuffer.Length);
```

Right after the message is sent, the client should wait for and receive the reply from the server:

``` C#
byte[] receivedData = client.Receive(ref ep);
string reply = Encoding.ASCII.GetString(receivedData);
Console.WriteLine("Server reply: {0}", reply);
```

You can optionally wrap your socket sending and receiving functions in a `try-catch` block to debug any further errors.

The complete main function’s code should look similar to this:

``` C#
UdpClient client = new UdpClient();
IPEndPoint ep = new IPEndPoint(IPAddress.Parse(serverAddress), serverPort);
client.Connect(ep);

while(true)
{
    Console.WriteLine("Enter text to send, blank line to quit");
    string text = Console.ReadLine();

    if (text.Length == 0)
    {
        break;
    }

    try
    {
        byte[] sendBuffer = Encoding.ASCII.GetBytes(text);
        client.Send(sendBuffer, sendBuffer.Length);

        byte[] receivedData = client.Receive(ref ep);
        string reply = Encoding.ASCII.GetString(receivedData);
        Console.WriteLine("Server reply: {0}", reply);
    }
    catch (Exception e)
    {
        Console.WriteLine(e.ToString());
    }
}
```

To test the *client* and *server* apps, you should first start the two applications. Once they are both started, typing something on the *client console* should display the upper-cased response from the server. The *server console* should also indicate that a message was received by a client and a response was sent to it.

## 3. Additional Features

**Task 1** - Update the client and server programs, to support the `exit` command sent by the client. Whenever the server receives the `exit` string, it should stop listening and terminate itself. The client program should also stop itself after it sends the `exit` command to the server.

**Task 2** - Extend the communication "protocol" between the client and the server to support the following commands:

`U` - client request for translating some string to upper-case. The string should be separated by a colon from the command.

Example:

``` bash
# client command
U:abc

# server response
U:ABC
```

`L` - client request to translate some string to lower-case. The string should be separated by a colon from the command.

Example:

``` bash
# client command
L:XyZ

# server response
L:xyz
```

`E` - client request to exit/terminate the server. Does *not* require a response.

`I` - if the client sends a command that does not match any of the above signatures, it should reply with the letter `I`, indicating that an invalid request was received.
