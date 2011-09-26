package test;

import kilim.Pausable;
import kilim.Task;
import regis.actor.aio.SocketPort;

public class EchoTask extends Task {
	private SocketPort port;

	public EchoTask(SocketPort port) {
		this.port = port;
	}

	@Override
	public void execute() throws Pausable, Exception {
		byte[] bs = new byte[1024];
		try {
			while (true) {
				int count = port.read(bs, 0, bs.length);
				if (count == -1) {
					break;
				} else if (count == 0) {
					continue;
				}
				port.write(bs, 0, count);
			}
		} finally {
			port.close();
			System.out.println("Connection " + port + " is closed.");
		}
	}
}
