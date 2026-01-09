package view.console;

import enums.*;
import java.util.ArrayList;
import java.util.List;


public class MenuView {
    private InputReader inputReader;


    public MenuView() {
        this.inputReader = new InputReader();
    }


    public int showMainMenu() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("                   MAIN MENU");
        System.out.println("═".repeat(50));
        System.out.println();
        System.out.println("  1.  New Game");
        System.out.println("  2.  Rules");
        System.out.println("  3. ️  About");
        System.out.println("  4.  Exit");
        System.out.println();
        System.out.print("Enter your choice (1-4): ");

        String input = inputReader.readLine();

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    //Get number of players from user

    public int getPlayerCount() {
        System.out.println("\n How many players?");
        System.out.println("  3-6 players");
        System.out.print("\nNumber of players: ");

        String input = inputReader.readLine().trim();

        if (input.equalsIgnoreCase("cancel")) {
            return -1;
        }

        try {
            int count = Integer.parseInt(input);
            if (count >= 2 && count <= 6) {
                return count;
            } else {
                System.out.println(" Must be 3-6 players!");
                return getPlayerCount(); // Retry
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input!");
            return getPlayerCount(); // Retry
        }
    }


    public GameMode getGameMode() {
        System.out.println("\n Select Game Mode:");
        System.out.println();
        System.out.println("  1. 👤 Individual - Simple Mode");
        System.out.println("     └─ 3 matching cards = 2 ECTS");
        System.out.println();
        System.out.println("  2. 👤 Individual - Advanced Mode");
        System.out.println("     └─ 3 matching cards (same branch) = 3 ECTS");
        System.out.println();
        System.out.println("  3. 👥 Team - Simple Mode");
        System.out.println("     └─ Teams of 2, 3 matching cards = 2 ECTS");
        System.out.println();
        System.out.println("  4. 👥 Team - Advanced Mode");
        System.out.println("     └─ Teams of 2, 3 matching cards (same branch) = 3 ECTS");
        System.out.println();
        System.out.print("Enter your choice (1-4): ");

        String input = inputReader.readLine().trim();

        if (input.equalsIgnoreCase("cancel")) {
            return null;
        }

        try {
            int choice = Integer.parseInt(input);
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
                    System.out.println(" Invalid choice!");
                    return getGameMode(); // Retry
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input!");
            return getGameMode(); // Retry
        }
    }


    public Difficulty getDifficulty() {
        System.out.println("\n⚙️  Select Difficulty:");
        System.out.println();
        System.out.println("  1. ✅ Simple");
        System.out.println("     └─ Any 3 matching cards = 2 ECTS");
        System.out.println();
        System.out.println("  2. 🔥 Advanced");
        System.out.println("     └─ 3 matching cards (same branch) = 3 ECTS");
        System.out.println();
        System.out.print("Enter your choice (1-2): ");

        String input = inputReader.readLine().trim();

        if (input.equalsIgnoreCase("cancel")) {
            return null;
        }

        try {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    return Difficulty.SIMPLE;
                case 2:
                    return Difficulty.ADVANCED;
                default:
                    System.out.println(" Invalid choice!");
                    return getDifficulty();
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input!");
            return getDifficulty();
        }
    }

    /**
     * Get player names from user
     * @param count Number of players
     * @return List of player names
     */
    public List<String> getPlayerNames(int count) {
        List<String> names = new ArrayList<>();

        System.out.println("\n  Enter Player Names:");
        System.out.println("(Names should be 1-20 characters)\n");

        for (int i = 1; i <= count; i++) {
            String name = getPlayerName(i);
            if (name == null) {
                return null; // User cancelled
            }
            names.add(name);
        }

        // Confirm names
        System.out.println("\n Players:");
        for (int i = 0; i < names.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + names.get(i));
        }

        System.out.print("\nConfirm? (y/n): ");
        String confirm = inputReader.readLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            return names;
        } else {
            System.out.println("\nLet's try again...");
            return getPlayerNames(count); // Retry
        }
    }

    // Get a single player name

    private String getPlayerName(int playerNumber) {
        while (true) {
            System.out.print("Player " + playerNumber + " name: ");
            String name = inputReader.readLine().trim();

            if (name.equalsIgnoreCase("cancel")) {
                return null;
            }

            if (name.isEmpty()) {
                System.out.println(" Name cannot be empty!");
                continue;
            }

            if (name.length() > 20) {
                System.out.println(" Name too long (max 20 characters)!");
                continue;
            }

            return name;
        }
    }

    // Display game configuration summary

    public void displayConfiguration(GameMode mode, Difficulty difficulty, List<String> playerNames) {
        System.out.println("\n Game configuration");

        System.out.println();
        System.out.println("  Mode: " + mode.getDisplayName());
        System.out.println("  Difficulty: " + difficulty.getDisplayName());
        System.out.println("  Players: " + playerNames.size());
        System.out.println();

        if (mode.isTeamMode()) {
            System.out.println("  Teams:");
            for (int i = 0; i < playerNames.size(); i += 2) {
                System.out.println("    Team " + ((i / 2) + 1) + ": " +
                        playerNames.get(i) + " & " + playerNames.get(i + 1));
            }
        } else {
            System.out.println("  Players:");
            for (int i = 0; i < playerNames.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + playerNames.get(i));
            }
        }
        System.out.println();
    }
}
