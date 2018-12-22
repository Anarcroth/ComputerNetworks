#!/usr/bin/env python

import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 5004
BUFFER_SIZE = 1024
MESSAGE = "Hello, World!"

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((TCP_IP, TCP_PORT))
sock.send(MESSAGE.encode("utf-8"))

data = sock.recv(BUFFER_SIZE).decode()

sock.close()

print("received data:", data)
