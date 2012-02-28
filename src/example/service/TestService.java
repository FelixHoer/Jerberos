package example.service;

import java.io.InputStream;
import java.io.OutputStream;

import kerberos.protocol.service.ServiceConfiguration;
import kerberos.socket.KerberosServerSocket;
import kerberos.socket.KerberosSocket;

public class TestService extends Thread {

    private final ServiceConfiguration config;

    /**
     * Starts a KerberosServerSocket, that accepts one client and echoes all
     * messages in upper case.
     * @param config configuration data for the service
     */
    public TestService(ServiceConfiguration config) {
        this.config = config;
        System.out.println("starting service...");
        this.start();
    }

    /**
     * Opens the KerberosServerSocket and accepts one client.
     * Reads all messages and responds with an upper-case version.
     */
    @Override
    public void run() {
        try {
            System.out.println("service started");

            KerberosServerSocket serverSocket = new KerberosServerSocket(config);

            KerberosSocket clientSocket = serverSocket.accept();

            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();

            boolean done = false;
            while (!done) {
                byte[] buffer = new byte[100];
                int red = in.read(buffer);

                byte[] input = new byte[red];
                System.arraycopy(buffer, 0, input, 0, red);

                String inputString = new String(input);
                System.out.println("service received: " + inputString);

                String outputString = inputString.toUpperCase();
                out.write(outputString.getBytes());
                out.flush();
                System.out.println("service sent: " + outputString);
            }

            out.close();
            in.close();

            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
