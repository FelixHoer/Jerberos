package kerberos.protocol.client;

public class SocketAddress {

    private String address;
    private int port;

    public SocketAddress() {}

    public SocketAddress(String address, int port) {
        this.setAddress(address);
        this.setPort(port);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
