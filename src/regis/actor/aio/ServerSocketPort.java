package regis.actor.aio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import kilim.Mailbox;
import kilim.Pausable;

public class ServerSocketPort implements Closeable {

	AsynchronousServerSocketChannel channel;

	Mailbox<Object> mailbox = new Mailbox<>();

	public ServerSocketPort(String host, int port) throws IOException {
		this(new InetSocketAddress(host, port));
	}

	public ServerSocketPort(SocketAddress address) throws IOException {
		channel = AsynchronousServerSocketChannel.open();
		channel.bind(address);
	}

	public SocketPort accept() throws IOException, Pausable {
		channel.accept(mailbox, IoCompletionHandler.ACCEPT_HANDLER);
		Object result = mailbox.get();
		return new SocketPort(IoCompletionHandler.processAcceptResult(result));
	}

	@Override
	public void close() throws IOException {
		channel.close();
	}
}
