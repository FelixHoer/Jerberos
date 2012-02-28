package kerberos.protocol.tgs;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;

import kerberos.protocol.SocketAcceptor;
import kerberos.protocol.dto.TGSRequest;
import kerberos.protocol.dto.TGSResponse;

public class TicketGrantingServer implements Closeable {

    private final SocketAcceptor<TGSRequest, TGSResponse> socketAcceptor;
    private final ServerSocket serverSocket;

    /**
     * Opens a ServerSocket and creates a SocketAcceptor-Thread, that accepts
     * new clients of the ServerSocket. After a connection is established the
     * TGSProtocol is performed.
     * @param config configuration information for the TGSProtocol and 
     *     ServerSocket-Port.
     * @throws IOException if the ServerSocket can't be opened
     */
    public TicketGrantingServer(TGSConfiguration config) throws IOException {
        serverSocket = new ServerSocket(config.getPort());
        TGSProtocol tgsProtocol = new TGSProtocol(config);
        socketAcceptor = new SocketAcceptor<TGSRequest, TGSResponse>(serverSocket, tgsProtocol);
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
