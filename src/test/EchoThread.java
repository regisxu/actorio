package test;

import java.io.IOException;
import java.net.Socket;

public class EchoThread extends Thread {
	private Socket socket;

	public EchoThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		byte[] bs = new byte[1024];
		try {
			while (true) {
				int count = socket.getInputStream().read(bs, 0, bs.length);
				if (count == -1) {
					break;
				} else if (count == 0) {
					continue;
				}
				socket.getOutputStream().write(bs, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// ignore
			}
			System.out.println("Connection " + socket + " is closed.");
		}
	}

}
