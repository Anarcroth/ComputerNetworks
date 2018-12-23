# Computer Networks

## Assignment 1

### Instructions

Every assignment needs to provide two main artifacts - a complete working program, along with its full source code, and your solution documentation.

**Task Description**

Create a Book server that contains a list of book titles and returns to the client a randomly - selected title from that list. The client and server processes should communicate using sockets and implement the protocol described below. The server should operate as follows.

The server begins execution by opening a `.txt` file that has a predefined list of your favorite books’ titles, one per line. It should read those titles and store them in some data structure in memory.

Your client operated by sending one of two possible requests - `GET` and `ADD`. The client program should interact with the user and him/her specify which command (with possible arguments) to execute next, display the responses, etc.

**`GET`**

The `GET` command is sent from the client to the server to obtain a random title. The command consists of the string `GET`, followed by a newline character. After sending that command, the client should wait for a reply from the server, containing the title, on the same socket. After displaying the received title, the client should either terminate or ask the user for another command. When the server retrieves a `GET` command from a client, it should return the string `OK`, followed by a newline, followed by one randomly-selected title from the current book title list. After sending a title to the user, the server should wait for another request.

**`ADD`**

The `ADD` command, which is sent by the client to the server, begins a *multi-message* exchange allowing the user to record one or more book titles to the list of titles stored on the server. A client that wants to `ADD` one or more new titles should begin by sending the string `ADD`, followed by a space, followed by a username, followed by the newline character. The client should then wait for the server to return a string `OK` (indicating that the client is authorized to upload titles) or `NOTOK` (indicating the server will not allow the client to upload titles, in which case the client should quit). If the client has been authorized to upload one or more titles, the client responds to the `OK` string by sending one or more titles to be added to the server’s title list. Each title should be terminated with a newline. A blank line indicates that there are no more titles to be uploaded. After sending the `ADD` command, the new titles, and the closing line, the client should read and display the return code sent by the server.

---

Your server should be initialized with the names of one or more users who will be able to execute the `ADD` command. When the server receives an `ADD` command from a client, it should parse it and check whether the provided username is allowed to execute the `ADD` command. If the user is not allowed to execute the command, the server should return `NOTOK`. Otherwise it should return `OK` as described above. If the user is allowed to execute the `ADD` command and the server replies with `OK`, it should then read in and add to its list of currently stored titles, the new additional ones. If the titles are received correctly, and the proper termination line (empty line) is encountered, the server should return `OK`. In case of any error, the string `ERR` should be returned.
