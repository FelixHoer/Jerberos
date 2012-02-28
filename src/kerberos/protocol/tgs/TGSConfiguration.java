package kerberos.protocol.tgs;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "port", "tgsKey", "serverKeys" })
public class TGSConfiguration {

    private int port;
    private byte[] tgsKey;
    private Map<String, byte[]> serverKeys = new HashMap<String, byte[]>();

    public TGSConfiguration() {}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, byte[]> getServerKeys() {
        return serverKeys;
    }

    public void setServerKeys(Map<String, byte[]> serverKeys) {
        this.serverKeys = serverKeys;
    }

    public byte[] getTgsKey() {
        return tgsKey;
    }

    public void setTgsKey(byte[] tgsKey) {
        this.tgsKey = tgsKey;
    }

}
