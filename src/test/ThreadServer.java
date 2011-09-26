package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer extends Thread {

	public void run() {
		ServerSocket server;
		try {
			server = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				Socket socket = server.accept();
				EchoThread task = new EchoThread(socket);
				task.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ThreadServer server = new ThreadServer();
		server.start();
	}
}