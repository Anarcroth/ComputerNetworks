import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

class UDPServer {

	public static final Logger LOGGER = Logger.getLogger(UDPServer.class);

	private String data;

	private byte[] receivedData;

	private DatagramSocket serverSocket;

	private DatagramPacket receivedPacket;

	public UDPServer() throws SocketException {

		receivedData = new byte[1024];

		serverSocket = new DatagramSocket(9876);

		LOGGER.info("Initialized server");
	}

	public void listen() throws IOException {

		LOGGER.info("Listening on " + 9876);

		while (true) {

			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
			serverSocket.receive(receivePacket);
			data = new String(receivePacket.getData());
			receivedPacket = receivePacket;

			LOGGER.info("Received: " + data);
		}
	}

	public void send() throws IOException {

		serverSocket.send(new DatagramPacket(
				data.toUpperCase().getBytes(),
				data.length(),
				receivedPacket.getAddress(),
				receivedPacket.getPort()));
	}
}
