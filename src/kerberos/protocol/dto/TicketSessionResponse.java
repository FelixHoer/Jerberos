package kerberos.protocol.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketSessionResponse {

    private Encrypted ticket;
    private Encrypted sessionKey;

    public Encrypted getTicket() {
        return ticket;
    }
    public void setTicket(Encrypted ticket) {
        this.ticket = ticket;
    }

    public Encrypted getSessionKey() {
        return sessionKey;
    }
    public void setSessionKey(Encrypted sessionKey) {
        this.sessionKey = sessionKey;
    }

}
