package kerberos.protocol.as;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;

import kerberos.protocol.SocketAcceptor;
import kerberos.protocol.dto.ASRequest;
import kerberos.protocol.dto.ASResponse;

public class AuthenticationServer implements Closeable {

    private final SocketAcceptor<ASRequest, ASResponse> socketAcceptor;
    private final ServerSocket serverSocket;

    /**
     * Opens a ServerSocket and creates a SocketAcceptor-Thread, that accepts
     * new clients of the ServerSocket. After a connection is established the
     * ASProtocol is performed.
     * @param config configuration information for the ASProtocol and 
     *     ServerSocket-Port.
     * @throws IOException if the ServerSocket can't be opened
     */
    public AuthenticationServer(ASConfiguration config) throws IOException {
        serverSocket = new ServerSocket(config.getPort());
        ASProtocol asProtocol = new ASProtocol(config);
        socketAcceptor = new SocketAcceptor<ASRequest, ASResponse>(serverSocket, asProtocol);
    }

    /**
     * Closes both ServerSocket and SocketAcceptor.
     */
    @Override
    public void close() throws IOException{
        socketAcceptor.close();
        serverSocket.close();
    }

}
