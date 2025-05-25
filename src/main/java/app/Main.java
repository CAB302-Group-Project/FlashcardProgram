package app;

import ai.FlashcardResult;
import ai.prompt;
import db.DAO.DeckDAO;
import db.DAO.UserDAO;
import db.DBConnector;

import java.sql.Connection;

/**
 * Entry point for the Flashcard application.
 * Initializes database connection and performs test inserts.
 */
public class Main {
    /**
     * Main method executed when the application starts.
     * Establishes a connection and inserts test data.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Connection conn = DBConnector.connect();
        if (conn != null) {
            System.out.println("Connection successful!");
        }

        UserDAO.insertTestUser();

        DeckDAO.insertTestDeckWithCards(1);



       /*// Debug testing for AI. Feel free to remove if bothersome.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter prompt: ");
        String userPrompt = scanner.nextLine();

        List<String> questions = prompt.quizPrompt(userPrompt);


        List<String> answers = new ArrayList<>();
        int count = 1;
        for (String question : questions) {

            System.out.println(count + ". " + question);
            String answer = scanner.nextLine();
            answers.add(answer);
            count += 1;
        }

        System.out.println("Questions finished. Processing grades...");

        List<String> grades = prompt.quizResults(questions, answers);

        for (String grade : grades) {
            System.out.println(grade);
        }*/

        // Debug testing for AI. Feel free to remove if bothersome.


        /*// Debug testing for File Picking. Feel free to remove if bothersome.
        try {
            String pdfText = pdfReader.pdfExtract(); // Triggering file picker

            if (pdfText != null && !pdfText.isEmpty()) {
                System.out.println("Extracted PDF Text:\n");
                System.out.println(pdfText);
            } else {
                System.out.println("No text found or PDF is empty.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Debug testing for File Picking. Feel free to remove if bothersome.*/

    }
}