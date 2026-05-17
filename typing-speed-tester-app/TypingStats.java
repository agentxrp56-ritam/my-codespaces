/**
 * Typing statistics calculations: gross WPM, net WPM, accuracy.
 */
public class TypingStats {
    /**
     * Gross WPM = (total chars / 5) / minutes
     */
    public static double computeGrossWPM(int totalCharsTyped, double elapsedSeconds) {
        double minutes = Math.max(elapsedSeconds / 60.0, 1.0/60.0); // avoid zero
        return (totalCharsTyped / 5.0) / minutes;
    }

    /**
     * Penalty WPM based on incorrect words per minute.
     */
    public static double computePenaltyWPM(int incorrectWords, double elapsedSeconds) {
        double minutes = Math.max(elapsedSeconds / 60.0, 1.0/60.0);
        return incorrectWords / minutes;
    }

    /**
     * Net WPM = gross - penalty (clamped to zero)
     */
    public static double computeNetWPM(double grossWPM, double penaltyWPM) {
        double net = grossWPM - penaltyWPM;
        return Math.max(0.0, net);
    }

    /**
     * Accuracy percentage based on correct characters vs total typed characters.
     */
    public static double computeAccuracy(int correctChars, int totalCharsTyped) {
        if (totalCharsTyped <= 0) return 0.0;
        return (correctChars / (double) totalCharsTyped) * 100.0;
    }
}
