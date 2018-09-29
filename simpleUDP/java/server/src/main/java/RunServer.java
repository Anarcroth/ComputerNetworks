public class RunServer {

	public static void main(String args[]) throws Exception {

		BookServer bookServer = new BookServer();

		bookServer.listen();

		bookServer.send();
	}
}

