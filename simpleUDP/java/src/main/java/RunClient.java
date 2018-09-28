import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class RunClient {

	private static final Logger LOGGER = Logger.getLogger(RunClient.class);

	public static void main(String args[]) throws Exception {

		Client client = new Client();

		LOGGER.info("Enter a message to send to the server: ");

		String clientInput = new BufferedReader(new InputStreamReader(System.in)).readLine();

		client.send(clientInput);

		LOGGER.info("Answer from server: " + client.receive());
	}
}
