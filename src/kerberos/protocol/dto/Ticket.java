package kerberos.protocol.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kerberos.serialize.DateSerializer;


@XmlRootElement
public class Ticket{

    private String clientName;
    private String serviceName;

    private byte[] sessionKey;

    private Date startTime;
    private Date endTime;

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }
    public void setSessionKey(byte[] sessionKey) {
        this.sessionKey = sessionKey;
    }

    @XmlJavaTypeAdapter(DateSerializer.class) 
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @XmlJavaTypeAdapter(DateSerializer.class) 
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
