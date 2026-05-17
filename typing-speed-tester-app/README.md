Typing Speed Tester (Java Swing)

Run in GitHub Codespaces:

```bash
cd typing-speed-tester-app
javac *.java
java TypingSpeedTester
```

Controls:
- Start typing to begin the 60s test.
- Press `R` or click `Restart (R)` to start a new test after completion.

Metrics shown:
- Gross WPM
- Net WPM (penalized by incorrect words)
- Accuracy % (correct characters / total typed characters)
- Total/correct/incorrect words and character count

Notes:
- Java 17+ recommended.
- No external dependencies; uses only Swing and AWT.
