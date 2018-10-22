import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class);

	public static void main(String argv[]) throws Exception {

		String sentence = "";

		String modifiedSentence;

		Scanner in = new Scanner(System.in);

		Socket clientSocket = new Socket("localhost", 6789);

		while (!sentence.equals("q")) {

			sentence = in.nextLine();

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			outToServer.writeBytes(sentence + '\n');

			modifiedSentence = inFromServer.readLine();

			LOGGER.info("FROM SERVER: " + modifiedSentence);
		}

		clientSocket.close();
	}
}