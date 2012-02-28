package kerberos.protocol.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketAuthenticatorRequest {

    private Encrypted ticket;
    private Encrypted authenticator;

    public Encrypted getTicket() {
        return ticket;
    }
    public void setTicket(Encrypted ticket) {
        this.ticket = ticket;
    }

    public Encrypted getAuthenticator() {
        return authenticator;
    }
    public void setAuthenticator(Encrypted authenticator) {
        this.authenticator = authenticator;
    }

}
