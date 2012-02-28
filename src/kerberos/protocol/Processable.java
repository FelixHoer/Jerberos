package kerberos.protocol;

public interface Processable<Req, Res> {

    /**
     * Processes a request and answers with an appropriate response.
     * @param request the request from the client
     * @return the response from the server
     * @throws Exception if the request was invalid or something went wrong 
     *     while generating the response
     */
    public Res process(Req request) throws Exception;

}
