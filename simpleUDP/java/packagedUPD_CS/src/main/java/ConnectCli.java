import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class ConnectCli {

	private static final Logger LOGGER = Logger.getLogger(ConnectCli.class);

	public static void main(String args[]) throws Exception {

		UDPClient client = new UDPClient();

		String clientInput = new BufferedReader(new InputStreamReader(System.in)).readLine();

		client.send(clientInput);

		LOGGER.info("Answer from server:" + client.receive());
	}
}
