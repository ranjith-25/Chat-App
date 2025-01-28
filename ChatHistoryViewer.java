
import java.util.List;
import java.util.Scanner;

public class ChatHistoryViewer {
    public static void main(String[] args) {
    	System.out.println("Enter the user name:");
    	try (Scanner in = new Scanner(System.in)) {
			String username = in.nextLine(); // Replace with the username you want to query
			List<String> chatHistory = DatabaseUtil.getChatHistoryByUser(username);

			if (chatHistory.isEmpty()) {
			    System.out.println("No chat history found for user: " + username);
			} else {
			    System.out.println("Chat history for user: " + username);
			    for (String record : chatHistory) {
			        System.out.println(record);
			    }
			}
		}
    }
}
