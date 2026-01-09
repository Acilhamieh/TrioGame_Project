import view.console.*;
import controller.*;
import model.*;
import enums.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Automated test for console interface
 * Tests all console components without requiring manual input
 */
public class ConsoleTest {

    public static void main(String[] args) {
        System.out.println("CONSOLE VIEW TEST");

        testInputReader();
        testCardDisplayer();
        testMenuView();
        testGameView();
        testIntegration();

        System.out.println("ALL CONSOLE TESTS PASSED!");
    }

    private static void testInputReader() {
        System.out.println("  Testing InputReader...");

        // Test with simulated input
        String simulatedInput = "3\nyes\nDana\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        InputReader reader = new InputReader(new java.util.Scanner(inputStream));

        // Test reading line
        String line = reader.readLine();
        assert line.equals("3") : "Should read '3'";
        System.out.println("   Read line: " + line);

        // Test reading yes/no
        boolean yesNo = reader.readYesNo();
        assert yesNo : "Should read 'yes' as true";
        System.out.println("   Read yes/no: " + yesNo);

        // Test reading string
        String name = reader.readLine();
        assert name.equals("Dana") : "Should read 'Dana'";
        System.out.println("   Read name: " + name);

        System.out.println(" InputReader working!\n");
    }

    private static void testCardDisplayer() {
        System.out.println(" Testing CardDisplayer...");

        CardDisplayer displayer = new CardDisplayer();

        // Create test cards
        Card card1 = new Card("AP4B", Branch.COMPUTER_SCIENCE);
        Card card2 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card card3 = new Card("GI40", Branch.INDUSTRIAL_ENGINEERING);
        Card pfCard = new Card("PFE", Branch.SPECIAL);

        // Test hand display
        Student student = new Student("Test");
        student.getHand().addCard(card1);
        student.getHand().addCard(card2);
        student.getHand().addCard(card3);

        System.out.println("  Testing hand display:");
        displayer.displayHand(student.getHand());
        System.out.println("  ✓ Hand displayed");

        // Test lecture hall display
        LectureHall hall = new LectureHall();
        for (int i = 0; i < 9; i++) {
            hall.addCard(new Card("CARD" + i, Branch.COMPUTER_SCIENCE));
        }

        System.out.println("\n  Testing lecture hall display:");
        displayer.displayLectureHall(hall);
        System.out.println("  ✓ Lecture hall displayed");

        // Test trio display
        System.out.println("\n  Testing trio display:");
        displayer.displayTrio(card1, card1, card1, true, 2);
        System.out.println("  ✓ Trio displayed");

        System.out.println(" CardDisplayer working!\n");
    }

    private static void testMenuView() {
        System.out.println("📋 Testing MenuView...");

        // MenuView requires manual input, so we'll just test creation
        MenuView menu = new MenuView();

        System.out.println("  ✓ MenuView created successfully");
        System.out.println("  ✓ Can display main menu (requires manual testing)");
        System.out.println("  ✓ Can get player count (requires manual testing)");
        System.out.println("  ✓ Can get game mode (requires manual testing)");

        System.out.println(" MenuView structure working!\n");
    }

    private static void testGameView() {
        System.out.println(" Testing GameView...");

        GameView gameView = new GameView();

        // Set up a test game
        GameController controller = new GameController();
        List<String> names = Arrays.asList("Dana", "Acil", "Alice");
        controller.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, names);
        controller.startGame();

        System.out.println("\n  Testing game state display:");
        System.out.println("  " + "─".repeat(40));
        gameView.displayGameState(controller);
        System.out.println("  " + "─".repeat(40));
        System.out.println("   Game state displayed");

        System.out.println("\n  Testing detailed scores:");
        System.out.println("  " + "─".repeat(40));
        gameView.displayDetailedScores(controller);
        System.out.println("  " + "─".repeat(40));
        System.out.println("   Detailed scores displayed");

        // Test victory display (simulated)
        Student winner = controller.getCurrentPlayer();
        winner.addEcts(6);
        System.out.println("\n  Testing victory display:");
        System.out.println("  " + "─".repeat(40));
        gameView.displayVictory(winner, controller.getGame());
        System.out.println("  " + "─".repeat(40));
        System.out.println("  Victory displayed");

        System.out.println(" GameView working!\n");
    }

    private static void testIntegration() {
        System.out.println(" Testing Console Integration...");

        // Test that all components work together
        GameController controller = new GameController();
        GameView gameView = new GameView();
        CardDisplayer cardDisplayer = new CardDisplayer();

        // Initialize game
        List<String> names = Arrays.asList("Dana", "Acil", "Alice");
        boolean success = controller.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE,
                Difficulty.SIMPLE, names);

        assert success : "Game should initialize";
        controller.startGame();

        System.out.println("  ✓ Game initialized through controller");

        // Display game state
        Student currentPlayer = controller.getCurrentPlayer();
        assert currentPlayer != null : "Should have current player";
        System.out.println("  ✓ Current player: " + currentPlayer.getName());

        // Display cards
        Hand hand = currentPlayer.getHand();
        assert hand.getSize() > 0 : "Player should have cards";
        System.out.println("  ✓ Player has " + hand.getSize() + " cards");

        // Display lecture hall
        LectureHall hall = controller.getGame().getLectureHall();
        assert hall.getCardCount() == 9 : "Hall should have 9 cards";
        System.out.println("  ✓ Lecture hall has 9 cards");

        System.out.println("\n  Full game state:");
        System.out.println("  " + "═".repeat(50));
        gameView.displayGameState(controller);
        System.out.println("  " + "═".repeat(50));

        System.out.println("  ✓ All console components integrate properly");
        System.out.println(" Integration tests passed!\n");
    }
}
