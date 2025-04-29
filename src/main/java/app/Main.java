package app;

import db.DBConnector;
import ai.prompt;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnector.connect();
        if (conn != null) {
            System.out.println("Connection successful!");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter prompt: "); // Where prompt goes
        String userPrompt = scanner.nextLine();

        prompt.flashcardPrompt(userPrompt);


    }
}