package view.console;

import java.util.Scanner;

/**
 * Utility class for reading console input.
 * Provides input reading methods with validation.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class InputReader {
    private Scanner scanner;

    /**
     * Constructor for InputReader
     */
    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructor with custom scanner (for testing)
     * @param scanner Custom scanner instance
     */
    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Read a line of input
     * @return User input as string
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Read an integer with validation
     * @param min Minimum valid value
     * @param max Maximum valid value
     * @return Valid integer
     */
    public int readInt(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

                System.out.print("Please enter a number between " + min + " and " + max + ": ");

            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Read yes/no input
     * @return true for yes, false for no
     */
    public boolean readYesNo() {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.print("Please enter 'y' or 'n': ");
        }
    }

    /**
     * Read yes/no input with custom prompt
     * @param prompt The question to ask
     * @return true for yes, false for no
     */
    public boolean readYesNo(String prompt) {
        System.out.print(prompt + " (y/n): ");
        return readYesNo();
    }

    /**
     * Wait for Enter key press
     */
    public void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Wait for Enter with custom message
     * @param message Message to display
     */
    public void waitForEnter(String message) {
        System.out.print("\n" + message);
        scanner.nextLine();
    }

    /**
     * Clear console screen
     * Prints multiple newlines to simulate screen clear
     */
    public void clearScreen() {
        // Print 50 newlines to simulate clear
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Read a non-empty string
     * @param prompt Prompt to display
     * @return Non-empty string
     */
    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("❌ Input cannot be empty!");
        }
    }

    /**
     * Read a string with length limit
     * @param prompt Prompt to display
     * @param maxLength Maximum allowed length
     * @return Valid string
     */
    public String readStringWithLimit(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("❌ Input cannot be empty!");
                continue;
            }

            if (input.length() > maxLength) {
                System.out.println("❌ Input too long! Maximum " + maxLength + " characters.");
                continue;
            }

            return input;
        }
    }

    /**
     * Read an integer (no range limit)
     * @param prompt Prompt to display
     * @return Integer value
     */
    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number! Please try again.");
            }
        }
    }

    /**
     * Read a choice from menu
     * @param maxChoice Maximum valid choice
     * @return Choice number (1 to maxChoice)
     */
    public int readChoice(int maxChoice) {
        return readInt(1, maxChoice);
    }

    /**
     * Read a choice from menu with prompt
     * @param prompt Prompt to display
     * @param maxChoice Maximum valid choice
     * @return Choice number (1 to maxChoice)
     */
    public int readChoice(String prompt, int maxChoice) {
        System.out.print(prompt);
        return readInt(1, maxChoice);
    }

    /**
     * Display a message
     * @param message Message to display
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Display an error message
     * @param message Error message
     */
    public void displayError(String message) {
        System.out.println("❌ ERROR: " + message);
    }

    /**
     * Display a success message
     * @param message Success message
     */
    public void displaySuccess(String message) {
        System.out.println("✅ " + message);
    }

    /**
     * Display a warning message
     * @param message Warning message
     */
    public void displayWarning(String message) {
        System.out.println("⚠️  " + message);
    }

    /**
     * Display an info message
     * @param message Info message
     */
    public void displayInfo(String message) {
        System.out.println("ℹ️  " + message);
    }

    /**
     * Close the scanner
     */
    public void close() {
        scanner.close();
    }
}
