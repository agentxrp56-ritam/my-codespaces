import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Main UI panel containing the target text, input area and live stats.
 */
public class TypingPanel extends JPanel {
    private static final int TEST_DURATION = 60; // seconds

    private final JTextPane targetPane = new JTextPane();
    private final JTextArea inputArea = new JTextArea(4, 60);
    private final JLabel timerLabel = new JLabel("Time: 60");
    private final JLabel wpmLabel = new JLabel("WPM: 0");
    private final JLabel netWpmLabel = new JLabel("Net WPM: 0");
    private final JLabel accuracyLabel = new JLabel("Accuracy: 0%");
    private final JLabel charsLabel = new JLabel("Chars: 0");
    private final JLabel totalWordsLabel = new JLabel("Total words typed: 0");
    private final JLabel correctWordsLabel = new JLabel("Correct words: 0");
    private final JLabel incorrectWordsLabel = new JLabel("Incorrect words: 0");

    private final JButton restartButton = new JButton("Restart (R)");

    private String targetText = "";
    private String[] targetWords = new String[0];

    private javax.swing.Timer countdownTimer;
    private int remainingSeconds = TEST_DURATION;
    private long startTimeMillis = -1;
    private boolean started = false;
    private boolean finished = false;

    public TypingPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#1e1e1e")); // dark background
        setForeground(Color.WHITE);

        // Top: target text
        targetPane.setEditable(false);
        targetPane.setBackground(getBackground());
        targetPane.setForeground(Color.LIGHT_GRAY);
        targetPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        targetPane.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JScrollPane targetScroll = new JScrollPane(targetPane);
        targetScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Center: input area
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        inputArea.setBackground(Color.decode("#121212"));
        inputArea.setForeground(Color.WHITE);
        inputArea.setCaretColor(Color.WHITE);
        inputArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setPreferredSize(new Dimension(800, 120));
        inputScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Right: stats panel
        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.setBackground(getBackground());
        stats.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        addStatLabels(stats);

        // Bottom: controls
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(getBackground());
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(this::restartPressed);
        bottom.add(restartButton, BorderLayout.EAST);

