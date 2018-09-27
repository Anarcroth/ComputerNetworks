public class ConnectServer {

	public static void main(String args[]) throws Exception {

		UDPServer bookServer = new UDPServer();

		bookServer.listen();

		bookServer.send();
	}
}

