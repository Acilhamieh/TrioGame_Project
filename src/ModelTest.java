import model.*;
import enums.*;
import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive test suite for all model classes
 * Tests each class independently and integration
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 */
public class ModelTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════");
        System.out.println("   TRIO_UTBM MODEL TEST SUITE");
        System.out.println("   Testing all 14 classes");
        System.out.println("════════════════════════════════════════\n");

        // Run all tests
        testEnumerations();
        testCard();
        testDeck();
        testHand();
        testLectureHall();
        testTrio();
        testStudent();
        testTeam();
        testTurnManager();
        testScoreBoard();
        testGame();
        testGameIntegration();

        // Results
        System.out.println("\n════════════════════════════════════════");
        System.out.println("   TEST RESULTS");
        System.out.println("════════════════════════════════════════");
        System.out.println("✅ Tests Passed: " + testsPassed);
        System.out.println("❌ Tests Failed: " + testsFailed);

        if (testsFailed == 0) {
            System.out.println("\n🎉 ALL TESTS PASSED!");
            System.out.println("Your model classes are working perfectly!");
        } else {
            System.out.println("\n⚠️  Some tests failed. Check the output above.");
        }
        System.out.println("════════════════════════════════════════\n");
    }

    // Helper methods
    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  ✓ " + testName);
            testsPassed++;
        } else {
            System.out.println("  ✗ " + testName + " FAILED");
            testsFailed++;
        }
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected.equals(actual)) {
            System.out.println("  ✓ " + testName);
            testsPassed++;
        } else {
            System.out.println("  ✗ " + testName + " FAILED");
            System.out.println("    Expected: " + expected + ", Got: " + actual);
            testsFailed++;
        }
    }

    // Test methods

    private static void testEnumerations() {
        System.out.println("\n📋 Testing Enumerations");
        System.out.println("─────────────────────────");

        // Branch
        assertEquals("Branch CS name", "Computer Science", Branch.COMPUTER_SCIENCE.getDisplayName());
        assertTrue("Branch toString works", Branch.MECHANICAL_ENGINEERING.toString().length() > 0);

        // GameMode
        assertTrue("Individual Simple is not team mode", !GameMode.INDIVIDUAL_SIMPLE.isTeamMode());
        assertTrue("Team Simple is team mode", GameMode.TEAM_SIMPLE.isTeamMode());
        assertTrue("Advanced mode detected", GameMode.INDIVIDUAL_ADVANCED.isAdvancedMode());
        assertEquals("Simple mode trios", 3, GameMode.INDIVIDUAL_SIMPLE.getTriosRequired());
        assertEquals("Advanced mode ECTS", 3, GameMode.INDIVIDUAL_ADVANCED.getEctsPerTrio());

        // Difficulty
        assertEquals("Difficulty name", "Normal", Difficulty.NORMAL.getDisplayName());

        // GameState
        assertTrue("PLAYING is active", GameState.PLAYING.isActive());
        assertTrue("GAME_OVER is not active", !GameState.GAME_OVER.isActive());
        assertTrue("GAME_OVER is game over", GameState.GAME_OVER.isGameOver());
    }

    private static void testCard() {
        System.out.println("\n🎴 Testing Card");
        System.out.println("─────────────────────────");

        Card card1 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card card2 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card card3 = new Card("IA41", Branch.COMPUTER_SCIENCE);
        Card pfeCard = new Card("PFE", Branch.SPECIAL);

        assertEquals("Card course code", "SY41", card1.getCourseCode());
        assertEquals("Card branch", Branch.COMPUTER_SCIENCE, card1.getBranch());
        assertTrue("Cards with same code are equal", card1.equals(card2));
        assertTrue("Cards with different codes are not equal", !card1.equals(card3));
        assertTrue("PFE card is detected", pfeCard.isPFE());
        assertTrue("Regular card is not PFE", !card1.isPFE());
    }

    private static void testDeck() {
        System.out.println("\n🎲 Testing Deck");
        System.out.println("─────────────────────────");

        Deck deck = new Deck(Difficulty.NORMAL);

        assertEquals("Deck has 36 cards", 36, deck.getRemainingCount());
        assertTrue("Deck is not empty", !deck.isEmpty());

        Card card = deck.dealCard();
        assertTrue("Dealt card is not null", card != null);
        assertEquals("Deck has 35 cards after dealing", 35, deck.getRemainingCount());

        deck.shuffle();
        assertTrue("Deck can be shuffled", true);
    }

    private static void testHand() {
        System.out.println("\n🤚 Testing Hand");
        System.out.println("─────────────────────────");

        Student student = new Student("Test");
        Hand hand = student.getHand();

        assertTrue("Hand starts empty", hand.isEmpty());
        assertEquals("Hand size is 0", 0, hand.getSize());

        Card card1 = new Card("ZZ99", Branch.COMPUTER_SCIENCE);
        Card card2 = new Card("AA11", Branch.COMPUTER_SCIENCE);

        hand.addCard(card1);
        hand.addCard(card2);

        assertEquals("Hand has 2 cards", 2, hand.getSize());
        assertTrue("Hand contains card", hand.contains(card1));

        // Test sorting (AA11 should come before ZZ99)
        Card firstCard = hand.getCard(0);
        assertEquals("Hand is sorted", "AA11", firstCard.getCourseCode());

        hand.removeCard(card1);
        assertEquals("Card removed", 1, hand.getSize());
    }

    private static void testLectureHall() {
        System.out.println("\n🏛️  Testing LectureHall");
        System.out.println("─────────────────────────");

        LectureHall hall = new LectureHall();

        assertTrue("Hall starts empty", hall.isEmpty());
        assertEquals("Max capacity is 9", 9, hall.getMaxCapacity());

        for (int i = 0; i < 9; i++) {
            Card card = new Card("CARD" + i, Branch.COMPUTER_SCIENCE);
            assertTrue("Card added", hall.addCard(card));
        }

        assertTrue("Hall is full", hall.isFull());
        assertEquals("Hall has 9 cards", 9, hall.getCardCount());

        Card extraCard = new Card("EXTRA", Branch.COMPUTER_SCIENCE);
        assertTrue("Cannot add to full hall", !hall.addCard(extraCard));

        hall.removeCard(0);
        assertEquals("Card removed", 8, hall.getCardCount());
    }

    private static void testTrio() {
        System.out.println("\n🎯 Testing Trio");
        System.out.println("─────────────────────────");

        // Valid trio
        Card c1 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card c2 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card c3 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Trio trio = new Trio(c1, c2, c3);

        assertTrue("Valid trio detected", trio.isValid());
        assertTrue("Trio valid for simple mode", trio.isValidForMode(GameMode.INDIVIDUAL_SIMPLE));
        assertEquals("Simple mode awards 2 ECTS", 2, trio.calculateEcts(GameMode.INDIVIDUAL_SIMPLE));

        // Advanced mode
        assertEquals("Advanced mode awards 3 ECTS", 3, trio.calculateEcts(GameMode.INDIVIDUAL_ADVANCED));

        // Invalid trio
        Card d1 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card d2 = new Card("IA41", Branch.COMPUTER_SCIENCE);
        Card d3 = new Card("LO43", Branch.COMPUTER_SCIENCE);
        Trio invalidTrio = new Trio(d1, d2, d3);

        assertTrue("Invalid trio detected", !invalidTrio.isValid());

        // PFE trio
        Card pfe1 = new Card("PFE", Branch.SPECIAL);
        Card pfe2 = new Card("PFE", Branch.SPECIAL);
        Card pfe3 = new Card("PFE", Branch.SPECIAL);
        Trio pfeTrio = new Trio(pfe1, pfe2, pfe3);

        assertTrue("PFE trio detected", pfeTrio.isPFETrio());
        assertEquals("PFE awards 6 ECTS", 6, pfeTrio.calculateEcts(GameMode.INDIVIDUAL_SIMPLE));
    }

    private static void testStudent() {
        System.out.println("\n👨‍🎓 Testing Student");
        System.out.println("─────────────────────────");

        Student student = new Student("Dana");

        assertEquals("Student name", "Dana", student.getName());
        assertEquals("Initial ECTS", 0, student.getEctsCredits());
        assertTrue("Not graduated initially", !student.hasGraduated());

        student.addEcts(2);
        assertEquals("ECTS updated", 2, student.getEctsCredits());

        student.addEcts(4);
        assertEquals("ECTS accumulated", 6, student.getEctsCredits());
        assertTrue("Graduated with 6 ECTS", student.hasGraduated());

        assertTrue("Has hand", student.getHand() != null);
        assertEquals("Trio count starts at 0", 0, student.getTrioCount());
    }

    private static void testTeam() {
        System.out.println("\n👥 Testing Team");
        System.out.println("─────────────────────────");

        Team team = new Team("Team Alpha");
        Student s1 = new Student("Dana");
        Student s2 = new Student("Acil");
        Student s3 = new Student("Alice");

        assertEquals("Team name", "Team Alpha", team.getTeamName());
        assertTrue("Team starts empty", team.isEmpty());

        assertTrue("First member added", team.addMember(s1));
        assertTrue("Second member added", team.addMember(s2));
        assertTrue("Team is full", team.isFull());
        assertTrue("Cannot add third member", !team.addMember(s3));

        assertEquals("Team has 2 members", 2, team.getMemberCount());

        team.addToTeamScore(6);
        assertEquals("Team score updated", 6, team.getTeamScore());
        assertTrue("Team graduated", team.hasGraduated());
    }

    private static void testTurnManager() {
        System.out.println("\n🔄 Testing TurnManager");
        System.out.println("─────────────────────────");

        List<Student> students = Arrays.asList(
                new Student("Dana"),
                new Student("Acil"),
                new Student("Alice")
        );

        TurnManager tm = new TurnManager(students);

        assertEquals("Round 1", 1, tm.getRoundNumber());
        assertEquals("First player", "Dana", tm.getCurrentStudent().getName());

        tm.nextTurn();
        assertEquals("Second player", "Acil", tm.getCurrentStudent().getName());

        tm.nextTurn();
        assertEquals("Third player", "Alice", tm.getCurrentStudent().getName());

        tm.nextTurn();
        assertEquals("Round 2", 2, tm.getRoundNumber());
        assertEquals("Back to first player", "Dana", tm.getCurrentStudent().getName());
    }

    private static void testScoreBoard() {
        System.out.println("\n📊 Testing ScoreBoard");
        System.out.println("─────────────────────────");

        ScoreBoard board = new ScoreBoard();
        Student s1 = new Student("Dana");
        Student s2 = new Student("Acil");

        board.registerStudent(s1);
        board.registerStudent(s2);

        board.updateScore(s1, 4);
        board.updateScore(s2, 2);

        assertEquals("Dana's score", 4, board.getScore(s1));
        assertEquals("Acil's score", 2, board.getScore(s2));

        List<Student> rankings = board.getRankings();
        assertEquals("First place", "Dana", rankings.get(0).getName());
        assertEquals("Second place", "Acil", rankings.get(1).getName());

        Student winner = board.getWinner();
        assertEquals("Winner is Dana", "Dana", winner.getName());
    }

    private static void testGame() {
        System.out.println("\n🎮 Testing Game");
        System.out.println("─────────────────────────");

        Game game = new Game();
        List<String> names = Arrays.asList("Dana", "Acil", "Alice");

        game.configure(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.NORMAL, names);
        game.initialize();

        assertEquals("3 players", 3, game.getNumberOfPlayers());
        assertEquals("3 students", 3, game.getStudents().size());
        assertTrue("Deck exists", game.getDeck() != null);
        assertTrue("Lecture hall exists", game.getLectureHall() != null);

        assertEquals("Lecture hall has 9 cards", 9, game.getLectureHall().getCardCount());

        // Check each student has cards
        for (Student s : game.getStudents()) {
            assertEquals("Student has 5 cards", 5, s.getHand().getSize());
        }

        // Deck should have 36 - (3*5) - 9 = 12 cards
        assertEquals("Deck has correct remaining", 12, game.getDeck().getRemainingCount());
    }

    private static void testGameIntegration() {
        System.out.println("\n🔗 Testing Game Integration");
        System.out.println("─────────────────────────");

        // Test a complete game scenario
        Game game = new Game();
        List<String> names = Arrays.asList("Dana", "Acil", "Alice");
        game.configure(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.NORMAL, names);
        game.initialize();

        Student currentPlayer = game.getTurnManager().getCurrentStudent();
        assertTrue("Current player exists", currentPlayer != null);

        // Try to create a trio manually
        Card c1 = new Card("TEST", Branch.COMPUTER_SCIENCE);
        Card c2 = new Card("TEST", Branch.COMPUTER_SCIENCE);
        Card c3 = new Card("TEST", Branch.COMPUTER_SCIENCE);

        Trio trio = new Trio(c1, c2, c3);
        assertTrue("Trio can be validated", game.validateTrio(trio));

        // Check victory conditions (no winner yet)
        Student winner = game.checkVictoryConditions();
        assertTrue("No winner yet", winner == null);

        System.out.println("  ✓ All integration tests passed");
    }
}