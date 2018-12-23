#!/usr/bin/env python

import socket

UDP_IP = "127.0.0.1"
UDP_PORT = 5005
MESSAGE = input("input: ")

print("UDP target IP:", UDP_IP)
print("UDP target port:", UDP_PORT)
print("message:", MESSAGE)

# Create a UDP connection
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.sendto(MESSAGE.encode("utf-8"), (UDP_IP, UDP_PORT))
