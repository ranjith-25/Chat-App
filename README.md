###Real-Time Chat Application
A real-time chat application built with Java using sockets for communication. The application supports text messaging, user authentication, and persistent chat history stored in SQLite.

Features
Real-Time Communication: Users can send and receive messages instantly.
Authentication: Users must log in with a username and password.
Persistent Chat History: All messages are stored in an SQLite database.
Multi-User Support: Multiple clients can connect and chat simultaneously.

Technologies Used
Java: Core programming language.
Java Sockets: For real-time communication between clients and server.
SQLite: To store user credentials and chat history.

Database Schema
The application uses the following tables:

Users Table:

sql
Copy
Edit
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

Chat History Table:

sql
Copy
Edit
CREATE TABLE chat_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    message TEXT NOT NULL,
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP
);

Setup Instructions
1. Prerequisites
Java Development Kit (JDK) 8 or later.
Eclipse IDE or any Java IDE.
SQLite JDBC driver (sqlite-jdbc-<version>.jar).

2. Clone the Repository
bash
Copy
Edit
git clone https://github.com/your-username/chat-app.git
cd chat-app

3. Add SQLite JDBC Driver
Download the driver from SQLite JDBC.
Add it to your IDE's build path:
Right-click your project in Eclipse > Build Path > Add External Archives.
Select the downloaded JAR file.

4. Database Initialization
The database is automatically initialized when you run the server for the first time. The following default users are created:

Username: user1 | Password: password1
Username: user2 | Password: password2
Running the Application

1. Start the Server
Open ChatServer.java in your IDE.
Run the file (Right-Click > Run As > Java Application).
The server will start on port 12345.

2. Start a Client
Open ChatClient.java in your IDE.
Run the file (Right-Click > Run As > Java Application).
Enter the username and password to join the chat.
Open multiple instances to simulate multiple users.
Chat History Viewer
To view the chat history for a specific user:

Open ChatHistoryViewer.java.
Modify the username in the code:
java
Copy
Edit
String username = "user1";
Run the file to display the chat history for the specified user.
Sample Commands

Start Chat:

Enter your username and password when prompted.
Start sending messages after successful login.

Broadcast Messages:

All messages sent are visible to every user currently connected to the server.
File Structure
bash
Copy
Edit
src/
│
├── com.chatapp/
│   ├── ChatServer.java          # Server-side logic
│   ├── ChatClient.java          # Client-side logic
│   ├── DatabaseUtil.java        # SQLite setup and database operations
│   ├── ChatHistoryViewer.java   # Utility to view chat history
│
├── resources/
│   ├── sqlite-jdbc-<version>.jar # SQLite JDBC Driver






