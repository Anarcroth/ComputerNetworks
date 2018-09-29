import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

class BookServer {

	public static final Logger LOGGER = Logger.getLogger(BookServer.class);

	private byte[] receivedData;

	private DatagramSocket serverSocket;

	private DatagramPacket receivedPacket;

	private ArrayList<String> bookList;

	public BookServer() throws SocketException, IOException {

		receivedData = new byte[1024];

		serverSocket = new DatagramSocket(9876);

		bookList = getBooks();

		LOGGER.info("Initialized server");
	}

	private ArrayList<String> getBooks() throws IOException {

		ArrayList<String> list = new ArrayList<>();

		File file = new File("/home/anarcroth/git-anarcroth/ComputerNetworksCourse/simpleUDP/java/server/bookList.txt");

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
		String data = new String(receivedPacket.getData()).trim();

		LOGGER.info("Received: " + data);

		parseReceivedMessage(data);
	}

	private void parseReceivedMessage(String message) throws IOException {

		if (message.equals("GET")) {

			send();
		} else if (message.startsWith("ADD")) {

		}
	}

	public void send() throws IOException {

		String randomBook = "\nOK\n" + getRandomBook();

		serverSocket.send(new DatagramPacket(
				randomBook.getBytes(),
				randomBook.length(),
				receivedPacket.getAddress(),
				receivedPacket.getPort()));
	}
}
