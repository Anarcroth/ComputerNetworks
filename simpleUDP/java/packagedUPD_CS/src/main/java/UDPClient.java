import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

class UDPClient {

	private static final Logger LOGGER = Logger.getLogger(UDPClient.class);

	private byte[] sendData;

	private byte[] receiveData;

	private InetAddress ipAddress;

	public UDPClient() throws UnknownHostException, IOException {

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		sendData = inFromUser.readLine().getBytes();
		receiveData = new byte[1024];

		ipAddress = InetAddress.getByName("localhost");

		LOGGER.info("Initialized client");
	}

	public byte[] getSendData() {

		return sendData;
	}

	public byte[] getReceiveData() {

		return receiveData;
	}

	public InetAddress getIpAddress() {

		return ipAddress;
	}
}
