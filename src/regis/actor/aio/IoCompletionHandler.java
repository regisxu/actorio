package regis.actor.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import kilim.Mailbox;

public class IoCompletionHandler<T> implements
		CompletionHandler<T, Mailbox<Object>> {

	public static final IoCompletionHandler<Integer> RW_HANDLER = new IoCompletionHandler<>();

	public static final IoCompletionHandler<Void> CONNECT_HANDLER = new IoCompletionHandler<>();

	public static final IoCompletionHandler<AsynchronousSocketChannel> ACCEPT_HANDLER = new IoCompletionHandler<>();

	public static enum CONNECTED {
		CONNECTED
	};

	@Override
	public void completed(T result, Mailbox<Object> attachment) {
		if (result == null) {
			attachment.putnb(CONNECTED.CONNECTED);
		} else {
			attachment.putnb(result);
		}
	}

	@Override
	public void failed(Throwable exc, Mailbox<Object> attachment) {
		attachment.putnb(exc);
	}

	public static int processRwResult(Object obj) throws IOException {
		if (obj instanceof Integer) {
			Integer count = (Integer) obj;
			return count.intValue();
		}
		if (obj instanceof IOException) {
			IOException ex = (IOException) obj;
			throw ex;
		}
		if (obj instanceof Throwable) {
			Throwable t = (Throwable) obj;
			throw new RuntimeException(t);
		}
		throw new RuntimeException(
				"Unexpected read/write operation result type: "
						+ obj.getClass());
	}

	public static AsynchronousSocketChannel processAcceptResult(Object result)
			throws IOException {
		if (result instanceof AsynchronousSocketChannel) {
			return (AsynchronousSocketChannel) result;
		}
		if (result instanceof IOException) {
			IOException exc = (IOException) result;
			throw exc;
		}
		if (result instanceof Throwable) {
			Throwable t = (Throwable) result;
			throw new RuntimeException(t);
		}
		throw new RuntimeException("Unexpected accept operation result type: "
				+ result.getClass());
	}

	public static void processConnectResult(Object result) throws IOException {
		if (result instanceof CONNECTED) {
			return;
		}
		if (result instanceof IOException) {
			IOException exc = (IOException) result;
			throw exc;
		}
		if (result instanceof Throwable) {
			Throwable t = (Throwable) result;
			throw new RuntimeException(t);
		}
		throw new RuntimeException("Unexpected connect operation result type: "
				+ result.getClass());
	}
}
