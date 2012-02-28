package kerberos.protocol.service;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptThread extends Thread implements Closeable{

    private final ServerSocket serverSocket;
    private final Authenticator authenticator;

    private boolean open = true;

    /**
     * Starts a Thread, that accepts client connections and authenticates them
     * using the Authenticator.
     * @param config configuration information for the Authenticator
     * @throws IOException if the ServerSocket can't be opened
     */
    public AcceptThread(ServiceConfiguration config) throws IOException {
        serverSocket = new ServerSocket(config.getPort());
        authenticator = new Authenticator(config);
        this.start();
    }

    /**
     * Accepts all client connections and starts the authentication process.
     */
    @Override
    public void run(){
        try {
            while(open){
                Socket socket = serverSocket.accept();
                authenticator.authenticate(socket);
            }
        } catch (Exception e) {
            try {
                this.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Closes the ServerSocket and wakes the AcceptThread (to let it die).
     */
    @Override
    public void close() throws IOException {
        open = false;
        authenticator.close();
        serverSocket.close();
        this.interrupt();
    }

    public Authenticator getAuthenticator(){
        return authenticator;
    }
}
