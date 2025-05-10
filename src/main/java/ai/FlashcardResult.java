package ai;

import java.util.List;


// This class is purely simplification and splitting of my prompt lists into Q and A.
// No need to touch this at all its all done in prompt but I hope you guys appreciate me simplifying for y'all
// And if no one sees this then its okay i dont even care its like whatever..............

public class FlashcardResult {
    public List<String> questions;
    public List<String> answers;

    public FlashcardResult(List<String> questions, List<String> answers) {
        this.questions = questions;
        this.answers = answers;
    }
}
