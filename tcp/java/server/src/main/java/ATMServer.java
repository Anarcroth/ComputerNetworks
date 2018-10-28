import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class ATMServer {

	private static final Logger LOGGER = Logger.getLogger(ATMServer.class);

	private Socket connectionSocket;

	private ArrayList<Account> accounts;

	private ServerSocket SERVER_SOCKET;

	private DataOutputStream outToClient;

	private static final String CLIENTS_LIST = "clients.txt";

	public ATMServer() throws IOException {

		accounts = readAccounts(CLIENTS_LIST);

		SERVER_SOCKET = new ServerSocket(6789);
	}

	public void init() throws IOException {

		connectionSocket = SERVER_SOCKET.accept();
	}

	public void listen() throws IOException {

		while (true) {

			init();

			while (true) {
				String clientCommand = get();
				Commands c = Commands.valueOf(clientCommand);
				parse(c);
			}
//			Thread t = new Thread(() -> {
//
//				try {
//
//					while (true) {
//
//						String clientCommand = get();
//						Commands c = Commands.valueOf(clientCommand);
//						parse(c);
//
//						//						Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//						//						for (Thread t1 : threadSet) {
//						//
//						//							LOGGER.info(t1.getName() + " - " + t1.getId());
//						//						}
//						//						LOGGER.info("\n");
//					}
//
//				} catch (Exception ioe) {
//
//					LOGGER.error("Could not connect with new client", ioe);
//				}
//			});
//
//			// TODO: create a thread pool that dispatches the different client conenctions
//			t.start();
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
					close();
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
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not parse incomming " + c, ioe);
		}
	}

	private void close() throws IOException {

		for (Account a :  accounts) {

			if (connectionSocket.getPort() == a.getPort()) {

				accounts.remove(a);
			}
		}
	}

	private void auth() throws IOException {

		send("OK");

		// This get's the expected pin form the client
		Integer receivedPin = Integer.parseInt(get());

		for (Account a : accounts) {

			if (receivedPin.equals(a.getPin())) {

				send("OK");

				a.setAuthorized(true);
				a.setPort(connectionSocket.getPort());
			}
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

		// This get's the expected debit amount from the client
		Integer am = Integer.parseInt(get());
		String a1 = am.toString();

		if (a.getCurrentBalance() < am) {

			send("NOTOK");
			send("Amount limited for withdraw!");
		} else {

			Integer diff = a.getCurrentBalance() - am;
			a.setCurrentBalance(diff);

			send("OK");
			send("Widrawn " + a1 + "$");
		}
	}

	private void credit() throws IOException {

		Account a = getAccountByPort(connectionSocket.getPort());

		send("OK");

		// This get's the expected credit amount from the client
		Integer am = Integer.parseInt(get());

		Integer sum = a.getCurrentBalance() + am;

		a.setCurrentBalance(sum);

		send("OK");
		send("Added " + am.toString() + "$");
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
