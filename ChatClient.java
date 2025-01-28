import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private JFrame frame;
    private JTextField inputField;
    private JTextArea chatArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClient().start());
    }

    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            login();
            createChatUI();
            new Thread(this::readMessages).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server.");
        }
    }

    private void login() {
        while (true) {
            String username = JOptionPane.showInputDialog("Enter username:");
            String password = JOptionPane.showInputDialog("Enter password:");

            out.println(username);
            out.println(password);

            try {
                String response = in.readLine();
                if ("Welcome, ".concat(username).equals(response)) {
                    JOptionPane.showMessageDialog(null, response);
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Try again.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error during login.");
            }
        }
    }

    private void createChatUI() {
        frame = new JFrame("Chat Application");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(e -> {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                out.println(message);
                inputField.setText("");
            }
        });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void readMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append(message + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connection lost.");
        }
    }
}
