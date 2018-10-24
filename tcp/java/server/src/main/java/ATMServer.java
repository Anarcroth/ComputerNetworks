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

						getResponse();
						sendResponse();

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

	public void getResponse() throws IOException {

		BufferedReader inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

		LOGGER.info("Received: " + inFromClient.readLine());
	}

	public void sendResponse() throws IOException {

		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		outToClient.writeBytes("OK\n");
	}

	public void drawMenu() {

	}
}
