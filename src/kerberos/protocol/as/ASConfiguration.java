package kerberos.protocol.as;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "port", "tgsKey", "clientKeys" })
public class ASConfiguration {

    private int port;
    private byte[] tgsKey;
    private Map<String, byte[]> clientKeys = new HashMap<String, byte[]>();

    public ASConfiguration() {}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getTgsKey() {
        return tgsKey;
    }

    public void setTgsKey(byte[] tgsKey) {
        this.tgsKey = tgsKey;
    }

    public Map<String, byte[]> getClientKeys() {
        return clientKeys;
    }

    public void setClientKeys(Map<String, byte[]> clientKeys) {
        this.clientKeys = clientKeys;
    }

}
