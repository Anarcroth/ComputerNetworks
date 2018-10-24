import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class RunClient {

	private static final Logger LOGGER = Logger.getLogger(RunClient.class);

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		while (!client.getInput().get(0).equals("quit")) {

			ArrayList<String> input = getUserInput();

			client.parse(input);
		}
	}

	public static ArrayList<String> getUserInput() {

		ArrayList<String> input = new ArrayList<>();

		while (!validUserInput(input)) {

			LOGGER.info("Enter a command: ");

			Scanner in = new Scanner(System.in);

			String rawInput = in.nextLine();

			in.close();

			input = new ArrayList<>(Arrays.asList(rawInput.split(" ")));
		}

		return input;
	}

	// This is a very basic check for any user input if it's valid or not
	// Since we don't care if the user passes any arguments to the possible commands (with the exception of AUTH),
	// we just make sure that the first word of the whole line input is a valid command
	private static boolean validUserInput(ArrayList<String> input) {

		try {

			Commands.valueOf(input.get(0));

			return true;

		} catch (IllegalArgumentException iae) {

			LOGGER.error(input.get(0) + " is not a supported command");

			return false;
		} catch (Exception e) {

			return false;
		}
	}
}
