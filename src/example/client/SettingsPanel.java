package example.client;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import kerberos.protocol.client.ClientConfiguration;
import kerberos.protocol.client.SocketAddress;
import kerberos.serialize.SerializeUtilities;

public class SettingsPanel extends JPanel {

    private static final long serialVersionUID = -8642459074075249440L;

    private final ClientFrame clientFrame;

    private JTextField nameField;
    private JTextField serviceNameField;
    private JTextField asAddressField;
    private JTextField asPortField;
    private JTextField tgsAddressField;
    private JTextField tgsPortField;
    private JTextField serviceAddressField;
    private JTextField servicePortField;
    private JTextField passwordField;

    /**
     * Displays input boxes to enter connection-informations.
     * It will validate inputs and notify the ClientFrame if they are acceptable.
     * @param clientFrame will be notified after data is entered
     */
    public SettingsPanel(ClientFrame clientFrame) {
        this.clientFrame = clientFrame;

        this.setBorder(new EmptyBorder(20,20,20,20));

        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        JLabel nameLabel = new JLabel("Name");
        nameField = new JTextField("alice");

        JLabel serviceNameLabel = new JLabel("Service Name");
        serviceNameField = new JTextField("bob");

        JLabel asLabel = new JLabel("AS Address");
        asAddressField = new JTextField("localhost");
        asPortField = new JTextField("7001");

        JLabel tgsLabel = new JLabel("TGS Address");
        tgsAddressField = new JTextField("localhost");
        tgsPortField = new JTextField("7002");

        JLabel serviceLabel = new JLabel("Service Address");
        serviceAddressField = new JTextField("localhost");
        servicePortField = new JTextField("7003");

        JLabel passwordLabel = new JLabel("Shared Secret with AS");
        passwordField = new JTextField("HXeCyA8z1CcON8RSJPqx7g==");

        JButton connectButton = new JButton("Connect!");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                creatConfiguration();
            }
        });

        //      								x  y  w  h  wx wy 

        this.addComponent(nameLabel,        	0, 0, 1, 1, 1, 1);
        this.addComponent(nameField,        	1, 0, 2, 1, 1, 1);

        this.addComponent(serviceNameLabel, 	0, 1, 1, 1, 1, 1);
        this.addComponent(serviceNameField, 	1, 1, 2, 1, 1, 1);

        this.addComponent(asLabel,       		0, 2, 1, 1, 1, 1);
        this.addComponent(asAddressField,   	1, 2, 1, 1, 1, 1);
        this.addComponent(asPortField, 			2, 2, 1, 1, 1, 1);

        this.addComponent(tgsLabel,       		0, 3, 1, 1, 1, 1);
        this.addComponent(tgsAddressField,   	1, 3, 1, 1, 1, 1);
        this.addComponent(tgsPortField, 		2, 3, 1, 1, 1, 1);

        this.addComponent(serviceLabel, 		0, 4, 1, 1, 1, 1);
        this.addComponent(serviceAddressField, 	1, 4, 1, 1, 1, 1);
        this.addComponent(servicePortField, 	2, 4, 1, 1, 1, 1);

        this.addComponent(passwordLabel,        0, 5, 1, 1, 1, 1);
        this.addComponent(passwordField,        1, 5, 2, 1, 1, 1);

        this.addComponent(connectButton,        1, 6, 1, 1, 1, 1);

        this.setVisible(true);
    }

    /**
     * Validates the input, creates a ClientConfiguration with it and notifies
     * the ClientFrame on success.
     */
    private void creatConfiguration(){
        ClientConfiguration config = new ClientConfiguration();

        List<String> errors = new ArrayList<String>();

        // validate input

        String clientName = nameField.getText();
        if(clientName.length() < 1)
            errors.add("You have to enter a name");
        else
            config.setClientName(clientName);

        String serverName = serviceNameField.getText();
        if(serverName.length() < 1)
            errors.add("You have to enter a service name");
        else
            config.setServerName(serverName);

        String asAddress = asAddressField.getText();
        Integer asPort = null;
        try{
            asPort = Integer.parseInt(asPortField.getText());
        }catch(NumberFormatException e){
            errors.add("You have to enter an AS Port");
        }
        if(asAddress.length() < 1)
            errors.add("You have to enter an AS Address");
        else if(asPort != null)
            config.setAsAddress(new SocketAddress(asAddress, asPort));

        String tgsAddress = tgsAddressField.getText();
        Integer tgsPort = null;
        try{
            tgsPort = Integer.parseInt(tgsPortField.getText());
        }catch(NumberFormatException e){
            errors.add("You have to enter an TGS Port");
        }
        if(tgsAddress.length() < 1)
            errors.add("You have to enter an TGS Address");
        else if(tgsPort != null)
            config.setTgsAddress(new SocketAddress(tgsAddress, tgsPort));

        String serviceAddress = serviceAddressField.getText();
        Integer servicePort = null;
        try{
            servicePort = Integer.parseInt(servicePortField.getText());
        }catch(NumberFormatException e){
            errors.add("You have to enter an Service Port");
        }
        if(serviceAddress.length() < 1)
            errors.add("You have to enter an Service Address");
        else if(servicePort != null)
            config.setServiceAddress(new SocketAddress(serviceAddress, servicePort));

        String secret = passwordField.getText();
        if(secret.length() < 1)
            errors.add("You have to enter a shared secret");
        else
            config.setKey(SerializeUtilities.fromBase64(secret));

        // notify ClientFrame if no errors occurred
        if(errors.isEmpty()){
            SettingsPanel.this.clientFrame.onSettingsEntered(config);
        }else{
            String message = "";
            for(String error: errors)
                message += error + "\n";
            JOptionPane.showMessageDialog(this, "Input is not correct:\n" + message);
        }
    }

    /**
     * Helper method to configure components for the GridBagLayout.
     * @param c the component
     * @param x the x position
     * @param y the y position
     * @param width grid-slots for the width
     * @param height grid-slots for the height
     * @param weightx expansion-ration for the x axis
     * @param weighty expansion-ration for the y axis
     */
    private void addComponent(Component c, int x, int y, int width, int height,
            double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        GridBagLayout gbl = (GridBagLayout) this.getLayout();
        gbl.setConstraints(c, gbc);
        this.add(c);
    }

}
