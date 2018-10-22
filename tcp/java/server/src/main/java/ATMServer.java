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

	private final ServerSocket CLIENT_SOCKET;

	public ATMServer() throws IOException {

		accounts = new ArrayList<>(5);
		accounts.forEach(a -> a = new Account());

		CLIENT_SOCKET = new ServerSocket(6789);
	}

	public void getResponse() throws IOException {

		connectionSocket = CLIENT_SOCKET.accept();

		BufferedReader inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

		LOGGER.info("Received: " + inFromClient.readLine());

	}

	public void sendResponse() throws IOException {

		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		outToClient.writeBytes("OK");
	}

	public void drawMenu() {

	}
}
