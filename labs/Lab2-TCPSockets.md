# Lab 02 – TCP Socket Programming

> Assist. Prof. Vladimir Georgiev

## Introduction

This programming exercise involves writing a *client* and a *server* programs that are going to communicate with each other over the network. The client will send simple expressions in the format `11 + 5 or 44 / 2` to the server, which will parse them, evaluate them and result a summary of the result in the format `11 + 5 = 16`. The two programs should make use of `TCP` sockets for the network communication. By using `TCP`, the server will be able to maintain different connections per each client that is currently connected to it. Therefore, in addition to evaluating the mathematical expressions, the server will also keep count of all errors (wrong requests) sent by each client, and will include that count in its log. The server can listen on any non-reserved port and the client can connect to the server on the loop back address `127.0.0.1`.

## 1. Creating the Server

Create a C# console app project named `TCPServer`.

Include the `System`, `System.Net`, `System.Net.Sockets`, `System.Text`, `System.Text.RegularExpressions` and `System.Threading` *namespaces*.

Add a private member in your main class for the port number which will be used by the server:

``` C#
private const int port = 10572;
```

In your main function, add the following code that will create the main `TCP` socket, bind it to the port and start listening for incoming connection requests:

``` C#
TcpListener server = new TcpListener(IPAddress.Any, port);
server.Start();
Console.WriteLine("Started listening ...");
```

Whenever a new connection is initiated by a client, the server should *accept* it, which should create a separate socket to be used for communication with that individual client. The following code listens indefinitely for such client requests, gets the client socket out of the accepted request and passes it to a new thread to take care of the subsequent communication in parallel:

``` C#
while (true)
{
    // wait for a client connection
    TcpClient client = server.AcceptTcpClient();

    // handle the communication with the client in a separate thread
    Thread t = new Thread(new ParameterizedThreadStart(HandleClient));
    t.Start(client);
}
```

To complete the program, you have to add the definition of the `HandleClient` function that will be responsible for processing all communication between the server and the individual client (identified by the client socket passed to that function). What your function will first need to do is get the client socket from the parameter:

``` C#
TcpClient client = (TcpClient)param;
Socket sock = client.Client;
string clientId = sock.RemoteEndPoint.ToString();
```

To repeatedly read a request from the client and send a response, we can use a while loop like in the code below. The first part reads a stream of bytes from the client and converts it to a string object. After processing it and determining the response, we could use the code at the end of the loop to send a reply back to the client.

``` C#
while (true)
{
    byte[] bytes = new byte[1024];
    int bytesRec = sock.Receive(bytes);
    string request = Encoding.ASCII.GetString(bytes, 0, bytesRec);
    string response = "";
    ...
    Console.WriteLine("[{0}] {1} => {2} ({3} errors total)",
    clientId, request, response, errorCount);

    byte[] replyBuffer = Encoding.ASCII.GetBytes(response);
    sock.Send(replyBuffer);
}
```

The complete code of the program, together with the specific processing of the client requests, is shown below:

