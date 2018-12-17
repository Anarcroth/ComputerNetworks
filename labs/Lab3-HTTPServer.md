# Lab 03 - HTTP Server

> Assist. Prof. Vladimir Georgiev

# Introduction

In this exercise you will create a *web server* that implements a simpler version of the `HTTP` protocol. The server will use a *cookie* that will be exchanged with the web browser client, in order to maintain a session state between the requests. The cookie will be used to keep count of the number of views that the client has done throughout one session. Since the cookie will have specific lifetime, it will expire and the session will end when the web browser is closed. Opening a connection from another browser will create a new session with the server, because cookies are not shared between different browsers.

# 1. Creating the Server

Create a `C#` console app project. Include the `System`, `System.IO`, `System.Net`, `System.Net.Sockets`, `System.Text`, `System.Text.RegularExpressions` and `System.Threading` *namespaces*.

Add a private member in your main class for the port number which will be used by the server:

``` C#
private const int port = 8286;
```

Similar to the previous Lab exercise, your main function should initialize a socket and start listening to the above port. Whenever a new connection is accepted, it will be passed to and handled by a separate thread function:

``` C#
TcpListener server = new TcpListener(IPAddress.Any, port);
server.Start();
Console.WriteLine("Started listening ...");

while(true)
{
    // wait for a client connection
    TcpClient client = server.AcceptTcpClient();

    // handle the communication with the client in a separate thread
    Thread t = new Thread(newParameterizedThreadStart(HandleClient));
    t.Start(client);
}
```

Before adding the body of the `HandleClient` function, you will first need to add some other helper functions. For reading the request from the client and sending a response,  this exercise uses a `NetworkStream`. In order to parse the request and retrieve the cookie, the server needs to convert the *bytes* read from the stream into a `string` object. This is done in the following function:

``` C#
private static string ReadString(NetworkStream stream)
{
    MemoryStream memoryStream = new MemoryStream();
    byte[] data = new byte[256];
    int size;
    do
    {
        size = stream.Read(data, 0, data.Length);
        if (size == 0)
        {
            Console.WriteLine("Client disconnected");
            Console.ReadLine();
            return null;
        }
        memoryStream.Write(data, 0, size);
    }
    while (stream.DataAvailable);

    return Encoding.UTF8.GetString(memoryStream.ToArray());
}
```

After receiving a request, the `HTTP` server processes it, generates a response and sends the response to the *client* (web browser). The server returns the same `HTML` content
in every response, only updating the number of views integer contained in the page. Inside the processing step, the current number of views will be taken from the value of the `num_of_views` cookie, located in a `Cookie` header and sent by the web browser. It will then be incremented by one and the new value will be included in the body of the `HTML` page returned in the response.

The response will also contain that number as the value of the same `num_of_views` cookie, passed in a `Set-Cookie` header, which will be persisted by the browser and sent on a subsequent request to the same server.

Below you can see two functions used for processing the request and retrieving a cookie's value provided its name:

``` C#
private static string GetResponse(string request)
{
    int views = 0;
    string strNumberOfViews = GetCookie(request, "num_of_views");
    if (!string.IsNullOrEmpty(strNumberOfViews))
    {
        views = Int32.Parse(strNumberOfViews);
    }

    views++;

    string response = "HTTP/1.1 200 OK\n" +
           "Content-Type: text/html\n" +
           "Set-Cookie: num_of_views=" + views + ";\n" +
           "\n" +
           "<html><head><title>Hello</title></head><body><h1>" + views +
               " views</h1></body></html>";

    return response;
}
```

``` C#
private static string GetCookie(string request, string cookieName)
{
    Match match = Regex.Match(request,
        @"Cookie:[^\n]*?" + cookieName + @"=([^;\n]+);?\n",
        RegexOptions.Compiled | RegexOptions.IgnoreCase | RegexOptions.Multiline);

    if (match.Success)
    {
        return match.Groups[1].Value;
    }

    return string.Empty;
}
```

The returned response follows the `HTTP` response message format, which starts with a status line, followed by several header lines, an empty line and the body appended last.

The last piece of the puzzle is the threaded function, used for processing a single request (the connection is closed after the response is sent to the browser):

``` C#
private static void HandleClient(object param)
{
    TcpClient client = (TcpClient)param;
    Console.WriteLine("Accepted connection from {0}", client.Client.RemoteEndPoint);

    NetworkStream stream = client.GetStream();
    string request = ReadString(stream);
    Console.WriteLine(request);

    string response = GetResponse(request);

    byte[] replyBuffer = Encoding.UTF8.GetBytes(response);
    stream.Write(replyBuffer, 0, replyBuffer.Length);

    stream.Close();
    client.Close();
}
```

## 2. Using a Web Browser to Test the Server

Starting the server should enter the listening state, after which you can open a web browser and point it to the `http://localhost:8286/` address to start sending requests.

Here is an example in operation:

``` bash
Started listening ...
Accepted client connection from 127.0.0.1:36156
GET / HTTP/1.1
Accept: text/html, application/xhtml+xml, */*
Accept-Language: en-US
User-Agent: Mozilla/5.0

Accept-Encoding: gzip, deflate
Host: localhost:8286
DNT: 1
Connection: Keep-Alive
```

## 3. Additional Features

**Task 1.** - The current cookie sent by the server will be destroyed when the user closes the web browser. Verify that this is the case by running the server, refreshing the page a couple of times, to increase the count, closing the browser and re-open the server page, to make sure its views count will be reset to **1**.

After that has been verified, try to update the code so that the cookie is preserved for (expires after) a week. You can check on the Internet what the format of the cookie in the `Set-Cookie` header is, and which *part/syntax* controls the expiration.

**Task 2.** - Update this web application to include a **reset** functionality. Think about and come up with a way for the web browser client to initiate a reset request to the web server, which should set the number of views to **1**.
