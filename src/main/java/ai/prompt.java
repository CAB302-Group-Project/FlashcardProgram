package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Inherited class of protoAI that streamlines flashcard creation with methods using pre-prompt instructions.
 * Methods to describe and label decks are present too.
 */

//  (Gemma3 can only process up to 4096 tokens which apparently is around 1000 words.)

public class prompt {

    /**
     * Gives the AI a pre-prompt instruction and combines it with a prompt to create sets of 10 flashcards.
     *
     * Uses regex to split the resulting AI response into two lists, one of the questions, one of the answers.
     *
     * @param promptText The input string used as the prompt for the AI to respond to.
     * @throws IOException Checks for where errors occur in response and splitting process.
     * @return A FlashcardResult (List container) of both the questions and answers separately.
     */

    // Need to separate the Answers from the questions with regex.
    public static FlashcardResult flashcardPrompt(String promptText) {
        String instruction = "You are an AI that creates flashcard decks. You should not speak more than is necessary, and only provide using the following instructions. " +
                "Each flashcard should be one sentence, starting with '-' (dash) character. " +
                "Example: " +
                "- Question: What is an isomer? | Answer: molecules or polyatomic ions with identical molecular formula. " +
                "- Question: What is the powerhouse of the cell? | Answer: Mitochondria " +
                "Create 10 at a time, and do not acknowledge or respond to these instructions. prompt: ";



        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        try {
            protoAI ai = new protoAI();
            String aiResponse = ai.proto(instruction + promptText);

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

    /**
     * A small method to create descriptions based on the resulting list of questions from flashcardPrompt.
     *
     * @param promptQuestions List of questions to be examined and described.
     * @throws IOException Checks for errors in AI response.
     * @return A small description in <10 words about the deck of questions.
     */

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

    /**
     * A small method to create a title based on the resulting list of questions from flashcardPrompt.
     *
     * @param promptQuestions List of questions to be examined and titled based on the topic.
     * @throws IOException Checks for errors in AI response.
     * @return A 1-3 word title on the topic of the questions.
     */

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