``` C#
class Program
{
    private const int port = 10572;

    static void Main(string[] args)
    {
        TcpListener server = new TcpListener(IPAddress.Any, port);
        server.Start();

        Console.WriteLine("Started listening ...");

        while (true)
        {
            // wait for a client connection
            TcpClient client = server.AcceptTcpClient();

            // handle the communication with the client in a separate thread
            Thread t = new Thread(new ParameterizedThreadStart(HandleClient));
            t.Start(client);
        }
    }

    private static void HandleClient(object param)
    {
        TcpClient client = (TcpClient)param;
        Socket sock = client.Client;
        string clientId = sock.RemoteEndPoint.ToString();

        Console.WriteLine("Accepted client connection from {0}", clientId);

        int errorCount = 0;

        // start a message exchange with the client
        // client will send two digits for multiplication and
        // the server should receive a reply
        while (true)
        {
            byte[] bytes = new byte[1024];
            int bytesRec = sock.Receive(bytes);
            string request = Encoding.ASCII.GetString(bytes, 0, bytesRec);
            string response = "";

            Match match = Regex.Match(request,
                          @"\s*([\d\.]+)\s*([\+\-\*\/])\s*([\d\.]+)", RegexOptions.Compiled);
            if (match.Success)
            {
                string val1 = match.Groups[1].Value;
                char op = match.Groups[2].Value[0];
                string val2 = match.Groups[3].Value;

                float a = float.Parse(val1);
                float b = float.Parse(val2);
                float result = 0;

                switch (op)
                {
                    case '+':
                        result = a + b;
                        break;
                    case '-':
                        result = a - b;
                        break;
                    case '*':
                        result = a * b;
                        break;
                    case '/':
                        result = a / b;
                        break;
                }

                response = request + " = " + result;
            }
            else
            {
                response = "ERROR";
                errorCount++;
            }

            Console.WriteLine("[{0}] {1} => {2} ({3} errors total)",
			clientId, request, response, errorCount);

            byte[] replyBuffer = Encoding.ASCII.GetBytes(response);
            sock.Send(replyBuffer);
        }
    }
}
```

Compile and run the server application. You should see the `Started listening…` message.

## 2. Creating the Client

Create a new C# console app project named `TCPClient`.

Add two constant members that store the server’s IP address and port. Those will be used to uniquely identify and connect to the server’s socket:

``` C#
private const int serverPort = 10572;
private const string serverAddress = "127.0.0.1";
```

**NOTE**: Make sure that the port matches the port used when creating the server!

Create the TCP connection, connect it to the server and get the connection socket:

``` C#
TcpClient client = new TcpClient();
client.Connect(serverAddress, serverPort);
Socket sock = client.Client;
```

What the client should do next is repeating the actions of reading user input, sending that input to the server, receiving the server’s response and showing it to the screen:

``` C#
while (true)
{
    Console.Write("Enter a simple expression to be evaluated: ");
    string input = Console.ReadLine();
    byte[] requestBuff = Encoding.ASCII.GetBytes(input);
    sock.Send(requestBuff);

    byte[] receiveBuff = new byte[1024];
    int bytesRec = sock.Receive(receiveBuff);
    string response = Encoding.ASCII.GetString(receiveBuff, 0, bytesRec);
    Console.WriteLine("Server response is: " + response);
}
```

To test the two applications, *first start the server*. Then start *two* instances of the client and use each one to evaluate a simple mathematical expression like *adding/subtracting/multiplying/dividing* two numbers. The server console should indicate each request/response exchanged with each different client, and the number of errors each client has accumulated throughout the connection session. A sample of the server is shown below:

``` bash
Started listening ...
Accepted client connection from 127.0.0.1:35058
Accepted client connection from 127.0.0.1:35060
[127.0.0.1:35058] 1 + 2 => 1 + 2 = 3 (0 errors total)
[127.0.0.1:35060] 4 + 5 => 4 + 5 = 9 (0 errors total)
[127.0.0.1:35060] str1 => ERROR (1 errors total)
[127.0.0.1:35060] str2 => ERROR (2 errors total)
[127.0.0.1:35060] str3 => ERROR (3 errors total)
[127.0.0.1:35060] str4 => ERROR (4 errors total)
[127.0.0.1:35060] str5 => ERROR (5 errors total)
[127.0.0.1:35058] str1 => ERROR (1 errors total)
[127.0.0.1:35058] str2 => ERROR (2 errors total)
[127.0.0.1:35058] str3 => ERROR (3 errors total)
[127.0.0.1:35058] str4 => ERROR (4 errors total)
[127.0.0.1:35058] 6 + 6 => 6 + 6 = 12 (4 errors total)
```

## 3. Additional Features

**Task 1** - Update the server to support the modulo operator (`%`).

**Task 2** - Update the server to terminate a client’s connection after **5** errors, by sending the `EXIT` response and closing the client socket. When the client receives the `EXIT` reply, it should also close the connection with the server and terminate the application.
