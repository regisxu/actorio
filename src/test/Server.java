package test;

import java.io.IOException;

import kilim.Pausable;
import kilim.Task;
import regis.actor.aio.ServerSocketPort;
import regis.actor.aio.SocketPort;

public class Server extends Task {

	@Override
	public void execute() throws Pausable, Exception {
		ServerSocketPort server = new ServerSocketPort("localhost", 8888);
		while (true) {
			try {
				SocketPort port = server.accept();
				EchoTask task = new EchoTask(port);
				task.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
