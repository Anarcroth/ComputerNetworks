import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class);

	private String receivedData;

	private InetAddress ipAddress;

	private DatagramSocket clientSocket;

	public Client() throws UnknownHostException, IOException {


		clientSocket = new DatagramSocket();
		ipAddress = InetAddress.getByName("localhost");

		LOGGER.info("Initialized client");
	}

	public void send(String message) throws IOException {

		DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), ipAddress, 9876);
		clientSocket.send(sendPacket);
	}

	public void receive() throws IOException {

		byte[] rawReceivedData = new byte[1024];
		DatagramPacket packet = new DatagramPacket(rawReceivedData, rawReceivedData.length);
		clientSocket.receive(packet);
		receivedData = new String(packet.getData(), 0, packet.getLength());

		LOGGER.info(receivedData);
	}

	public void showHelp() {

		LOGGER.info("The client is capable of the following commands:");
		LOGGER.info("ADD - Add one or more books to the books server.");
		LOGGER.info("GET - Get a random book title from the books server.");
		LOGGER.info("?/help - Print help.");
	}

	public void quit() {

		LOGGER.info("Closing the connection. . .");

		clientSocket.close();

		System.exit(0);
	}

	public void parseResponse() throws IOException {

		if (receivedData.equals("OK")) {

			Scanner input = new Scanner(System.in);
			ArrayList<String> lines = new ArrayList<String>();
			String lineNew;

			while (input.hasNextLine()) {
				lineNew = input.nextLine();
				if (lineNew.isEmpty()) {
					break;
				}
				lines.add(lineNew);
			}

			send(String.join(",", lines));
			receive();

		} else if (receivedData.equals("NOTOK")) {

			LOGGER.info("This cilent is not part of the users who can access the book server.");
			quit();
		}
	}
}
