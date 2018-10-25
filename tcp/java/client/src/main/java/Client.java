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

	Scanner in;

	public Client() throws IOException {

		input = new ArrayList<>();
		input.add("");

		in = new Scanner(System.in);

		CLIENT_SOCKET = new Socket("localhost", 6789);
	}

	public void send(String command) throws IOException {

		outToServer.writeBytes(command + '\n');
	}

	public void getUserInput() {

		LOGGER.info("Enter a command: ");

		String rawInput = in.nextLine();

		checkUserInput(new ArrayList<>(Arrays.asList(rawInput.split(" "))));
	}

	public ArrayList<String> getInput() {

		return input;
	}

	public void end() {

		try {

			close();
		} catch (IOException ioe) {

			LOGGER.error("Could not close connection", ioe);
		}
	}

	public void parse(Commands c, ArrayList<String> command) {

		LOGGER.info("Parsing the command " + c);

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

		in.close();

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

	// This is a very basic check for any user input if it's valid or not
	// Since we don't care if the user passes any arguments to the possible commands (with the exception of AUTH),
	// we just make sure that the first word of the whole line input is a valid command
	private void checkUserInput(ArrayList<String> input) {

		try {

			Commands c = Commands.valueOf(input.get(0));

			parse(c, input);

		} catch (IllegalArgumentException iae) {

			LOGGER.error(input.get(0) + " is not a supported command");

			getUserInput();
		}
	}
}