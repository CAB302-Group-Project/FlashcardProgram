package app;

import db.DBConnector;
import ai.prompt;
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

        // Debug testing for AI. Feel free to remove if bothersome.
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
        }

        // Debug testing for AI. Feel free to remove if bothersome.



    }
}