package view.console;

import controller.GameController;
import enums.Difficulty;
import enums.GameMode;
import model.Card;
import model.Hand;
import model.LectureHall;
import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);
    private final GameController controller = new GameController();

    public void start() {
        while (true) {
            System.out.println("==================================");
            System.out.println("           TRIO_UTBM");
            System.out.println("==================================");
            System.out.println("1. New Game");
            System.out.println("2. Rules");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    startNewGame();
                    break;
                case "2":
                    showRules();
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showRules() {
        System.out.println("\nRULES (Console Version)");
        System.out.println("----------------------");
        System.out.println("This console version is a simplified mode.");
        System.out.println("On your turn:");
        System.out.println("- Select 2 cards from your hand");
        System.out.println("- Select 1 card from the lecture hall");
        System.out.println("- If the 3 cards match, you win a trio");
        System.out.println("- Otherwise, the turn passes to the next player");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void startNewGame() {
        int numPlayers = askNumberOfPlayers();
        GameMode mode = askGameMode();
        Difficulty difficulty = askDifficulty();
        List<String> names = askPlayerNames(numPlayers);

        boolean ok = controller.initializeGame(numPlayers, mode, difficulty, names);
        if (!ok) {
            System.out.println("Game initialization failed.");
            return;
        }

        controller.startGame();
        gameLoop();
    }

    private void gameLoop() {
        while (controller.isGameRunning()) {
            Student current = controller.getCurrentPlayer();

            System.out.println("\n----------------------------------");
            System.out.println("Current player: " + current.getName());
            System.out.println("ECTS: " + current.getEctsCredits() +
                    " | Trios: " + current.getTrioCount());
            System.out.println("----------------------------------");

            displayHand(current.getHand());

            if (!controller.getGame().getGameMode().isTeamMode()) {
                displayLectureHall(controller.getGame().getLectureHall());
            }

            System.out.println("\n1. Play turn");
            System.out.println("2. View scores");
            System.out.println("3. Quit game");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    playTurn();
                    break;
                case "2":
                    System.out.println(controller.getGame().getScoreBoard().toString());

                    break;
                case "3":
                    controller.endGame();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void playTurn() {
        System.out.println("Enter 3 indices: handIndex1 handIndex2 hallIndex");
        System.out.print("> ");

        try {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            if (parts.length != 3) {
                System.out.println("You must enter exactly 3 numbers.");
                return;
            }

            int[] indices = new int[3];
            for (int i = 0; i < 3; i++) {
                indices[i] = Integer.parseInt(parts[i]);
            }

            controller.executeTurn(indices);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Use numbers only.");
        }
    }

    private void displayHand(Hand hand) {
        System.out.println("\nYour hand:");
        List<Card> cards = hand.getAllCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("[" + i + "] " + cards.get(i).getCourseCode());
        }
    }

    private void displayLectureHall(LectureHall hall) {
        System.out.println("\nLecture hall:");
        List<Card> cards = hall.getAllCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("[" + i + "] " + cards.get(i).getCourseCode());
        }
    }

    private int askNumberOfPlayers() {
        while (true) {
            System.out.print("Number of players (2-6): ");
            try {
                int n = Integer.parseInt(scanner.nextLine());
                if (n >= 2 && n <= 6) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid number.");
        }
    }

    private GameMode askGameMode() {
        while (true) {
            System.out.println("Game mode:");
            System.out.println("1. Individual - Simple");
            System.out.println("2. Individual - Advanced");
            System.out.println("3. Team - Simple");
            System.out.println("4. Team - Advanced");
            System.out.print("Choice: ");

            String c = scanner.nextLine();
            switch (c) {
                case "1": return GameMode.INDIVIDUAL_SIMPLE;
                case "2": return GameMode.INDIVIDUAL_ADVANCED;
                case "3": return GameMode.TEAM_SIMPLE;
                case "4": return GameMode.TEAM_ADVANCED;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private Difficulty askDifficulty() {
        while (true) {
            System.out.println("Difficulty:");
            System.out.println("1. Simple");
            System.out.println("2. Advanced");
            System.out.print("Choice: ");

            String c = scanner.nextLine();
            switch (c) {
                case "1": return Difficulty.SIMPLE;
                case "2": return Difficulty.ADVANCED;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private List<String> askPlayerNames(int count) {
        List<String> names = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            System.out.print("Name of player " + i + ": ");
            names.add(scanner.nextLine().trim());
        }
        return names;
    }
}
