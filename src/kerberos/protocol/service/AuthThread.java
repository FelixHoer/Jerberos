package kerberos.protocol.service;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import kerberos.socket.KerberosSocket;
import kerberos.stack.Stack;

public class AuthThread extends Thread implements Closeable{

    private final Socket socket;
    private final ServiceConfiguration config;
    private final Authenticator authenticator;

    private Stack stack;

    /**
     * Executes the Service Protocol in a new Thread and notifies the 
     * Authenticator with the result.
     * @param socket the connection to the client
     * @param config containing the service's secret key
     * @param authenticator the Authenticator, holding the authenticated 
     *        Sockets list
     */
    public AuthThread(Socket socket, ServiceConfiguration config, Authenticator authenticator) {
        this.socket = socket;
        this.config = config;
        this.authenticator = authenticator;

        this.start();
    }

    /**
     * Performs the ServiceProtocol, which is the last step of the 
     * Kerberos-Protocol.
     * Will call Authenticator.done if the protocol was successful, or 
     * Authenticator.fail if something went wrong.
     */
    @Override
    public void run(){
        try {

            ServiceProtocol service = new ServiceProtocol(config, socket);

            stack = service.getStack();
            byte[] key = service.getSessionKey();

            KerberosSocket ks = new KerberosSocket(stack, key);
            authenticator.done(this, ks);

        } catch (Exception e) {
            e.printStackTrace();
            authenticator.fail(this, e);
            try {
                this.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Closes the connection an wakes the Thread.
     * @throws Exception if an exception occurred
     */
    @Override
    public void close() throws IOException {
        if(stack != null)
            stack.close();
        else
            socket.close();
        this.interrupt();
    }
}
