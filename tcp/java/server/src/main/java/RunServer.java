import org.apache.log4j.Logger;

class RunServer {

	private static final Logger LOGGER = Logger.getLogger(RunServer.class);

	public static void main(String argv[]) throws Exception {

		ATMServer atmServer = new ATMServer();

		atmServer.listen();
	}
}