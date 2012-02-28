package kerberos.protocol.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kerberos.serialize.DateSerializer;

@XmlRootElement
public class Authenticator{

    private String clientName;
    private Date time;

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @XmlJavaTypeAdapter(DateSerializer.class) 
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }

}
