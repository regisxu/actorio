package test;

import java.io.IOException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException {
		int count = 5000;
		Socket[] connections = new Socket[count];
		for (int i = 0; i < count; ++i) {
			connections[i] = new Socket("localhost", 8888);
		}
		
		for (Socket socket : connections) {
			socket.getOutputStream().write("hello".getBytes());
			byte[] bs = new byte[256];
			socket.getInputStream().read(bs);
		}
		for (Socket socket : connections) {
			socket.close();
		}
	}
}
