package app;

import db.DBConnector;
import db.UserDAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnector.connect();
        if (conn != null) {
            System.out.println("Connection successful!");
        }

        UserDAO.insertTestUser();

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