import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class);

	private byte[] receiveData;

	private InetAddress ipAddress;

	private DatagramSocket clientSocket;

	public Client() throws UnknownHostException, IOException {

		receiveData = new byte[1024];
		clientSocket = new DatagramSocket();
		ipAddress = InetAddress.getByName("localhost");

		LOGGER.info("Initialized client");
	}

	public void send(String message) throws IOException {

		DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), ipAddress, 9876);
		clientSocket.send(sendPacket);
	}

	public String receive() throws IOException {

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);

		clientSocket.close();

		return new String(receivePacket.getData());
	}
}
