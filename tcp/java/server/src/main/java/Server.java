import org.apache.log4j.Logger;

class Server {

	private static final Logger LOGGER = Logger.getLogger(Server.class);

	public static void main(String argv[]) throws Exception {

		ATMServer atmServer = new ATMServer();

		atmServer.listen();
	}
}