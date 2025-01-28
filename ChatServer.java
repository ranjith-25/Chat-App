import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static ExecutorService clientPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
    	DatabaseUtil.initializeDatabase();
    	DatabaseUtil.addSampleUsers();
        System.out.println("Chat Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private Connection dbConnection;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.dbConnection = DriverManager.getConnection("jdbc:sqlite:chat_app.db");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Authentication
            out.println("Enter username:");
            String username = in.readLine();
            out.println("Enter password:");
            String password = in.readLine();

            if (authenticate(username, password)) {
                out.println("Welcome, " + username + "!");
                broadcastMessage(username + " has joined the chat.");

                // Chat Handling
                String message;
                while ((message = in.readLine()) != null) {
                    saveMessage(username, message);
                    broadcastMessage(username + ": " + message);
                }
            } else {
                out.println("Authentication failed. Connection closing.");
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        }
    }

    private boolean authenticate(String username, String password) {
        try (PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return false;
        }
    }

    private void saveMessage(String username, String message) {
        try (PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO chat_history (username, message) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving message: " + e.getMessage());
        }
    }

    private void broadcastMessage(String message) {
        // Implement broadcasting logic here (e.g., send to all connected clients).
        System.out.println(message);
    }
}
