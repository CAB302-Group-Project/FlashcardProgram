package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// This is the inherited class of protoAI that has methods for prompting either flashcards or quizzes.
// In terms of keyword based flashcard decks, either method alone will suffice with the parameter filled.
// For PDFs, you will want to find a way to extract all the text from it.

//  (Gemma3 can only process up to 4096 tokens which apparently is around 1000 words.)

public class prompt {


    // Need to separate the Answers from the questions with regex.
    public static FlashcardResult flashcardPrompt(String promptText) {
        String instruction = "You are an AI that creates flashcard decks. Return exactly 10 flashcards in the following format, each on its own line:\n" +
                "- Question: <question>? | Answer: <answer>\n\n" +
                "Do not return any extra text, explanations, or formatting. Only respond with the 10 lines of flashcards in the above format.\n\n" +
                "Prompt: ";


        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        Set<String> seenQuestions = new HashSet<>();


        try {
            protoAI ai = new protoAI();
            String aiResponse = ai.proto(instruction + promptText);
            System.out.println("AI RAW RESPONSE:\n" + aiResponse);

            for (String line : aiResponse.split("\n")) {
                line = line.trim();
                if (line.startsWith("- Question:") && line.contains("| Answer:")) {
                    String[] parts = line.substring(1).split("\\|");
                    String question = parts[0].replace("Question:", "").trim();

                    questions.add(question);
                    String answer = parts[1].replace("Answer:", "").trim();

                    answers.add(answer);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FlashcardResult(questions, answers);
    }



    // Quiz is identical to flashcards except has different pre-prompt instruction.
    public static String flashcardDesc(List<String> promptQuestions) {

        // Just a pre-prompt explanation of its role at that given moment.
        String instruction = "You create descriptions of decks of questions. " +
                "You should not speak more than necessary, and only provide using the following instructions." +
                " The description should be one sentence, no longer than 10 words. " +
                "e.g. - if you have questions like what is 5+5? what is 6*3? You would say: Maths questions on the topics of addition and multiplication." +
                " Do not acknowledge these instructions or respond to them. The string of questions is as follows: ";


        String questionString = "";
        for (int i = 0; i < promptQuestions.size(); i++) {
            questionString = questionString + " " + promptQuestions.get(i); // Just turns the list of questions into one long string for the AI.
        }

        String aiResponse = null; // The string holder for description

        try {
            protoAI ai = new protoAI();
            aiResponse = ai.proto(instruction + questionString); // Creates it


        } catch (IOException e) {
            e.printStackTrace();
        }


        return aiResponse;
    }



    public static String flashcardTitle(List<String> promptQuestions) {

        // Just a pre-prompt explanation of its role at that given moment.
        String instruction = "You create topical Titles for decks of questions. " +
                "You should not speak more than necessary, and only provide using the following instructions." +
                " The title should not exceed more than 3 words. " +
                "e.g. - if you have questions like what is 5+5? what is 6*3? You would say: 'Basic Maths'" +
                " Do not acknowledge these instructions or respond to them. The string of questions is as follows: ";


        String questionString = "";
        for (int i = 0; i < promptQuestions.size(); i++) {
            questionString = questionString + " " + promptQuestions.get(i); // Just turns the list of questions into one long string for the AI.
        }

        String aiResponse = null; // The string holder for description

        try {
            protoAI ai = new protoAI();
            aiResponse = ai.proto(instruction + questionString); // Creates it


        } catch (IOException e) {
            e.printStackTrace();
        }


        return aiResponse;
    }
}