import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;

class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class);

	private ArrayList<String> input;

	private Socket CLIENT_SOCKET;

	private DataOutputStream outToServer;

	public Client() throws IOException {

		input = new ArrayList<>();
		input.add("");

		CLIENT_SOCKET = new Socket("localhost", 6789);
	}

	public void send(String command) throws IOException {

		outToServer.writeBytes(command + '\n');
	}

	public ArrayList<String> getInput() {

		return input;
	}

	public void parse(ArrayList<String> command) {

		LOGGER.info("Parsing the command " + command);

		Commands c = Commands.valueOf(command.get(0));

		try {

			switch (c) {

				case START:
					start();
					break;

				case CLOSE:
					close();
					break;

				case AUTH:
					break;

				case DEBIT:
					break;

				case CREDIT:
					break;

				case BALANCE:
					break;

				case PING:
					ping();
					get();
					break;

				default:
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not execute command " + command, ioe);
		}
	}

	private void start() throws IOException {

		LOGGER.info("Creating a connection to the server");

		outToServer = new DataOutputStream(CLIENT_SOCKET.getOutputStream());
	}

	private void close() throws IOException {

		LOGGER.info("Closing the connection");

		CLIENT_SOCKET.close();
	}

	private void auth() {

	}

	private void balance() {

	}

	private void debit() {

	}

	private void ping() throws IOException {

		outToServer.writeBytes("ping" + '\n');
	}

	private void get() throws IOException {

		String inFromServer = new BufferedReader(new InputStreamReader(CLIENT_SOCKET.getInputStream())).readLine();

		LOGGER.info(inFromServer);
	}
}