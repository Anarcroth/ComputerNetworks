class RunServer {

	public static void main(String argv[]) throws Exception {

		ATMServer atmServer = new ATMServer();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				atmServer.updateClients();
			}
		});

		atmServer.listen();
	}
}