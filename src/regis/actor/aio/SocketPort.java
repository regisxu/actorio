package regis.actor.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import kilim.Mailbox;
import kilim.Pausable;

public class SocketPort implements Port {

	private AsynchronousSocketChannel channel;

	private Mailbox<Object> mailbox = new Mailbox<>();

	SocketPort(AsynchronousSocketChannel channel) {
		this.channel = channel;
	}

	public SocketPort() throws IOException {
		channel = AsynchronousSocketChannel.open();
	}

	public void connect(String host, int port) throws Pausable, IOException {
		channel.connect(new InetSocketAddress(host, port), mailbox, IoCompletionHandler.CONNECT_HANDLER);
		Object result = mailbox.get();
		IoCompletionHandler.processConnectResult(result);
	}

	@Override
	public int write(ByteBuffer buffer) throws Pausable, IOException {
		channel.write(buffer, mailbox, IoCompletionHandler.RW_HANDLER);
		Object result = mailbox.get();
		return IoCompletionHandler.processRwResult(result);
	}

	@Override
	public void write(byte[] bs, int offset, int length) throws Pausable,
			IOException {
		ByteBuffer buffer = ByteBuffer.wrap(bs, offset, length);
		while (buffer.hasRemaining()) {
			write(buffer);
		}
	}

	@Override
	public int read(ByteBuffer buffer) throws Pausable, IOException {
		channel.read(buffer, mailbox, IoCompletionHandler.RW_HANDLER);
		Object result = mailbox.get();
		return IoCompletionHandler.processRwResult(result);
	}

	@Override
	public int read(byte[] bs, int offset, int length) throws Pausable,
			IOException {
		return read(ByteBuffer.wrap(bs, offset, length));
	}

	@Override
	public void close() throws IOException {
		channel.close();
	}

}
