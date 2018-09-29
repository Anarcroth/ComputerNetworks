import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class RunClient {

	private static final Logger LOGGER = Logger.getLogger(RunClient.class);

	public static Client client;

	public static boolean quit;

	public static void main(String[] args) throws Exception {

		client = new Client();

		LOGGER.info("Type '?' or 'help' for help");

		quit = false;
		while (!quit) {

			LOGGER.info("Input: ");
			Scanner in = new Scanner(System.in);
			String input = in.nextLine();

			try {
				parse(input);

			} catch (IOException ioe) {

				LOGGER.error("Could not parse input", ioe);
			}
		}
	}

	public static void parse(String input) throws IOException {

		if (input.equals("?") || input.equals("help")) {

			client.showHelp();
		} else if (input.equals("GET")) {

			client.send(input);
			LOGGER.info(client.receive());
		} else if (input.startsWith("ADD")) {


		} else if (input.startsWith("quit")) {

			client.quit();
			quit = true;
		}
	}
}
