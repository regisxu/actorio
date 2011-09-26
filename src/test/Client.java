package test;

import java.io.IOException;
import java.util.Random;

import kilim.Pausable;
import kilim.Task;
import regis.actor.aio.SocketPort;

public class Client extends Task {

	private String host;

	private int port;

	private Random random = new Random(System.currentTimeMillis());

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void execute() throws Pausable, IOException {
		SocketPort socket = new SocketPort();
		socket.connect(host, port);
		try {
			byte[] echo = new byte[1024];
			long time = 0;
			for (int loop = 0; loop < 1000; ++loop) {
				if (loop % 100 == 0) {
					System.out.println("Task " + this + ", loop " + loop + ", average response time " + (time / 100));
					time  = 0;
				}
				byte[] message = generateMessage().getBytes();
				long start = System.currentTimeMillis();
				socket.write(message, 0, message.length);

				int count = 0;
				while (count < message.length) {
					int i = socket.read(echo, count, message.length - count);
					if (i == -1) {
						System.err.println("Connection is closed unexpected!");
						return;
					}
					count += i;
				}
				long end = System.currentTimeMillis();
				time += end - start;
				if (count > message.length) {
					System.err.println("Received inccorect echo message: "
							+ new String(echo, 0, count));
					return;
				}
				for (int i = 0; i < message.length; ++i) {
					if (message[i] != echo[i]) {
						System.err.println("Received inccorect echo message: "
								+ new String(echo, 0, count));
						return;
					}
				}
			}
		} finally {
			socket.close();
		}
	}

	private String generateMessage() {

		int length = random.nextInt(256);
		byte[] message = new byte[length];
		for (int i = 0; i < length; ++i) {
			message[i] = (byte) (random.nextInt(26) + 'A');
		}
		return new String(message, 0, length);
	}

	public static void main(String[] args) throws IOException {
		int count = Integer.parseInt(args[0]);
		Client[] cs = new Client[count];
		for (int i = 0; i < count; ++i) {
			cs[i] = new Client("localhost", 8888);
			cs[i].start();
		}
	}
}
