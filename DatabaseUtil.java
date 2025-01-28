import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:chat_app.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Create users table
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                    """;
            statement.execute(createUsersTable);

            // Create chat history table
            String createChatHistoryTable = """
                    CREATE TABLE IF NOT EXISTS chat_history (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL,
                        message TEXT NOT NULL,
                        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
                    );
                    """;
            statement.execute(createChatHistoryTable);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
    public static void addSampleUsers() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT OR IGNORE INTO users (username, password) VALUES (?, ?)")) {
            
            statement.setString(1, "user1");
            statement.setString(2, "password1");
            statement.executeUpdate();

            statement.setString(1, "user2");
            statement.setString(2, "password2");
            statement.executeUpdate();

            System.out.println("Sample users added.");
        } catch (SQLException e) {
            System.err.println("Error adding sample users: " + e.getMessage());
        }
    }
    public static List<String> getChatHistoryByUser(String username) {
        List<String> chatHistory = new ArrayList<>();
        String query = """
                SELECT username, message, timestamp
                FROM chat_history
                WHERE username = ?
                ORDER BY timestamp ASC;
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getString("timestamp");

                chatHistory.add("[" + timestamp + "] " + user + ": " + message);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving chat history: " + e.getMessage());
        }

        return chatHistory;
    }
}
