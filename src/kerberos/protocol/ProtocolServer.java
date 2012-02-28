package kerberos.protocol;

import java.io.IOException;
import java.net.Socket;

import kerberos.stack.Stack;

public class ProtocolServer<Req, Res> extends Thread{

    private final Socket socket;
    private final Processable<Req, Res> protocol;

    /**
     * Starts a Thread to process an incoming request and to produce an 
     * appropriate response according to a given protocol.
     * @param socket a connection to the client
     * @param protocol a Processable, that defines how to respond
     */
    public ProtocolServer(Socket socket, Processable<Req, Res> protocol) {
        this.socket = socket;
        this.protocol = protocol;
        this.start();
    }

    /**
     * Responds to a received message according to the protocol.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run(){
        Stack stack = null;
        try {
            stack = new Stack(socket);

            Req request = (Req) stack.read();
            Res response = protocol.process(request);
            stack.write(response);

            stack.close();
        } catch (Exception e) {
            e.printStackTrace();
            if(stack != null)
                try {
                    stack.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }

}
