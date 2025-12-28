package controller;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles and validates user input for the game.
 * Parses commands, validates selections, and provides input utilities.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class InputHandler {
    private Scanner scanner;

    /**
     * Constructor for InputHandler
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructor with custom scanner (for testing)
     * @param scanner Custom scanner instance
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Parse player count from input
     * @param input User input string
     * @return Number of players (3 or 4), or -1 if invalid
     */
    public int parsePlayerCount(String input) {
        try {
            int count = Integer.parseInt(input.trim());
            if (count == 3 || count == 4) {
                return count;
            }
            System.out.println("❌ Invalid player count. Must be 3 or 4.");
            return -1;
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return -1;
        }
    }

    /**
     * Parse game mode from input
     * @param input User input (1-4)
     * @return GameMode enum, or null if invalid
     */
    public GameMode parseGameMode(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            switch (choice) {
                case 1:
                    return GameMode.INDIVIDUAL_SIMPLE;
                case 2:
                    return GameMode.INDIVIDUAL_ADVANCED;
                case 3:
                    return GameMode.TEAM_SIMPLE;
                case 4:
                    return GameMode.TEAM_ADVANCED;
                default:
                    System.out.println("❌ Invalid choice. Please enter 1-4.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return null;
        }
    }

    /**
     * Parse difficulty from input
     * @param input User input (1-3)
     * @return Difficulty enum, or null if invalid
     */
    public Difficulty parseDifficulty(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            switch (choice) {
                case 1:
                    return Difficulty.EASY;
                case 2:
                    return Difficulty.NORMAL;
                case 3:
                    return Difficulty.HARD;
                default:
                    System.out.println("❌ Invalid choice. Please enter 1-3.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return null;
        }
    }

    /**
     * Parse card selection from user input
     * Expected format: "0 1 2" or "0,1,2" (3 numbers)
     * @param input User input string
     * @return Array of 3 indices, or null if invalid
     */
    public int[] parseCardSelection(String input) {
        try {
            // Clean input and split by spaces or commas
            String cleaned = input.trim().replaceAll("[,;]", " ");
            String[] parts = cleaned.split("\\s+");

            if (parts.length != 3) {
                System.out.println("❌ Must select exactly 3 cards");
                return null;
            }

            int[] indices = new int[3];
            for (int i = 0; i < 3; i++) {
                indices[i] = Integer.parseInt(parts[i]);
                if (indices[i] < 0) {
                    System.out.println("❌ Card indices must be non-negative");
                    return null;
                }
            }

            return indices;

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid format. Use: 0 1 2 (three numbers)");
            return null;
        }
    }

    /**
     * Validate player name
     * @param name Player name input
     * @return true if valid
     */
    public boolean isValidPlayerName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Name cannot be empty");
            return false;
        }

        if (name.trim().length() > 20) {
            System.out.println("❌ Name too long (max 20 characters)");
            return false;
        }

        return true;
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
     * @return Valid integer, or -1 if user cancels
     */
    public int readInt(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("exit")) {
                    return -1;
                }

                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

                System.out.println("❌ Please enter a number between " + min + " and " + max);

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Read yes/no input
     * @param prompt The question to ask
     * @return true for yes, false for no
     */
    public boolean readYesNo(String prompt) {
        System.out.print(prompt + " (y/n): ");

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
     * Read player names
     * @param count Number of players
     * @return List of player names
     */
    public List<String> readPlayerNames(int count) {
        List<String> names = new ArrayList<>();

        System.out.println("\nEnter player names:");
        for (int i = 1; i <= count; i++) {
            String name;
            do {
                System.out.print("Player " + i + ": ");
                name = scanner.nextLine().trim();
            } while (!isValidPlayerName(name));

            names.add(name);
        }

        return names;
    }

    /**
     * Validate that input is a number
     * @param input Input string
     * @return true if valid number
     */
    public boolean isNumber(String input) {
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parse menu choice
     * @param input User input
     * @param maxChoice Maximum valid choice
     * @return Choice number, or -1 if invalid
     */
    public int parseMenuChoice(String input, int maxChoice) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 1 && choice <= maxChoice) {
                return choice;
            }
            System.out.println("❌ Invalid choice. Please enter 1-" + maxChoice);
            return -1;
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return -1;
        }
    }

    /**
     * Wait for Enter key press
     */
    public void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Clear console (attempt - may not work on all systems)
     */
    public void clearScreen() {
        // Print multiple newlines to simulate screen clear
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Close the scanner
     */
    public void close() {
        scanner.close();
    }
}
