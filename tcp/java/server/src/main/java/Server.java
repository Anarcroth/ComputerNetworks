import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import org.apache.log4j.Logger;

class Server {

	private static final Logger LOGGER = Logger.getLogger(Server.class);

	public static void main(String argv[]) throws Exception {

		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();

			Thread t = new Thread(() -> {

				try {

					while (true) {

						BufferedReader inFromClient =
								new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
						DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
						String clientSentence = inFromClient.readLine();

						LOGGER.info("Received: " + clientSentence);

						String capitalizedSentence = clientSentence.toUpperCase() + '\n';
						outToClient.writeBytes(capitalizedSentence);

						LOGGER.info("Send message");
						LOGGER.info(connectionSocket.isConnected());

						LOGGER.info("port: " + connectionSocket.getPort());

						Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
						for (Thread t1 : threadSet) {

							LOGGER.info(t1.getName() + " - " + t1.getId());
						}
						LOGGER.info("\n");
					}

				} catch (IOException ioe) {

					LOGGER.error("Could not connect with new client", ioe);
				}
			});

			t.start();
		}
	}
}