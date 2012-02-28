package kerberos.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAcceptor<Req, Res> extends Thread{

    private final ServerSocket serverSocket;
    private final Processable<Req, Res> protocol;
    private boolean open = true;

    /**
     * Accepts all clients for the given ServerSocket and executes each time 
     * the given protocol in a new Thread.
     * @param serverSocket the ServerSocket, to which clients can connect
     * @param protocol the protocol
     */
    public SocketAcceptor(ServerSocket serverSocket, Processable<Req, Res> protocol) {
        this.serverSocket = serverSocket;
        this.protocol = protocol;
        this.start();
    }

    /**
     * After calling close, no new Sockets will be accepted.
     */
    public void close(){
        open = false;
    }

    /**
     * Accepts new Sockets and forwards them to a new ProtocolServer-Thread for 
     * further processing.
     * If the ServerSocked is closed, the currently blocking accept will throw 
     * an exception and thereby awake.
     */
    @Override
    public void run(){
        while(open){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                new ProtocolServer<Req, Res>(socket, protocol);
            } catch (IOException e) {
                if(open)
                    e.printStackTrace();
            }
        }
    }
}
