package kerberos.protocol.client;

import kerberos.protocol.dto.ASRequest;
import kerberos.protocol.dto.ASResponse;
import kerberos.stack.Stack;

public class ASClientProtocol{

    private final Stack stack;
    private final ClientConfiguration config;

    private ASResponse asResponse;

    /**
     * Sends a request to the Authentication Server and blocks until it receives
     * an an response. 
     * @param config the client configuration containing addresses and keys
     * @throws Exception if the protocol could not be executed correctly
     */
    public ASClientProtocol(ClientConfiguration config) throws Exception {
        this.config = config;
        stack = new Stack(config.getAsAddress());

        this.request();
        this.receive();

        stack.close();
    }

    /**
     * Sends the request, which only contains the names of client and desired
     * service.
     * @throws Exception if the request could not be sent
     */
    private void request() throws Exception{
        System.out.println("-- Requesting AS --");

        ASRequest asRequest = new ASRequest();
        asRequest.setClientName(config.getClientName());
        asRequest.setServiceName(config.getServerName());

        stack.write(asRequest);
    }

    /**
     * Reads and stores the response of the Authetication-Server.
     * @throws Exception if the response could not be red
     */
    private void receive() throws Exception{
        System.out.println("-- Received AS --");

        Object object = stack.read();
        asResponse = (ASResponse) object;
    }

    public ASResponse getResponse() {
        return asResponse;
    }

}
