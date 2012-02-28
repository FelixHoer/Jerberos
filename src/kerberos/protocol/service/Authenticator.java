package kerberos.protocol.service;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import kerberos.socket.KerberosSocket;
import kerberos.stack.ReadQ;

public class Authenticator implements Closeable{

    private final ServiceConfiguration config;
    private final ReadQ authSocketsQ;

    private final Object authThreadsLock = new Object();
    private final List<AuthThread> authThreads = new ArrayList<AuthThread>();

    /**
     * Starts authentication for each new Socket in a new Thread and maintains 
     * a list of authenticated sockets.
     * @param config containing the service's secret key
     */
    public Authenticator(ServiceConfiguration config) {
        this.config = config;
        this.authSocketsQ = new ReadQ();
    }

    /**
     * Starts authentication for a new Socket in a new Thread and keeps track
     * of this Thread.
     * @param socket the connection to the client
     */
    public void authenticate(Socket socket){
        synchronized(authThreadsLock){
            authThreads.add(new AuthThread(socket, config, this));
        }
    }

    /**
     * Removes the AuthThread from the pending authentication list and adds the 
     * socket to the authenticated sockets.
     * Will be called after the client is successfully authenticated.
     * @param thread the AuthThread responsible for the authentication
     * @param socket the finally authenticated KerberosSocket
     */
    public void done(AuthThread thread, KerberosSocket socket){
        synchronized(authThreadsLock){
            authThreads.remove(thread);
        }
        authSocketsQ.add(socket);
    }

    /**
     * Removes the AuthThread from the pending authentication list and places
     * an exception in the authenticated sockets list.
     * Will be called after the client failed the authentication.
     * @param thread the AuthThread responsible for the authentication
     * @param e the exception that occurred
     */
    public void fail(AuthThread thread, Exception e){
        synchronized(authThreadsLock){
            authThreads.remove(thread);
        }
        authSocketsQ.add(e);
    }

    /**
     * Takes an authenticated KerberosSocket off the authenticated sockets list.
     * @return an authenticated KerberosSocket
     * @throws Exception if an exception occurred
     */
    public KerberosSocket getAuthenticatedSocket() throws Exception {
        return (KerberosSocket) authSocketsQ.take();
    }

    /**
     * Closes all AuthThread, that are still in the authentication process.
     * @throws Exception if an exception occurred
     */
    @Override
    public void close() throws IOException {
        synchronized(authThreadsLock){
            for(AuthThread authThread: authThreads)
                authThread.close();
            authThreads.clear();
        }
        authSocketsQ.terminate(new Exception("closed"));
    }

}
