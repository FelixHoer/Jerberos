package kerberos.socket;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingDeque;

import kerberos.protocol.dto.Encrypted;
import kerberos.stack.Stack;

public class KerberosInputStream extends InputStream {

    private final LinkedBlockingDeque<byte[]> buffers = new LinkedBlockingDeque<byte[]>();
    private final Stack stack;
    private final byte[] key;

    /**
     * Creates an InputStream, that reads encrypted messages from a previously
     * authenticated client.
     * @param stack the connection to the authenticated client
     * @param key the session key negotiated by the Kerberos-Protocol
     */
    public KerberosInputStream(Stack stack, byte[] key) {
        this.stack = stack;
        this.key = key;
    }

    /**
     * Reads all available messages from the stack-buffer into the local buffer 
     * without blocking.
     * @throws Exception if messages can't be red
     */
    private void readAvailable() throws Exception {
        for(Object o: stack.readAvailable()){
            Encrypted input = (Encrypted) o;
            this.buffers.add(input.decrypt(key));
        }
    }

    /**
     * Read one message from the stack-buffer into the local buffer or block 
     * until one is available.
     * @throws Exception if message can't be red
     */
    private void readFromStack() throws Exception {
        Encrypted input = (Encrypted) stack.read();
        this.buffers.add(input.decrypt(key));
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public int read() throws IOException {
        byte[] oneByte = new byte[1];
        this.read(oneByte);
        return (int) oneByte[0];
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public int read(byte[] output, int offset, int length) throws IOException {
        try {
            int red = 0;

            this.readAvailable();

            // if still empty -> blocking read
            if (this.buffers.isEmpty()) {
                this.readFromStack();
            }

            // drain buffers until buffers empty or "len" red
            // reinsert remaining bytes
            while (!buffers.isEmpty() && red < length) {
                byte[] buffer = buffers.poll();

                // copy buffer to output
                int diff = length - red;
                int take = buffer.length < diff ? buffer.length : diff;
                System.arraycopy(buffer, 0, output, offset + red, take);

                // reinsert remaining bytes if whole buffer was too much
                if (take < buffer.length) {
                    int rem = buffer.length - take;
                    byte[] remaining = new byte[rem];
                    System.arraycopy(buffer, take, remaining, 0, rem);
                    buffers.addFirst(remaining);
                }

                red += take;
            }

            return red;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public int available() {
        int length = 0;
        for (byte[] buffer : this.buffers)
            length += buffer.length;
        return length;
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public void mark(int readlimit) {
        throw new UnsupportedOperationException("Cant mark on KerberosInputStream");
    }

    /**
     * @see java.io.InputStream
     */
    @Override
    public void reset() {
        throw new UnsupportedOperationException("Cant reset on KerberosInputStream");
    }

}
