public class RunClient {

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		// This closes the connection quietly
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				client.end();
			}
		});

		while (true) {

			client.getUserInput();
		}
	}
}
