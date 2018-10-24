import java.util.Scanner;

import org.apache.log4j.Logger;

public class RunClient {

	private static Logger LOGGER = Logger.getLogger(RunClient.class);

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		Scanner c = new Scanner(System.in);

		String in = c.nextLine();

		while (!in.equals("quit")) {

			in = c.nextLine();

			client.parse(in);
		}
	}
}
