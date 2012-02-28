package example.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import kerberos.protocol.client.ClientConfiguration;
import kerberos.socket.KerberosSocket;

public class CommunicationPanel extends JPanel{

    private static final long serialVersionUID = 2346609404857413222L;

    /**
     * Connects with a KerverosSocket to the AS/TGS/Service and displays:
     *   a MessagePanel, to read from the Stream and
     *   an InputPanel, to write to the Stream
     * @param config the configuration required by a KerberosSocket
     * @throws Exception will be thrown if the KerberosSocket can't connect
     */
    public CommunicationPanel(ClientConfiguration config) throws Exception {
        this.setBorder(new EmptyBorder(20,20,20,20));
        this.setLayout(new BorderLayout());

        KerberosSocket clientSocket = null;
        OutputStream out = null;
        InputStream in = null;
        try{
            // connect
            clientSocket = new KerberosSocket(config);

            out = clientSocket.getOutputStream();
            in = clientSocket.getInputStream();

            // create in/output panels
            JPanel messagePanel = new MessagePanel(in);
            JPanel inputPanel = new InputPanel(out);

            this.add(messagePanel, BorderLayout.CENTER);
            this.add(inputPanel, BorderLayout.SOUTH);

        }catch(Exception e){
            if(out != null)
                try {
                    out.close();
                } catch (IOException e1) {
                }
            if(in != null)
                try {
                    in.close();
                } catch (IOException e1) {
                }
            if(clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e1) {
                }

            throw e;
        }

        this.setVisible(true);
    }

}
