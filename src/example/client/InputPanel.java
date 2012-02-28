package example.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel {

    private static final long serialVersionUID = -7667731638565812884L;

    private final OutputStream outputStream;
    private JTextField textField;

    /**
     * Displays a JPanel, that allows the user to write to the OutputStream.
     * @param outputStream an encrypted OutputStream to the Service
     */
    public InputPanel(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.setLayout(new BorderLayout());

        textField = new JTextField("capitalize this");
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputPanel.this.sendMessage();
            }
        });

        JButton submitButton = new JButton("Send");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                InputPanel.this.sendMessage();
            }
        });

        this.add(textField, BorderLayout.CENTER);
        this.add(submitButton, BorderLayout.EAST);
    }

    /**
     * Writes the entered text to the OutputStream.
     */
    private void sendMessage() {
        String message = textField.getText();
        if(message.isEmpty())
            return;

        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
            System.out.println("client sent message: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textField.setText("");
    }

}
