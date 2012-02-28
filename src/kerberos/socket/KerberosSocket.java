package kerberos.socket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kerberos.protocol.client.ASClientProtocol;
import kerberos.protocol.client.ClientConfiguration;
import kerberos.protocol.client.ServiceClientProtocol;
import kerberos.protocol.client.TGSClientProtocol;
import kerberos.stack.Stack;

public class KerberosSocket implements Closeable {

    private final Stack stack;
    private final KerberosInputStream kis;
    private final KerberosOutputStream kos;

    /**
     * Creates a KerberosSocket for encrypted and mutually authenticated 
     * communication with a server.
     * To achieve this, the client has to run through the Kerberos-Protocol.
     * @param config information needed to execute the Kerberos-Protocol
     * @throws Exception if the exception could not be execute correctly
     */
    public KerberosSocket(ClientConfiguration config) throws Exception {

        // run protocol steps
        ASClientProtocol asClient = new ASClientProtocol(config);
        TGSClientProtocol tgsClient = new TGSClientProtocol(config, asClient);
        ServiceClientProtocol serviceClient = new ServiceClientProtocol(config, tgsClient);

        stack = serviceClient.getStack();
        byte[] key = serviceClient.getSessionKey();

        kis = new KerberosInputStream(stack, key);
        kos = new KerberosOutputStream(stack, key);

    }

    /**
     * Creates a KerberosSocket for encrypted communication with a previously 
     * authenticated partner.
     * @param stack a Stack responsible for sending and receiving
     * @param key the session key for client and service negotiated by the
     *            Kerberos-Protocol
     */
    public KerberosSocket(Stack stack, byte[] key) {
        this.stack = stack;

        kis = new KerberosInputStream(stack, key);
        kos = new KerberosOutputStream(stack, key);
    }

    public InputStream getInputStream() throws IOException{
        return this.kis;
    }

    public OutputStream getOutputStream() throws IOException{
        return this.kos;
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() throws IOException {
        stack.close();
    }

}
