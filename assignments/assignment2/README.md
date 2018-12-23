# Computer Networks

## Assignment 2

### Instructions

Every assignment needs to provide two main artifacts - a complete working program, along with its full source code, and your solution documentation.

**Task Description**

For this assignment, you are required to write a client process and a bank ATM server process using `TCP` sockets. The idea is that a bank customer uses the client process to remotely access his/her bank balance and perform typical ATM transactions (see below).

**NOTE**: You should use Stream (i.e. TCP) sockets and your server should support parallel communication with two or more clients at the same time (e.g. by using multi-threading or multi-processing).

Your server process should contain an in-memory list of at least *5* customers, each one having:

- A unique 4-digit PIN Number
- A unique 16-digit Account Number
- The current account’s balance
- The account holder’s name

The client process presents the customer with a menu of ATM options (commands) and the customer selects an option by typing in the appropriate menu item number from the keyboard. The client process then sends the appropriate request message to the server process. Note that some menu options will require the customer to enter further information from the keyboard, e.g. a bank pin number. The client process should prompt the customer for these. The client process should operate by sending the server one of a number of commands to query the server about the bank balance as specified by the customer’s keyboard input.

You need to devise a suitable protocol for the communication between the client and server processes to deal with the possible client requests and server replies. This protocol will comprise a number of suitable commands with zero, one or more arguments as required. Every server response should begin with an `OK` or `NOTOK`, followed by appropriate text messages.

**`START`**

The START command establishes a `TCP` connection between the client and the server processes.

**`CLOSE`**

The `CLOSE` command closes the connection between the client and the server.

**`AUTH`**

The `AUTH` command allows the customer to authenticate before the server by sending his/her *PIN*. The format of the `AUTH` command is listed below:

**`AUTH <PIN>`**

After sending the authentication request, the client should wait for a response from the server. The server will return `OK` (indicating that the client is authorized to perform account transactions) and `NOTOK` (indicating the server does not recognize the pin number in which case the client should close the connection). On receipt of the `AUTH` command, the server should check the pin number sent against its list of customers, and react accordingly.

If the customer has supplied a valid pin number, the server must remember this for future requests in this session.

For the customer to be able to perform any other transaction with the server, the customer must first have supplied a valid pin number to the server using the `AUTH` command and received back a positive acknowledgment of `OK`.

**`BALANCE`**

The `BALANCE` command allows the customer to ask for a statement of the amount of money (i.e. the balance) in his/her account. The server responds by returning the string `OK` (terminated with a newline), followed by the name of the customer, the number of the bank account and balance of the account in some currency.

**`DEBIT`**

This command allows the customer to withdraw a specified (via the keyboard) amount of money (in principle only!) from the account provided that the account has enough money otherwise the request is refused. The server replies with an `OK` or `NOTOK` as appropriate, and some suitable text, e.g. "Account balance has been updated and now holds X amount of money" or "Debit request refused as account only holds X amount of money".

**`CREDIT`**

This command allows the customer to deposit a specified amount of money in the account. Again, the server should respond it an appropriate way by giving the new balance in the account.
