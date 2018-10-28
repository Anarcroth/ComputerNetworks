import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class ATMServer {

	private static final Logger LOGGER = Logger.getLogger(ATMServer.class);

	private final ServerSocket SERVER_SOCKET;

	private static final String CLIENTS_LIST = "clients.txt";

	private Socket connectionSocket;

	private ArrayList<Account> accounts;

	private DataOutputStream outToClient;

	public ATMServer() throws IOException {

		accounts = readAccounts(CLIENTS_LIST);

		SERVER_SOCKET = new ServerSocket(6789);
	}

	public void listen() throws IOException {

		while (true) {

			LOGGER.info("Starting ATM Server.");
			// init
			connectionSocket = SERVER_SOCKET.accept();
			ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			executorService.submit(() -> {
				try {
					while (true) {
						String clientCommand = get();
						Commands c = Commands.valueOf(clientCommand);
						parse(c);
					}
				} catch (Exception ioe) {
					LOGGER.error("Could not connect with new client", ioe);
				}
			});
		}
	}

	public String get() throws IOException {

		String inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())).readLine();

		LOGGER.info("Received: " + inFromClient);

		return inFromClient;
	}

	public void send(String message) throws IOException {

		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		outToClient.writeBytes(message + '\n');
	}

	public void parse(Commands c) {

		try {
			switch (c) {
				case START:
					LOGGER.info("Started a connection with " + connectionSocket.getPort());
					send("OK");
				case CLOSE:
					LOGGER.info("Closed connection form " + connectionSocket.getPort());
					break;
				case AUTH:
					auth();
					break;
				case BALANCE:
					balance();
					break;
				case DEBIT:
					debit();
					break;
				case CREDIT:
					credit();
					break;
				case PING:
					send("PONG");
					break;
				default:
					LOGGER.error("Unrecognized command " + c);
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not parse incomming " + c, ioe);
		}
	}

	public void updateClients() {

		File file = new File(CLIENTS_LIST);
		try (FileWriter fileWriter = new FileWriter(file, false)) {
			for (Account a : accounts) {
				fileWriter.write(a.getAccountName() + " " + a.getPin() + " " + a.getCurrentBalance() + "\n");
			}
		} catch (IOException ioe) {
			LOGGER.error("Could not update clients list", ioe);
		}
	}

	private void auth() throws IOException {

		send("OK");

		// This gets the expected pin form the client
		Integer receivedPin = Integer.parseInt(get());

		Account authAccount = accounts.stream().filter(a -> a.getPin().equals(receivedPin)).findFirst().orElse(null);

		try {
			authAccount.setAuthorized(true);
			authAccount.setPort(connectionSocket.getPort());
			send("OK");
		} catch (NullPointerException npe) {
			LOGGER.error("Unrecognized pin");
			send("NOTOK");
		}
	}

	private void balance() throws IOException {

		Account a = getAccountByPort(connectionSocket.getPort());

		send("OK");
		send(a.getAccountName() + ", " + a.getAccountId() + ", " + a.getCurrentBalance() + " $");
	}

	private void debit() throws IOException {

		Account a = getAccountByPort(connectionSocket.getPort());
		send("OK");

		try {
			// This get's the expected debit amount from the client
			Integer am = Integer.parseInt(get());

			if (a.getCurrentBalance() < am) {

				send("NOTOK");
				send("Amount limited for withdraw!");
			} else {

				Integer diff = a.getCurrentBalance() - am;
				a.setCurrentBalance(diff);

				send("OK");
				send("Widrawn " + am.toString() + "$");
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error("This is not a valid input");
			send("NOTOK");
		}
	}

	private void credit() throws IOException {

		Account a = getAccountByPort(connectionSocket.getPort());

		send("OK");

		try {
			// This get's the expected credit amount from the client
			Integer am = Integer.parseInt(get());
			Integer sum = a.getCurrentBalance() + am;
			a.setCurrentBalance(sum);

			send("OK");
			send("Added " + am.toString() + "$");
		} catch (NumberFormatException nfe) {
			LOGGER.error("This is not a valid input");
			send("NOTOK");
		}
	}

	private Account getAccountByPort(int port) {

		for (Account a : accounts) {
			if (a.getPort() == port) {
				return a;
			}
		}
		return null;
	}

	private ArrayList<Account> readAccounts(String entity) throws IOException {

		ArrayList<Account> list = new ArrayList<>();

		File file = new File(entity);

		try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
			stream.forEach(b -> {
				String[] ac = b.split(" ");
				String name = ac[0];
				Integer pin = Integer.parseInt(ac[1]);
				Integer balance = Integer.parseInt(ac[2]);
				list.add(new Account(name, pin, balance));
			});
		}
		return list;
	}
}
