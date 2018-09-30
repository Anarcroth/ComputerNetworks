import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

class BookServer {

	public static final Logger LOGGER = Logger.getLogger(BookServer.class);

	private static final String USERS_LIST = "usersList.txt";

	private static final String BOOKS_LIST = "bookList.txt";

	private byte[] receivedData;

	private DatagramSocket serverSocket;

	private DatagramPacket receivedPacket;

	private ArrayList<String> users;

	private ArrayList<String> bookList;

	private String clientData;

	public BookServer() throws SocketException, IOException {

		receivedData = new byte[1024];

		serverSocket = new DatagramSocket(9876);

		users = get(USERS_LIST);

		bookList = get(BOOKS_LIST);

		LOGGER.info("Initialized server");
		LOGGER.info("Listening on port " + 9876);
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

		receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		serverSocket.receive(receivedPacket);
		clientData = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

		LOGGER.info("Received: " + clientData);
	}

	public void parseReceivedMessage() {

		try {
			if (clientData.startsWith("ADD")) {

				String user = clientData.substring(4);
				initAddingBooks(user);
			} else if (clientData.equals("GET")) {

				send("OK\n" + getRandomBook());
			} else {

				send(clientData + " is not a recognized command.\nERR Bad Request");
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not parse received message", ioe);
		}
	}

	private void initAddingBooks(String user) throws IOException {

		LOGGER.info("Init adding books");

		if (isUserValid(user)) {

			getNewBooks();

		} else if (!isUserValid(user)) {

			LOGGER.info("Invalid user");
			send("NOTOK");

		} else {

			send("ERR");
		}
	}

	private void getNewBooks() throws IOException {

		send("OK");

		listen();

		List<String> newBooks = new ArrayList<>(Arrays.asList(clientData.split(",")));

		appendBookList(newBooks);

		send("OK");
	}

	private boolean isUserValid(String user) throws IOException {

		return users.contains(user) ? true : false;
	}

	private void send(String message) throws IOException {

		LOGGER.info(message);
		serverSocket.send(new DatagramPacket(
				message.getBytes(),
				message.length(),
				receivedPacket.getAddress(),
				receivedPacket.getPort()));
	}

	private void appendBookList(List<String> newBooks) {

		try (
				FileWriter fw = new FileWriter(BOOKS_LIST, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			for (String book : newBooks) {

				out.println(book);
			}

		} catch (IOException ioe) {

			LOGGER.error("Could not append new books", ioe);
		}
	}
}