        // Layout
        add(targetScroll, BorderLayout.NORTH);
        add(inputScroll, BorderLayout.CENTER);
        add(stats, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        inputArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { onTextChanged(); }
            @Override
            public void removeUpdate(DocumentEvent e) { onTextChanged(); }
            @Override
            public void changedUpdate(DocumentEvent e) { onTextChanged(); }
        });

        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!started && !finished) {
                    if (!Character.isISOControl(e.getKeyChar())) {
                        startTest();
                    }
                }
                if (finished && (e.getKeyCode() == KeyEvent.VK_R)) {
                    restartTest();
                }
                if (!finished && e.getKeyCode() == KeyEvent.VK_R && (e.isControlDown())) {
                    restartTest();
                }
            }
        });

        // Timer
        countdownTimer = new javax.swing.Timer(1000, e -> {
            remainingSeconds -= 1;
            timerLabel.setText("Time: " + remainingSeconds);
            if (remainingSeconds <= 0) {
                endTest();
            }
        });

        // initial setup
        loadNewTest();
    }

    private void addStatLabels(JPanel stats) {
        timerLabel.setForeground(Color.WHITE);
        wpmLabel.setForeground(Color.WHITE);
        netWpmLabel.setForeground(Color.WHITE);
        accuracyLabel.setForeground(Color.WHITE);
        charsLabel.setForeground(Color.WHITE);
        totalWordsLabel.setForeground(Color.WHITE);
        correctWordsLabel.setForeground(Color.WHITE);
        incorrectWordsLabel.setForeground(Color.WHITE);

        stats.add(timerLabel);
        stats.add(Box.createRigidArea(new Dimension(0,8)));
        stats.add(wpmLabel);
        stats.add(Box.createRigidArea(new Dimension(0,6)));
        stats.add(netWpmLabel);
        stats.add(Box.createRigidArea(new Dimension(0,6)));
        stats.add(accuracyLabel);
        stats.add(Box.createRigidArea(new Dimension(0,6)));
        stats.add(charsLabel);
        stats.add(Box.createRigidArea(new Dimension(0,12)));
        stats.add(totalWordsLabel);
        stats.add(Box.createRigidArea(new Dimension(0,6)));
        stats.add(correctWordsLabel);
        stats.add(Box.createRigidArea(new Dimension(0,6)));
        stats.add(incorrectWordsLabel);
    }

    private void loadNewTest() {
        targetText = TextGenerator.getRandomParagraph();
        targetWords = targetText.split("\\s+");
        inputArea.setText("");
        started = false;
        finished = false;
        remainingSeconds = TEST_DURATION;
        timerLabel.setText("Time: " + remainingSeconds);
        wpmLabel.setText("WPM: 0");
        netWpmLabel.setText("Net WPM: 0");
        accuracyLabel.setText("Accuracy: 0%");
        charsLabel.setText("Chars: 0");
        totalWordsLabel.setText("Total words typed: 0");
        correctWordsLabel.setText("Correct words: 0");
        incorrectWordsLabel.setText("Incorrect words: 0");
        inputArea.setEditable(true);
        inputArea.setEnabled(true);
        updateTargetPane(0); // uncolored
    }

    private void startTest() {
        started = true;
        startTimeMillis = System.currentTimeMillis();
        countdownTimer.start();
    }

    private void endTest() {
        countdownTimer.stop();
        finished = true;
        inputArea.setEditable(false);
        inputArea.setEnabled(false);
        showFinalResults();
    }

    private void restartPressed(ActionEvent e) {
        restartTest();
    }

    private void restartTest() {
        countdownTimer.stop();
        loadNewTest();
    }

    private void onTextChanged() {
        String typed = inputArea.getText();
        int totalCharsTyped = typed.length();

        // calculate per-character correctness
        int correctChars = 0;
        int minLen = Math.min(typed.length(), targetText.length());
        for (int i = 0; i < minLen; i++) {
            if (typed.charAt(i) == targetText.charAt(i)) correctChars++;
        }

        // update colored target
        updateTargetPane(typed.length());

        // calculate words stats
        String[] typedWords = typed.trim().isEmpty() ? new String[0] : typed.trim().split("\\s+");
        int totalWordsTyped = typedWords.length;
        int correctWords = 0;
        int incorrectWords = 0;
        int compareLen = Math.min(typedWords.length, targetWords.length);
        for (int i = 0; i < compareLen; i++) {
            if (typedWords[i].equals(targetWords[i])) correctWords++; else incorrectWords++;
        }
        // words beyond target count as incorrect
        if (typedWords.length > targetWords.length) {
            incorrectWords += typedWords.length - targetWords.length;
        }

        // elapsed time
        double elapsedSeconds = started ? (System.currentTimeMillis() - startTimeMillis) / 1000.0 : 0.0;
        elapsedSeconds = Math.max(elapsedSeconds, 1.0); // avoid tiny values early on

        double grossWPM = TypingStats.computeGrossWPM(totalCharsTyped, elapsedSeconds);
        double penalty = TypingStats.computePenaltyWPM(incorrectWords, elapsedSeconds);
        double netWPM = TypingStats.computeNetWPM(grossWPM, penalty);
        double accuracy = TypingStats.computeAccuracy(correctChars, Math.max(1, totalCharsTyped));

        // update labels
        wpmLabel.setText(String.format("WPM: %.0f", grossWPM));
        netWpmLabel.setText(String.format("Net WPM: %.0f", netWPM));
        accuracyLabel.setText(String.format("Accuracy: %.0f%%", accuracy));
        charsLabel.setText("Chars: " + totalCharsTyped);
        totalWordsLabel.setText("Total words typed: " + totalWordsTyped);
        correctWordsLabel.setText("Correct words: " + correctWords);
        incorrectWordsLabel.setText("Incorrect words: " + incorrectWords);

        if (!started && totalCharsTyped > 0) startTest();
    }

    private void updateTargetPane(int typedLength) {
        StyledDocument doc = targetPane.getStyledDocument();
        doc.removeUndoableEditListener(null);
        targetPane.setEditable(true);
        targetPane.setText("");
        SimpleAttributeSet correctAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(correctAttr, new Color(0x4CAF50)); // green
        StyleConstants.setFontSize(correctAttr, 18);
        SimpleAttributeSet incorrectAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(incorrectAttr, new Color(0xF44336)); // red
        StyleConstants.setFontSize(incorrectAttr, 18);
        SimpleAttributeSet normalAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(normalAttr, Color.LIGHT_GRAY);
        StyleConstants.setFontSize(normalAttr, 18);

        String typed = inputArea.getText();
        int len = targetText.length();
        try {
            for (int i = 0; i < len; i++) {
                if (i < typed.length()) {
                    char tc = typed.charAt(i);
                    char tt = targetText.charAt(i);
                    if (tc == tt) {
                        doc.insertString(doc.getLength(), String.valueOf(tt), correctAttr);
                    } else {
                        doc.insertString(doc.getLength(), String.valueOf(tt), incorrectAttr);
                    }
                } else {
                    doc.insertString(doc.getLength(), String.valueOf(targetText.charAt(i)), normalAttr);
                }
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        targetPane.setEditable(false);
    }

    private void showFinalResults() {
        String typed = inputArea.getText();
        int totalCharsTyped = typed.length();
        int correctChars = 0;
        int minLen = Math.min(typed.length(), targetText.length());
        for (int i = 0; i < minLen; i++) if (typed.charAt(i) == targetText.charAt(i)) correctChars++;

        String[] typedWords = typed.trim().isEmpty() ? new String[0] : typed.trim().split("\\s+");
        int totalWordsTyped = typedWords.length;
        int correctWords = 0;
        int incorrectWords = 0;
        int compareLen = Math.min(typedWords.length, targetWords.length);
        for (int i = 0; i < compareLen; i++) {
            if (typedWords[i].equals(targetWords[i])) correctWords++; else incorrectWords++;
        }
        if (typedWords.length > targetWords.length) incorrectWords += typedWords.length - targetWords.length;

        double elapsedSeconds = TEST_DURATION; // full duration
        double grossWPM = TypingStats.computeGrossWPM(totalCharsTyped, elapsedSeconds);
        double penalty = TypingStats.computePenaltyWPM(incorrectWords, elapsedSeconds);
        double netWPM = TypingStats.computeNetWPM(grossWPM, penalty);
        double accuracy = TypingStats.computeAccuracy(correctChars, Math.max(1, totalCharsTyped));

        // center result panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#1e1e1e"));
        panel.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JLabel title = new JLabel("Test Complete");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0,12)));

        panel.add(createStatLabel(String.format("Gross WPM: %.0f", grossWPM)));
        panel.add(createStatLabel(String.format("Net WPM: %.0f", netWPM)));
        panel.add(createStatLabel(String.format("Accuracy: %.0f%%", accuracy)));
        panel.add(createStatLabel("Total typed words: " + totalWordsTyped));
        panel.add(createStatLabel("Correct words: " + correctWords));
        panel.add(createStatLabel("Incorrect words: " + incorrectWords));
        panel.add(createStatLabel("Characters typed: " + totalCharsTyped));

        panel.add(Box.createRigidArea(new Dimension(0,12)));
        JLabel hint = new JLabel("Press R to restart or click Restart");
        hint.setForeground(Color.LIGHT_GRAY);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(hint);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Result", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JLabel createStatLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
}
