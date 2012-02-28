package kerberos.stack;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BinaryStreamLayer extends Layer<Object, byte[]>{

    private final Socket socket;

    /**
     * The BinaryStreamLayer is responsible for writing data to the OutputStream
     * and receiving data from the InputStream.
     * @param socket the bidirectional connection 
     */
    public BinaryStreamLayer(final Socket socket) {
        this.socket = socket;
    }

    /**
     * Writes the message received from the Layer above to the OutputStream.
     * @param object the message from above
     * @throws Exception if the message could not be sent
     */
    @Override
    protected void receiveFromUpper(byte[] object) throws Exception {
        System.out.println("BSL received from upper: " + object);
        System.out.println("-> " + socket.getInetAddress().getHostAddress() + ":"+  socket.getPort());
        OutputStream os = socket.getOutputStream();
        os.write(object);
        os.flush();
    }

    /**
     * Blocks until a message from the partner has been received. This message
     * is then forwarded to the Layer above.
     * @throws Exception
     */
    public void receive() throws Exception {
        InputStream is = socket.getInputStream();

        byte[] inBuffer = new byte[64 * 1024]; // 64k

        int red = is.read(inBuffer);

        byte[] inBytes = new byte[red];
        System.arraycopy(inBuffer, 0, inBytes, 0, red);

        this.sendToUpper(inBytes);
    }

    /**
     * Closes the Socket.
     */
    @Override
    public void close() throws IOException {
        this.socket.close();
    }

}
