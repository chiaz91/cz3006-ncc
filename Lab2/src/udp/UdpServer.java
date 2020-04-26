package udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;


// Rfc865 Udp Server
public class UdpServer {
	private static int PORT_QOTD = 17; // for Quote of the Day
	private static int SERVER_PORT = PORT_QOTD;

	private static int BUFFER_SIZE_MAX = 65508;
	private static byte[] inBuffer = new byte[BUFFER_SIZE_MAX];
	

	public static void main(String[] argv) throws UnknownHostException {
		// 1. Open UDP socket at well-known port
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(SERVER_PORT);
			System.out.println("[SERVER] RUNNING ON "+InetAddress.getLocalHost()+" AT PORT "+socket.getLocalPort());
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}

		
		while (true) {
			try {
				// 2. Listen for UDP request from client
				DatagramPacket request = new DatagramPacket(inBuffer,inBuffer.length);
				socket.receive(request);
				String msgReceived = new String(request.getData(), 0, request.getLength());
				System.out.println("[CLIENT MESSAGE] " + request.getAddress().getHostAddress()+"/"+request.getPort() + ": " + msgReceived);
				if (msgReceived.equals("system down")) {
					System.out.println("[SERVER] ENDING PROGRAM");
					break;
				}


				// 3. Send UDP reply to client
				InetAddress address = request.getAddress();
				int port = request.getPort();
				String msgReply = "Greeting from Server";
				 byte[] outBuffer = msgReply.getBytes();
				DatagramPacket reply = new DatagramPacket(outBuffer, outBuffer.length, address, port);
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("[SERVER] ENDED");
	}
}
