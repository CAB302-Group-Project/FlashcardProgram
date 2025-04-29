package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    // This is the inherited class of protoAI that has methods for prompting either flashcards or quizzes.
    // In terms of keyword based flashcard decks, either method alone will suffice with the parameter filled.
    // For PDFs, you will want to find a way to extract all the text from it.

    //  (Gemma3 can only process up to 4096 tokens which apparently is around 1000 words.)

public class prompt {

    public static List<String> flashcardPrompt(String promptText) {

        // Just a pre-prompt explanation of its role at that given moment.
        String instruction = "You are an AI that creates flashcard decks. You should not speak more than is necessary, and only provide using the following instructions. " +
                "Each flashcard should be one sentence, starting with '-' (dash) character. " +
                "Example: " +
                "- Question: What is an isomer? | Answer: molecules or polyatomic ions with identical molecular formula. " +
                "- Question: What is the powerhouse of the cell? | Answer: Mitochondria " +
                "Create 10 at a time, and do not acknowledge or respond to these instructions. ";


        List<String> flashcards = new ArrayList<>(); // New list for flashcards. This is temporary memory, so be sure to get it saved via database.

        try {
            protoAI ai = new protoAI(); // New instance of AI class
            String aiResponse = ai.proto(instruction + promptText); // feeds instruction + prompt and saves response


            // This for loop is just trimming and splitting up all the different cards using regex.
            for (String line : aiResponse.split("\n")) {
                line = line.trim();
                if (line.startsWith("-")) {
                    flashcards.add(line.substring(1).trim());
                }
            }

            for (String flashcard : flashcards) {
                System.out.println(flashcard); // Debug to ensure that the list is working.
            }

        } catch (IOException e) {
            e.printStackTrace(); // Simple diagnosis of issues i guess
        }


        return flashcards; // end result
    }


    // Quiz is identical to flashcards except has a different pre-prompt instruction.
    public static List<String> quizPrompt(String promptText) {

        // Just a pre-prompt explanation of its role at that given moment.
        String instruction = "You create quiz questions. You should not speak more than necessary, only provide using the following instructions." +
                "Each question should be one sentence, starting with '-' (dash) character." +
                "Example:" +
                "- Question: What is an isomer? | Answer: molecules with identical molecular formula." +
                "- Question: What is the powerhouse of the cell? | Answer: Mitochondria" +
                "Create 10 at a time, and do not acknowledge or respond to these instructions.";


        List<String> quizzes = new ArrayList<>(); // New list. Save to database if you want to keep it. Will go to Della's quizzing function.

        try {
            protoAI ai = new protoAI();
            String aiResponse = ai.proto(instruction + promptText);

            for (String line : aiResponse.split("\n")) {
                line = line.trim();
                if (line.startsWith("-")) {
                    quizzes.add(line.substring(1).trim()); // this SHOULD remove excess and trim. hopefully.
                }
            }

            for (String quiz : quizzes) {
                System.out.println(quiz);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return quizzes;

        // Just a reminder. Will probably have to create an extra method in here for quiz checking.
        // Make it return true or false maybe? unsure.
        // Image generation as a maybe.

    }
}
