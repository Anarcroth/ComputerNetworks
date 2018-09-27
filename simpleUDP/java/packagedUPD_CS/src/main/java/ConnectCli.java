import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.log4j.Logger;

public class ConnectCli {

	private static final Logger LOGGER = Logger.getLogger(ConnectCli.class);

	public static void main(String args[]) throws Exception {

		UDPClient client = new UDPClient();

//		client.send();

		DatagramSocket clientSocket = new DatagramSocket();

		DatagramPacket sendPacket =
				new DatagramPacket(client.getSendData(), client.getSendData().length, client.getIpAddress(), 9876);
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(client.getReceiveData(), client.getReceiveData().length);
		clientSocket.receive(receivePacket);

		String modifiedSentence = new String(receivePacket.getData());

		LOGGER.info("FROM SERVER:" + modifiedSentence);

		clientSocket.close();
	}
}
