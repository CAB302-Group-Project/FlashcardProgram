/*package com.example.flashcardai.app;

public class app.Main {
    public static void main(String[] args) {
        System.out.println("Hello from Flashcard AI!");
    }
}*/
/*package com.example.flashcardai.app;

import com.example.flashcardai.controllers.DashboardController;

public class app.Main {
    public static void main(String[] args) {
        System.out.println("Hello from Flashcard AI!");

        DashboardController dashboard = new DashboardController();
        dashboard.displayDecks();
    }
}*/

package com.example.flashcardai.app;

import com.example.flashcardai.controllers.DashboardController;
import com.example.flashcardai.controllers.QuizController;
import com.example.flashcardai.models.Flashcard;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Flashcard AI!");

        DashboardController dashboard = new DashboardController();
        dashboard.displayDecks();

        // Test the quiz system
        QuizController quiz = new QuizController();
        quiz.startQuiz("1"); // Assuming "1" is your Java Basics deck ID

        // Run second quiz session
        //System.out.println("\n=== Starting Second Quiz ===");
        //quiz.startQuiz("1");

        // Show full history
        //System.out.println("\n=== Session History ===");
        quiz.showSessionHistory();

        // Test Flashcard class directly
        /*Flashcard testCard = new Flashcard("test1", "Sample Q", "Sample A");
        System.out.println("Test Question: " + testCard.getQuestion());
        System.out.println("Test Answer: " + testCard.getAnswer());*/
    }
}



