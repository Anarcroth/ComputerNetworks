#!/usr/bin/env python

import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 5004
BUFFER_SIZE = 1024

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((TCP_IP, TCP_PORT))
sock.listen(1)

connection, address = sock.accept()

print("Connection address:", address)

while True:
    data = connection.recv(BUFFER_SIZE).decode()

    if not data:
        break

    print("received data:", data)
    connection.sendall(data.encode("utf-8"))

connection.close()
