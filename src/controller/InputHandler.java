package controller;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class InputHandler {
    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }


    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }


    public int parsePlayerCount(String input) {
        try {
            int count = Integer.parseInt(input.trim());
            if (count >= 2 && count <= 6) {
                return count;
            }
            System.out.println(" Invalid player count. Must be 2-6 players.");
            return -1;
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input. Please enter a number.");
            return -1;
        }
    }


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
                    System.out.println(" Invalid choice. Please enter 1-4.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input. Please enter a number.");
            return null;
        }
    }

    public Difficulty parseDifficulty(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            switch (choice) {
                case 1:
                    return Difficulty.SIMPLE;
                case 2:
                    return Difficulty.SIMPLE;
                case 3:
                    return Difficulty.ADVANCED;
                default:
                    System.out.println(" Invalid choice. Please enter 1-3.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input. Please enter a number.");
            return null;
        }
    }


    public int[] parseCardSelection(String input) {
        try {
            // Clean input and split by spaces or commas
            String cleaned = input.trim().replaceAll("[,;]", " ");
            String[] parts = cleaned.split("\\s+");

            if (parts.length != 3) {
                System.out.println(" Must select exactly 3 cards");
                return null;
            }

            int[] indices = new int[3];
            for (int i = 0; i < 3; i++) {
                indices[i] = Integer.parseInt(parts[i]);
                if (indices[i] < 0) {
                    System.out.println(" Card indices must be non-negative");
                    return null;
                }
            }

            return indices;

        } catch (NumberFormatException e) {
            System.out.println(" Invalid format. Use: 0 1 2 (three numbers)");
            return null;
        }
    }


    public boolean isValidPlayerName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(" Name cannot be empty");
            return false;
        }

        if (name.trim().length() > 20) {
            System.out.println(" Name too long (max 20 characters)");
            return false;
        }

        return true;
    }


    public String readLine() {
        return scanner.nextLine();
    }


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

                System.out.println(" Please enter a number between " + min + " and " + max);

            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter a number.");
            }
        }
    }


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


    public boolean isNumber(String input) {
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int parseMenuChoice(String input, int maxChoice) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 1 && choice <= maxChoice) {
                return choice;
            }
            System.out.println(" Invalid choice. Please enter 1-" + maxChoice);
            return -1;
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input. Please enter a number.");
            return -1;
        }
    }


    public void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }


    public void clearScreen() {
        // Print multiple newlines to simulate screen clear
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }


    public void close() {
        scanner.close();
    }
}
