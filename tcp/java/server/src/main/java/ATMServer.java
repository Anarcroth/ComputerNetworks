import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ATMServer {

	private static final Logger LOGGER = Logger.getLogger(ATMServer.class);

	private Socket connectionSocket;

	private ArrayList<Account> accounts;

	private ServerSocket SERVER_SOCKET;

	public ATMServer() throws IOException {

		accounts = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			accounts.add(new Account());
		}

		SERVER_SOCKET = new ServerSocket(6789);
	}

	public void init() throws IOException {

		connectionSocket = SERVER_SOCKET.accept();
	}

	public void listen() throws IOException {

		while (true) {

			init();

			Thread t = new Thread(() -> {

				try {

					while (true) {

						String clientCommand = get();
						parse(clientCommand);

						//						Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
						//						for (Thread t1 : threadSet) {
						//
						//							LOGGER.info(t1.getName() + " - " + t1.getId());
						//						}
						//						LOGGER.info("\n");
					}

				} catch (Exception ioe) {

					LOGGER.error("Could not connect with new client", ioe);
				}
			});

			// TODO: create a thread pool that dispatches the different client conenctions
			t.start();
		}
	}

	public String get() throws IOException {

		String inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())).readLine();

		LOGGER.info("Received: " + inFromClient);

		return inFromClient;
	}

	public void send(String message) throws IOException {

		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		outToClient.writeBytes(message + "\n");
	}

	public void parse(String command) {

		String[] messages = command.split(" ");
		Commands c = Commands.valueOf(messages[0]);

		try {
			switch (c) {
				case PING:
					send("pong");
					break;
				case START:
				case CLOSE:
					send("OK");
					break;
				case AUTH:
					auth();
					break;
				case BALANCE:
					balance();
					break;
				case DEBIT:
					debit(messages[1]);
					break;
				case CREDIT:
					credit();
					break;
				default:
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not parse incomming " + c, ioe);
		}
	}

	private void auth() throws IOException {

		Integer pin = Integer.parseInt(get());

		for (Account a : accounts) {

			if (pin.equals(a.getPin())) {

				send("OK");

				a.setAuthorized(true);
				a.setPort(connectionSocket.getPort());
			}
		}
	}

	private void balance() throws IOException {

		Account a = getAccountByPort(connectionSocket.getPort());

		send("OK\n" + a.getAccountName() + ", " + a.getAccountId() + ", " + a.getCurrentBalance() + " $");
	}

	private void debit(String amount) throws IOException {

		Integer am = Integer.parseInt(amount);

		Account a = getAccountByPort(connectionSocket.getPort());

		if (a.getCurrentBalance() < am) {

			send("NOTOK\nAmount limited for withdraw!");
		} else {

			Integer diff = a.getCurrentBalance() - am;
			a.setCurrentBalance(diff);

			send("OK\n" + am + " $ were widraw.");
		}
	}

	private void credit() throws IOException {

	}

	private Account getAccountByPort(int port) {

		for (Account a : accounts) {

			if (a.getPort() == port) {

				return a;
			}
		}

		return null;
	}
}
