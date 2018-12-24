#!/usr/bin/env python

import socket

UDP_IP = "127.0.0.1"
UDP_PORT = 5005

print("UDP target IP:", UDP_IP)
print("UDP target port:", UDP_PORT)

# Create a UDP connection
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

while True:
    MESSAGE = input("input: ")
    sock.sendto(MESSAGE.encode("utf-8"), (UDP_IP, UDP_PORT))
    data, addrs = sock.recvfrom(1024)
    print("server response:", data.decode())
