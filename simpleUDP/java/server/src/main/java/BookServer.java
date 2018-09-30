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

	private byte[] receivedData;

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
		String receievedCommand = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

		LOGGER.info("Received: " + receievedCommand);

		parseReceivedMessage(receievedCommand);
	}

	private void parseReceivedMessage(String receievedCommand) {

		try {
			if (receievedCommand.startsWith("ADD")) {

				String user = receievedCommand.substring(4);
				engageInAddingBooks(user);
			} else if (receievedCommand.equals("GET")) {

				send("OK\n" + getRandomBook());
			} else {

				send(receievedCommand + " is not a recognized command.\nERR 400 Bad Request");
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not parse received message", ioe);
		}
	}

	private void engageInAddingBooks(String user) throws IOException {

		LOGGER.info("Engaging in adding books");

		while (true) {
			if (isUserValid(user)) {
				send("OK");
				receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				serverSocket.receive(receivedPacket);
				String receievedCommand = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

				LOGGER.info(receievedCommand);

				List<String> newBooks = new ArrayList<>(Arrays.asList(receievedCommand.split(",")));

				appendBookList(newBooks);

				send("OK");

				break;

			} else {

				LOGGER.info("Invalid user");
				send("NOTOK");

				break;
			}
		}
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
				FileWriter fw = new FileWriter("/home/anarcroth/git-anarcroth/ComputerNetworksCourse/simpleUDP/java/server/bookList.txt", true);
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
