import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

class BookServer {

	public static final Logger LOGGER = Logger.getLogger(BookServer.class);

	private String data;

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

		File file = new File("bookList.txt");
		try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {
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

		DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
		serverSocket.receive(receivePacket);
		data = new String(receivePacket.getData());
		receivedPacket = receivePacket;

		LOGGER.info("Received: " + data);
	}

	public void send() throws IOException {

		serverSocket.send(new DatagramPacket(
				data.toUpperCase().getBytes(),
				data.length(),
				receivedPacket.getAddress(),
				receivedPacket.getPort()));
	}
}
