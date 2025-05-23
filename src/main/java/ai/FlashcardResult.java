package ai;

import java.util.List;


/**
 * This class is used as the return method of flashcardPrompt to return two prompt lists.
 * It holds AI created lists to be distributed later.
 */

public class FlashcardResult {
    public List<String> questions;
    public List<String> answers;

    /**
     * Holds two values.
     *
     * @param questions The list of questions created by the AI.
     * @param answers The list of answers created by the AI.
     * No return as it is used as a return method, does not dispense new info itself.
     */

    public FlashcardResult(List<String> questions, List<String> answers) {



        this.questions = questions;
        this.answers = answers;
    }
}
