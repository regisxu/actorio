package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class TestAIO {
	public static void main(String[] args) throws IOException {
		new Thread(new Runnable() {
			public void run() {
				try {
					ServerSocket server = new ServerSocket(8888);
					Socket socket = server.accept();
					socket.getOutputStream().write("hello".getBytes());
					System.out.println(socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		final AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
		channel.connect(new InetSocketAddress("localhost", 8888), null,
				new CompletionHandler() {

					@Override
					public void completed(Object result, Object attachment) {
						System.out.println(result);
						ByteBuffer bb = ByteBuffer.allocate(100);
						channel.read(bb, attachment, new CompletionHandler() {

							@Override
							public void completed(Object result,
									Object attachment) {
								System.out.println(result);
								
							}

							@Override
							public void failed(Throwable exc, Object attachment) {
								exc.printStackTrace();
							}
						});
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(new String(bb.array()));
					}

					@Override
					public void failed(Throwable exc, Object attachment) {
						exc.printStackTrace();
					}

				});
		
		System.out.println();

	}
}
