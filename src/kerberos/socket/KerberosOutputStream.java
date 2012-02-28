package kerberos.socket;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import kerberos.protocol.dto.Encrypted;
import kerberos.stack.Stack;

public class KerberosOutputStream extends OutputStream {

    private final LinkedList<byte[]> buffers = new LinkedList<byte[]>();
    private final Stack stack;
    private final byte[] key;

    /**
     * Creates an OutputStream, that communicates via the Stack with a client 
     * and encrypts all data to send.
     * @param stack the connection to the authenticated client
     * @param key the session key negotiated by the Kerberos-Protocol
     */
    public KerberosOutputStream(Stack stack, byte[] key) {
        this.stack = stack;
        this.key = key;
    }

    @Override
    /**
     * @see java.io.OutputStream#write
     */
    public void write(int output) throws IOException {
        byte[] bytes = { (byte) output };
        this.buffers.add(bytes);
    }

    @Override
    /**
     * @see java.io.OutputStream#write
     */
    public void write(byte[] buffer) throws IOException {
        this.write(buffer, 0, buffer.length);
    }

    @Override
    /**
     * @see java.io.OutputStream#write
     */
    public void write(byte[] buffer, int offset, int length) throws IOException {
        if (offset + length > buffer.length)
            throw new IOException("Cant read that much from buffer");

        byte[] bytes = new byte[length];
        System.arraycopy(buffer, offset, bytes, 0, length);
        this.buffers.add(bytes);
    }

    @Override
    /**
     * @see java.io.OutputStream#flush
     */
    public void flush() throws IOException {
        int length = 0;
        for (byte[] buffer : this.buffers)
            length += buffer.length;

        byte[] bytes = new byte[length];
        int offset = 0;
        for (byte[] buffer : this.buffers) {
            System.arraycopy(buffer, 0, bytes, offset, buffer.length);
            offset += buffer.length;
        }

        this.buffers.clear();

        try {
            Encrypted encrypted = new Encrypted(bytes, key);
            stack.write(encrypted);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
