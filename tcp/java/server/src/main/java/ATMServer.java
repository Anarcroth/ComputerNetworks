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

	public void parse(String message) {

		Commands c = Commands.valueOf(message);

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
				case CREDIT:
					credit();
					break;
				case DEBIT:
					debit();
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

		for (Account a : accounts) {

			if (a.getPort() == connectionSocket.getPort()) {

				send("OK\n" + a.getAccountName() + ", " + a.getAccountId() + ", " + a.getCurrentBalance() + " $");
			}
		}
	}

	private void credit() throws IOException {

	}

	private void debit() throws IOException {

	}
}
