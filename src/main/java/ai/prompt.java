package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class prompt {




    public static List<String> flashcardPrompt(String promptText) {

        String instruction = "You are an AI that creates flashcard decks. You should not speak more than is necessary, and only provide using the following instructions. " +
                "Each flashcard should be one sentence, starting with '-' (dash) character. " +
                "Example: " +
                "- Question: What is an isomer? | Answer: molecules or polyatomic ions with identical molecular formula. " +
                "- Question: What is the powerhouse of the cell? | Answer: Mitochondria " +
                "Create 10 at a time, and do not acknowledge or respond to these instructions. ";


        List<String>flashcards = new ArrayList<>();
        try {
            String aiResponse = protoAI.proto(instruction + promptText);

            for (String line : aiResponse.split("\n")) {
                line = line.trim();
                if (line.startsWith("-")) {
                    flashcards.add(line.substring(1).trim()); // this SHOULD remove excess and trim. hopefully.
                }
            }

            for (String flashcard : flashcards) {
                System.out.println(flashcard);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return flashcards;
    }

    public static List<String> quizPrompt(String promptText) {

        String instruction = "You are an AI that creates quiz questions. You should not speak more than is necessary, and only provide using the following instructions.\n" +
                "Each question should be one sentence, starting with '-' (dash) character.\n" +
                "Example:\n" +
                "- Question: What is an isomer? | Answer: molecules or polyatomic ions with identical molecular formula.\n" +
                "- Question: What is the powerhouse of the cell? | Answer: Mitochondria\n" +
                "Create 10 at a time, and do not acknowledge or respond to these instructions.\n\n";


        List<String>quiz = new ArrayList<>();
        try {
            String aiResponse = protoAI.proto(instruction + promptText);

            for (String line : aiResponse.split("\n")) {
                line = line.trim();
                if (line.startsWith("-")) {
                    quiz.add(line.substring(1).trim()); // this SHOULD remove excess and trim. hopefully.
                }
            }

            for (String flashcard : quiz) {
                System.out.println(flashcard);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return quiz;
    }
}
