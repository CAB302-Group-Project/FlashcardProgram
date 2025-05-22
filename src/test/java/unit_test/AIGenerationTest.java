package unit_test;

import ai.FlashcardResult;
import ai.prompt;
import ai.protoAI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static db.Deck.DeckDataHolder.questions;
import static org.junit.jupiter.api.Assertions.*;


public class AIGenerationTest {

    String testPrompt = "Chemistry";
    String initialTest = "Hello gemma3";



    @Test
    public void aiFunctionality() throws IOException {

        try {
            protoAI ai = new protoAI();

            String aiResponse = ai.proto(initialTest);

            assertNotNull(aiResponse, "Ai response should not be null");
            assertFalse(aiResponse.trim().isEmpty(), "AI response shouldn't be blank");
        }
        catch(IOException err) {
            fail("AI functionality is down.", err.getCause());

        }

    }

    @Test
    public void flashcardFunctionality() throws IOException {

        FlashcardResult deck = prompt.flashcardPrompt(testPrompt);

        List<String> questions = deck.questions;
        List<String> answers = deck.answers;

        assertNotNull(questions);
        assertNotNull(answers);

        assertFalse(questions.isEmpty(), "Questions field is empty.");
        assertFalse(answers.isEmpty(), "Answers field is empty.");

        String title = prompt.flashcardTitle(questions);
        String desc = prompt.flashcardDesc(questions);

        assertNotNull(title);
        assertNotNull(desc);

        assertFalse(title.isEmpty(), "Title field is empty.");
        assertFalse(desc.isEmpty(), "Description field is empty.");


    }
}
