package example.client;

import javax.swing.SwingUtilities;

public class Client {

    /**
     * Creates and starts a ClientFrame.
     */
    public Client() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame();
            }
        });
    }

}