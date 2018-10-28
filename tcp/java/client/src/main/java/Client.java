import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import jline.console.ConsoleReader;
import org.apache.log4j.Logger;

class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class);

	private final Scanner UIN;

	private boolean authorized;

	private Socket CLIENT_SOCKET;

	private ArrayList<String> input;

	private DataOutputStream outToServer;

	private BufferedReader inFromServer;

	public Client() throws IOException {

		input = new ArrayList<>();
		input.add("");

		UIN = new Scanner(System.in);

		authorized = false;

		CLIENT_SOCKET = new Socket("localhost", 6789);
	}

	public void getUserInput() {

		drawMenu();

		LOGGER.info("Enter a command number: ");

		String rawInput = UIN.nextLine();

		checkUserInput(rawInput);
	}

	public String getUserInput(String message) {

		LOGGER.info(message);

		String rawInput = UIN.nextLine();

		return rawInput;
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

	public void parse(Commands c) {

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
					ping();
					break;
				default:
			}
		} catch (IOException ioe) {

			LOGGER.error("Could not execute " + c, ioe);
		}
	}

	private void start() throws IOException {

		LOGGER.info("Creating a connection to the server");

		outToServer = new DataOutputStream(CLIENT_SOCKET.getOutputStream());

		inFromServer = new BufferedReader(new InputStreamReader(CLIENT_SOCKET.getInputStream()));

		send("START");
		get();
	}

	private void close() throws IOException {

		LOGGER.info("Closing the connection");

		send("CLOSE");

		CLIENT_SOCKET.close();

		System.exit(0);
	}

	private void auth() throws IOException {

		send("AUTH");
		get();
		String pin = getUserInput("Enter your pin number: ");
		send(pin);

		if (get().equals("OK")) {

			authorized = true;
		} else {

			LOGGER.error("You did not authorize correctly. Missing or wrong pin!");
		}
	}

	private void balance() throws IOException {

		if (authorized) {

			send("BALANCE");
			if (get().equals("OK")) {

				LOGGER.info(get());
			}
		} else {

			LOGGER.info("Please enter your pin number and authorize first");
			auth();
		}
	}

	private void debit() throws IOException {

		if (authorized) {

			send("DEBIT");
			if (get().equals("OK")) {
				String debit = getUserInput("Enter debit amount: ");
				send(debit);
			}

			LOGGER.info(get());
			LOGGER.info(get());

			// Tell the balance to the client
			balance();
		} else {

			LOGGER.info("Please enter your pin number and authorize first");
			auth();
		}
	}

	private void credit() throws IOException {

		if (authorized) {

			send("CREDIT");
			if (get().equals("OK")) {
				String credit = getUserInput("Enter the deposit amount: ");
				send(credit);
			}

			LOGGER.info(get());
			LOGGER.info(get());

			// Tell the balance to the client
			balance();
		} else {

			getUserInput("Please enter your pin number and authorize first");
		}
	}

	private void ping() throws IOException {

		send("PING");
		get();
	}

	public void send(String message) throws IOException {

		outToServer.writeBytes(message + '\n');
	}

	private String get() throws IOException {

		String inFromServer = this.inFromServer.readLine();

		LOGGER.info(inFromServer);

		return inFromServer;
	}

	// This is a very basic check for any user input if it's valid or not
	// Since we don't care if the user passes any arguments to the possible commands (with the exception of AUTH),
	// we just make sure that the first word of the whole line input is a valid command
	private void checkUserInput(String input) {

		try {

			Integer inputNumber = Integer.parseInt(input);

			if (inputNumber >= 0 && inputNumber <= 6) {

				Commands c = Commands.getCommand(inputNumber);
				parse(c);
			}

		} catch (IllegalArgumentException iae) {

			LOGGER.error(input + " is not a supported command");

			getUserInput();
		}
	}

	private void drawMenu() {

		try {

			ConsoleReader r = new ConsoleReader();
			r.clearScreen();

		} catch (IOException ioe) {

			LOGGER.error("Could not initialize a console reader to clear screen. Your screen won't be cleared", ioe);
		}

		LOGGER.info("--- ATM Services ---");
		LOGGER.info("1. Start");
		LOGGER.info("2. Close");
		LOGGER.info("3. Authenticate");
		LOGGER.info("4. Balance");
		LOGGER.info("5. Deibt");
		LOGGER.info("6. Credit");
		LOGGER.info("0. Ping");
	}
}