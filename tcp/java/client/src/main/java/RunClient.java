import org.apache.log4j.Logger;

public class RunClient {

	private static final Logger LOGGER = Logger.getLogger(RunClient.class);

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		while (!client.getInput().get(0).equals("quit")) {

			client.getUserInput();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				client.end();
			}
		});
	}
}
