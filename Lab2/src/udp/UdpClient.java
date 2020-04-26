package udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpClient {
	private static int SERVER_PORT = 17; // QOTD
	private static int BUFFER_SIZE_MAX = 65508;
	private static byte[] inBuffer = new byte[BUFFER_SIZE_MAX];

	private static String SERVER_HOSTNAME = "";
	private static String MESSAGE_DEFAULT = "Name, LabGroup, ClientIP";

	public static void main(String[] argv) {
		// 1. Open UDP socket
		DatagramSocket socket;
		try {
			// random assign available socket
			socket = new DatagramSocket();
			System.out.println("[CLIENT] RUNNING ON "+InetAddress.getLocalHost()+" AT PORT "+socket.getLocalPort());
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}

		try {
			// input sending message
			String msgSending = MESSAGE_DEFAULT;
			if (msgSending == null || msgSending.length() == 0) {
				Scanner sc = new Scanner(System.in);
				System.out.print("Enter a message: ");
				msgSending = sc.nextLine();
			}


			// 2. Send UDP request to server
			byte[] outBuffer = msgSending.getBytes();
			InetAddress address = InetAddress.getByName(SERVER_HOSTNAME);
			if (SERVER_HOSTNAME == null || SERVER_HOSTNAME.length() == 0) {
				address = InetAddress.getLocalHost();
			}
			DatagramPacket packet = new DatagramPacket(outBuffer, outBuffer.length, address, SERVER_PORT);
			socket.send(packet);

			// 3. Receive UDP reply from server
			packet = new DatagramPacket(inBuffer, inBuffer.length);
			socket.receive(packet);
			String msgReceived = new String(packet.getData(), 0, packet.getLength());
			System.out.println("[CLIENT] "+packet.getAddress().getHostAddress() + ": " + msgReceived);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[CLIENT] END");
	}
}
