import javax.swing.*;
import java.awt.*;

/**
 * Main launcher for Typing Speed Tester application.
 */
public class TypingSpeedTester {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system look and feel for a modern feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            JFrame frame = new JFrame("Typing Speed Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 400);
            frame.setMinimumSize(new Dimension(800, 350));

            TypingPanel panel = new TypingPanel();
            frame.getContentPane().add(panel);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
