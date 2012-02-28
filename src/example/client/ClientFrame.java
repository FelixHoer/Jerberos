package example.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kerberos.protocol.client.ClientConfiguration;

public class ClientFrame extends JFrame {

    private static final long serialVersionUID = 2796573099283491860L;

    private JPanel content = null;

    /**
     * Initializes the Frame and displays the SettingsPanel.
     */
    public ClientFrame() {
        this.setTitle("Kerberos Client for Capitalization Service");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - this.getSize().width) / 2;
        int y = (d.height - this.getSize().height) / 2;
        this.setLocation(x, y);

        this.setLayout(new BorderLayout());

        this.onStarted();

        this.setVisible(true);
    }

    /**
     * Displays the SettingsPanel.
     */
    public void onStarted(){
        JPanel settingsPanel = new SettingsPanel(this);
        this.setContent(settingsPanel);
    }

    /**
     * Displays the CommunicationPanel, which tries to connect to the previously
     * entered Servers.
     * @param config 
     */
    public void onSettingsEntered(ClientConfiguration config){
        try{
            CommunicationPanel communicationPanel = new CommunicationPanel(config);
            this.setContent(communicationPanel);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Could not connect to server:\n" + e);
        }
    }

    /**
     * Replaces the old content with a new JPanel and displays it.
     * @param next the new content
     */
    private void setContent(JPanel next) {
        if(content != null){
            content.setVisible(false);
            this.remove(content);
        }

        content = next;
        this.add(next, BorderLayout.CENTER);
        content.setVisible(true);
    }

}
