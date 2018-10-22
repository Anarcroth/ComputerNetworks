import java.util.Set;

import org.apache.log4j.Logger;

class Server {

	private static final Logger LOGGER = Logger.getLogger(Server.class);

	public static void main(String argv[]) throws Exception {

		ATMServer atmServer = new ATMServer();

		while (true) {

			Thread t = new Thread(() -> {

				try {

					while (true) {

						Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
						for (Thread t1 : threadSet) {

							LOGGER.info(t1.getName() + " - " + t1.getId());
						}
						LOGGER.info("\n");
					}

				} catch (Exception ioe) {

					LOGGER.error("Could not connect with new client", ioe);
				}
			});

			// TODO: create a thread pool that dispatches the different client conenctions
			t.start();
		}
	}
}