package regis.actor.aio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import kilim.Pausable;

public interface Port extends Closeable {
	public int write(ByteBuffer buffer) throws Pausable, IOException;

	public void write(byte[] bs, int offset, int length) throws Pausable,
			IOException;

	public int read(ByteBuffer buffer) throws Pausable, IOException;

	public int read(byte[] bs, int offset, int length) throws Pausable,
			IOException;
}
