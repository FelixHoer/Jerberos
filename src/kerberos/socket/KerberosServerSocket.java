package kerberos.socket;

import java.io.Closeable;
import java.io.IOException;

import kerberos.protocol.service.AcceptThread;
import kerberos.protocol.service.ServiceConfiguration;

public class KerberosServerSocket implements Closeable{

    private final AcceptThread acceptThread;

    /**
     * Starts a Thread, that accepts client connections and authenticates them.
     * @param config configuration information for the service
     * @throws IOException if the ServerSocket can't be opened
     */
    public KerberosServerSocket(ServiceConfiguration config) throws IOException {
        acceptThread = new AcceptThread(config);
    }

    /**
     * Blocks until a client is authenticated and then returns the connection. 
     * @return a connection to a authenticated client
     * @throws Exception if communication or protocol execution failed
     */
    public KerberosSocket accept() throws Exception{
        return acceptThread.getAuthenticator().getAuthenticatedSocket();
    }

    /**
     * Closes the ServerSocket and AcceptThread.
     */
    @Override
    public void close() throws IOException {
        acceptThread.close();
    }

}
