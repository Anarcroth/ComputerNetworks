import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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

	public Client() throws IOException {

		input = new ArrayList<>();
		input.add("");

		UIN = new Scanner(System.in);

		authorized = false;

		CLIENT_SOCKET = new Socket("localhost", 6789);
	}

	public void getUserInput() {

		drawMenu();

		LOGGER.info("Enter a command: ");

		String rawInput = UIN.nextLine();

		checkUserInput(new ArrayList<>(Arrays.asList(rawInput.split(" "))));
	}

	public void getUserInput(String message) {

		LOGGER.info(message);

		String rawInput = UIN.nextLine();

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
					auth(command.get(1));
					break;
				case BALANCE:
					balance();
					break;
				case DEBIT:
					break;
				case CREDIT:
					credit(command.get(1));
					break;
				case PING:
					ping();
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

		UIN.close();

		CLIENT_SOCKET.close();
	}

	private void auth(String pin) throws IOException {

		send("AUTH " + pin);
		if (get().equals("OK")) {

			authorized = true;
		} else {

			LOGGER.error("You did not authorize correctly. Missing or wrong pin!");
		}
	}

	private void balance() throws IOException {

		if (authorized) {

			send("BALANCE");
			String balanceAnswer = get();

			LOGGER.info("Your balance is " + balanceAnswer);
		} else {

			getUserInput("Please enter your pin number and authorize first");
		}
	}

	private void debit() throws IOException {

		if (authorized) {

		} else {

			getUserInput("Please enter your pin number and authorize first");
		}
	}

	private void credit(String amount) throws IOException {

		if (authorized) {

			send("CREDIT " + amount);
			String creditAnswer = get();

			LOGGER.info("You deposited " + amount);

			// Tell the balance to the client
			balance();
		} else {

			getUserInput("Please enter your pin number and authorize first");
		}
	}

	private void ping() throws IOException {

		send("ping");
		get();
	}

	public void send(String message) throws IOException {

		outToServer.writeBytes(message + '\n');
	}

	private String get() throws IOException {

		String inFromServer = new BufferedReader(new InputStreamReader(CLIENT_SOCKET.getInputStream())).readLine();

		LOGGER.info(inFromServer);

		return inFromServer;
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
		LOGGER.info("3. Balance");
		LOGGER.info("4. Deibt");
		LOGGER.info("5. Credit");
		LOGGER.info("0. Ping");
	}
}