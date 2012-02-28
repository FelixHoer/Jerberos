package kerberos.protocol.client;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlType(propOrder = { 
        "clientName", "serverName", "asAddress", "tgsAddress", "serviceAddress" 
})
public class ClientConfiguration {

    private String clientName;
    private String serverName;

    private byte[] key;

    private SocketAddress asAddress;
    private SocketAddress tgsAddress;
    private SocketAddress serviceAddress;

    public ClientConfiguration() {}

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @XmlTransient 
    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public SocketAddress getAsAddress() {
        return asAddress;
    }

    public void setAsAddress(SocketAddress asAddress) {
        this.asAddress = asAddress;
    }

    public SocketAddress getTgsAddress() {
        return tgsAddress;
    }

    public void setTgsAddress(SocketAddress tgsAddress) {
        this.tgsAddress = tgsAddress;
    }

    public SocketAddress getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(SocketAddress serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

}
