import org.apache.log4j.Logger;

public class RunClient {

	private static Logger LOGGER = Logger.getLogger(RunClient.class);

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		while (!client.getUserInput().equals("quit")) {


		}
	}
}
