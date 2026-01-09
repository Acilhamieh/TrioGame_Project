package view.console;

import java.util.Scanner;


public class InputReader {
    private Scanner scanner;

    //Constructor for InputReader

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    //Constructor with custom scanner (for testing)

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    //Read a line of input

    public String readLine() {
        return scanner.nextLine();
    }

    //Read an integer with validation

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

    //Read yes/no input

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


    public boolean readYesNo(String prompt) {
        System.out.print(prompt + " (y/n): ");
        return readYesNo();
    }

    //Wait for Enter key press

    public void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Wait for Enter with custom message

    public void waitForEnter(String message) {
        System.out.print("\n" + message);
        scanner.nextLine();
    }

    // Clear console screen

    public void clearScreen() {
        // Print 50 newlines to simulate clear
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    //Read a non-empty string

    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(" Input cannot be empty!");
        }
    }

    //Read a string with length limit

    public String readStringWithLimit(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(" Input cannot be empty!");
                continue;
            }

            if (input.length() > maxLength) {
                System.out.println(" Input too long! Maximum " + maxLength + " characters.");
                continue;
            }

            return input;
        }
    }

    // Read an integer (no range limit)

    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println(" Invalid number! Please try again.");
            }
        }
    }

    //Read a choice from menu

    public int readChoice(int maxChoice) {
        return readInt(1, maxChoice);
    }

    //Read a choice from menu with prompt

    public int readChoice(String prompt, int maxChoice) {
        System.out.print(prompt);
        return readInt(1, maxChoice);
    }

    //Display a message

    public void displayMessage(String message) {
        System.out.println(message);
    }

    //Display an error message

    public void displayError(String message) {
        System.out.println("❌ ERROR: " + message);
    }

    //Display a success message

    public void displaySuccess(String message) {
        System.out.println("✅ " + message);
    }

    //Display a warning message

    public void displayWarning(String message) {
        System.out.println("⚠️  " + message);
    }

    // Display an info message

    public void displayInfo(String message) {
        System.out.println("ℹ️  " + message);
    }


    public void close() {
        scanner.close();
    }
}
