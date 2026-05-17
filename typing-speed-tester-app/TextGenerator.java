import java.util.Random;

/**
 * Provides random typing paragraphs.
 */
public class TextGenerator {
    private static final String[] PARAGRAPHS = new String[] {
        "The quick brown fox jumps over the lazy dog. This pangram contains every letter of the alphabet and is often used for testing fonts and keyboards.",
        "Typing regularly improves speed and accuracy. Practice for short focused sessions and you will notice steady improvement over time.",
        "Build small projects to apply knowledge. Hands-on practice helps cement concepts and exposes real-world edge cases worth learning from.",
        "Consistent practice beats occasional cramming. Small daily sessions compound into significant progress across weeks and months.",
        "Focus on accuracy before speed. When accuracy improves, speed naturally follows as your fingers learn correct motions and patterns.",
        "Java Swing provides lightweight components for desktop applications. Use layout managers to create responsive, readable interfaces.",
        "Keep UI feedback immediate and helpful. Live stats encourage the user and guide improvement during short practice runs.",
        "Measure your performance with clear metrics: words per minute, accuracy percentage, and error counts for meaningful progress tracking."
    };

    private static final Random RAND = new Random();

    public static String getRandomParagraph() {
        return PARAGRAPHS[RAND.nextInt(PARAGRAPHS.length)];
    }
}
