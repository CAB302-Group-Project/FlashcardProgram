package app;

import db.DBConnector;
<<<<<<< Updated upstream
=======
import ai.prompt;
import db.UserDAO;

>>>>>>> Stashed changes
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnector.connect();
        if (conn != null) {
            System.out.println("Connection successful!");
        }
<<<<<<< Updated upstream
=======

        UserDAO.insertTestUser();

        // Debug testing for AI.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter prompt: ");
        String userPrompt = scanner.nextLine();

        prompt.flashcardPrompt(userPrompt);
        // Debug testing for AI.
>>>>>>> Stashed changes
    }
}