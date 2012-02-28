package example.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class MessagePanel extends JPanel {

    private static final long serialVersionUID = 2682464929009517973L;

    private JPanel messageList;

    /**
     * The MessagePanel will show messages from the Service in a List.
     * @param inputStream
     */
    public MessagePanel(InputStream inputStream) {
        this.setBackground(new Color(255,200,200));
        this.setLayout(new BorderLayout());

        messageList = new JPanel(new GridLayout(0,1));
        JScrollPane scrollPane = new JScrollPane(messageList);

        this.addMessage("Connection established!");
        this.addMessage("Enter text in the box below to send it to the server, " 
                + "which will translate it to upper case.");

        this.add(scrollPane, BorderLayout.CENTER);

        // start another Thread, that will block while reading
        ReadThread readThread = new ReadThread(inputStream);
        readThread.start();
    }

    /**
     * Updates the list to display a new message.
     * @param message the text to display
     */
    public void addMessage(String message){
        JLabel label = new JLabel(message);
        messageList.add(label);
        label.setVisible(true);
        this.validate();
    }

    private class ReadThread extends Thread{

        private final InputStream inputStream;

        /**
         * Reads from the InputStream in another Thread, to make sure, that the
         * blocking read-operations won't freeze the user interface.
         * @param inputStream an InputStream, to read the messages
         */
        public ReadThread(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        /**
         * Reads messages from the InputStream and displays them.
         */
        @Override
        public void run(){
            try {
                while(true){
                    byte[] buffer = new byte[100];
                    int red = inputStream.read(buffer);

                    byte[] input = new byte[red];
                    System.arraycopy(buffer, 0, input, 0, red);

                    final String message = new String(input);

                    System.out.println("client received message: " + message);

                    // let the EventThread add the received message
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            MessagePanel.this.addMessage(
                                    "Server responded with: " + message);
                        }
                    });
                }
            } catch (IOException e) {
                MessagePanel.this.addMessage("InputStream closed: " + e);
                e.printStackTrace();
            }
        }

    }

}
