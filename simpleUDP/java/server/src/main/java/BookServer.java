import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

class BookServer {

	public static final Logger LOGGER = Logger.getLogger(BookServer.class);

	private byte[] receivedData;

	private String receievedCommand;

	private DatagramSocket serverSocket;

	private DatagramPacket receivedPacket;

	private ArrayList<String> users;

	private ArrayList<String> bookList;

	public BookServer() throws SocketException, IOException {

		receivedData = new byte[1024];

		serverSocket = new DatagramSocket(9876);

		users = get("/home/anarcroth/git-anarcroth/ComputerNetworksCourse/simpleUDP/java/server/usersList.txt");

		bookList = get("/home/anarcroth/git-anarcroth/ComputerNetworksCourse/simpleUDP/java/server/bookList.txt");

		LOGGER.info("Initialized server");
	}

	private ArrayList<String> get(String entity) throws IOException {

		ArrayList<String> list = new ArrayList<>();

		File file = new File(entity);

		try (Stream<String> stream = Files.lines(file.toPath())) {
			stream.forEach(b -> list.add(b));
		}

		return list;
	}

	private String getRandomBook() {

		Random rand = new Random();

		return bookList.get(rand.nextInt(bookList.size()));
	}

	public void listen() throws IOException {

		LOGGER.info("Listening on port " + 9876);

		receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		serverSocket.receive(receivedPacket);
		receievedCommand = new String(receivedPacket.getData()).trim();

		LOGGER.info("Received: " + receievedCommand);
	}

	public void parseReceivedMessage() {

		try {
			if (receievedCommand.equals("GET")) {

				send("OK\n" + getRandomBook());
			} else if (receievedCommand.startsWith("ADD")) {

				validateUser(receievedCommand);
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not parse receive message", ioe);
		}
	}

	private void validateUser(String message) throws IOException {

		LOGGER.info(message.substring(4));
		if (users.contains(message.substring(4))) {

			LOGGER.info("ITS OK");
			send("OK");
		}
	}

	private void send(String message) throws IOException {

		serverSocket.send(new DatagramPacket(
				message.getBytes(),
				message.length(),
				receivedPacket.getAddress(),
				receivedPacket.getPort()));
	}
}
