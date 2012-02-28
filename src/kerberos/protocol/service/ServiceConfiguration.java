package kerberos.protocol.service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceConfiguration {

    private int port;
    private byte[] key;

    public ServiceConfiguration() {}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

}
