import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class);

	private String input;

	private Socket CLIENT_SOCKET;

	public Client() throws IOException {

		CLIENT_SOCKET = new Socket("localhost", 6789);
	}

	public void send(String command) throws IOException {

		DataOutputStream outToServer = new DataOutputStream(CLIENT_SOCKET.getOutputStream());

		outToServer.writeBytes(input + '\n');
	}

	public void get() throws IOException {

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(CLIENT_SOCKET.getInputStream()));
	}

	public String getUserInput() {

		Scanner in = new Scanner(System.in);

		input = in.nextLine();

		in.close();

		return input;
	}

	private void start() {

	}

	private void close() throws IOException {

		CLIENT_SOCKET.close();
	}

	private void auth() {

	}

	private void balance() {

	}

	private void debit() {
		
	}
}