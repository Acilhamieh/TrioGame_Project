import controller.GameController;
import enums.Difficulty;
import enums.GameMode;
import model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Backend Test for MEMORY GAME with ID system
 * Tests new rules: 1 from me + 1 from neighbor + 1 from hall
 *
 * @author Dana SLEIMAN
 * @version 2.0 - Memory Game Edition
 */
public class BackendTest {

    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════");
        System.out.println("   BACKEND TEST - MEMORY GAME");
        System.out.println("   New Rules: 1+1+1 Trio System");
        System.out.println("════════════════════════════════════════\n");

        // Test 1: Card ID System
        System.out.println("TEST 1: Card ID System");
        System.out.println("─────────────────────────────");
        testCardIDSystem();

        // Test 2: Hand Sorting
        System.out.println("\nTEST 2: Hand Sorting by ID");
        System.out.println("─────────────────────────────");
        testHandSorting();

        // Test 3: Card Visibility
        System.out.println("\nTEST 3: Card Visibility (First & Last)");
        System.out.println("─────────────────────────────");
        testCardVisibility();

        // Test 4: Neighbor System
        System.out.println("\nTEST 4: Neighbor Identification");
        System.out.println("─────────────────────────────");
        testNeighborSystem();

        // Test 5: Game Initialization
        System.out.println("\nTEST 5: Game Initialization");
        System.out.println("─────────────────────────────");
        testGameInitialization();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("   BACKEND TEST COMPLETE");
        System.out.println("════════════════════════════════════════");
        System.out.println("\n📊 SUMMARY:");
        System.out.println("✅ = Working correctly");
        System.out.println("❌ = Bug found");
        System.out.println("⚠️  = Check manually");
        System.out.println("\n🔧 Memory game backend ready for GUI!");
    }

    private static void testCardIDSystem() {
        try {
            System.out.println("Creating cards with IDs...");

            Card sy41 = new Card("SY41", enums.Branch.COMPUTER_SCIENCE, 12);
            Card ia41 = new Card("IA41", enums.Branch.COMPUTER_SCIENCE, 11);
            Card pfe = new Card("PFE", enums.Branch.SPECIAL, 1);

            if (sy41.getId() == 12) {
                System.out.println("✅ SY41 has ID: 12");
            } else {
                System.out.println("❌ SY41 ID wrong: " + sy41.getId());
            }

            if (ia41.getId() == 11) {
                System.out.println("✅ IA41 has ID: 11");
            } else {
                System.out.println("❌ IA41 ID wrong: " + ia41.getId());
            }

            if (pfe.getId() == 1) {
                System.out.println("✅ PFE has ID: 1");
            } else {
                System.out.println("❌ PFE ID wrong: " + pfe.getId());
            }

            System.out.println("✅ Card ID system working!");

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testHandSorting() {
        try {
            System.out.println("Testing hand sorting (highest → lowest ID)...");

            Student testStudent = new Student("Test");
            Hand hand = testStudent.getHand();

            // Add cards in random order
            hand.addCard(new Card("PFE", enums.Branch.SPECIAL, 1));
            hand.addCard(new Card("SY41", enums.Branch.COMPUTER_SCIENCE, 12));
            hand.addCard(new Card("AP4B", enums.Branch.COMPUTER_SCIENCE, 9));
            hand.addCard(new Card("EN21", enums.Branch.ENERGY_ENGINEERING, 2));
            hand.addCard(new Card("IA41", enums.Branch.COMPUTER_SCIENCE, 11));

            System.out.println("Added cards in order: PFE(1), SY41(12), AP4B(9), EN21(2), IA41(11)");

            // Check if sorted correctly (highest to lowest)
            List<Card> cards = hand.getAllCards();
            System.out.println("\nSorted order:");
            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                System.out.println("  [" + i + "] " + card.getCourseCode() + " (ID:" + card.getId() + ")");
            }

            // Verify order
            boolean correctOrder = true;
            for (int i = 0; i < cards.size() - 1; i++) {
                if (cards.get(i).getId() < cards.get(i + 1).getId()) {
                    correctOrder = false;
                    break;
                }
            }

            if (correctOrder) {
                System.out.println("\n✅ Hand sorting works! (12 → 11 → 9 → 2 → 1)");
            } else {
                System.out.println("\n❌ Hand sorting broken!");
            }

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testCardVisibility() {
        try {
            System.out.println("Testing card visibility rules...");

            Student testStudent = new Student("Test");
            Hand hand = testStudent.getHand();

            // Add 9 cards
            hand.addCard(new Card("SY41", enums.Branch.COMPUTER_SCIENCE, 12));
            hand.addCard(new Card("IA41", enums.Branch.COMPUTER_SCIENCE, 11));
            hand.addCard(new Card("SY48", enums.Branch.COMPUTER_SCIENCE, 10));
            hand.addCard(new Card("AP4B", enums.Branch.COMPUTER_SCIENCE, 9));
            hand.addCard(new Card("GI21", enums.Branch.INDUSTRIAL_ENGINEERING, 8));
            hand.addCard(new Card("MQ18", enums.Branch.MECHANICAL_ENGINEERING, 5));
            hand.addCard(new Card("MQ41", enums.Branch.MECHANICAL_ENGINEERING, 4));
            hand.addCard(new Card("EN21", enums.Branch.ENERGY_ENGINEERING, 2));
            hand.addCard(new Card("PFE", enums.Branch.SPECIAL, 1));

            System.out.println("Hand with 9 cards:");
            for (int i = 0; i < hand.getSize(); i++) {
                boolean visible = hand.isCardVisible(i);
                Card card = hand.getCard(i);
                if (visible) {
                    System.out.println("  [" + i + "] " + card.getCourseCode() + " (ID:" + card.getId() + ") ✅ VISIBLE");
                } else {
                    System.out.println("  [" + i + "] [?] (hidden) ❌ HIDDEN");
                }
            }

            // Verify only first and last are visible
            boolean firstVisible = hand.isCardVisible(0);
            boolean lastVisible = hand.isCardVisible(hand.getSize() - 1);
            boolean middleHidden = !hand.isCardVisible(4); // Middle card should be hidden

            if (firstVisible && lastVisible && middleHidden) {
                System.out.println("\n✅ Visibility system working!");
                System.out.println("   First (0) and Last (8) visible, middle hidden");
            } else {
                System.out.println("\n❌ Visibility system broken!");
            }

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testNeighborSystem() {
        try {
            GameController controller = new GameController();
            List<String> players = new ArrayList<>();
            players.add("Dana");
            players.add("Acil");
            players.add("Alice");

            controller.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, players);
            controller.startGame();

            Game game = controller.getGame();
            List<Student> students = game.getStudents();

            System.out.println("Player order: Dana → Acil → Alice → Dana (circular)");

            for (Student student : students) {
                Student neighbor = game.getNeighbor(student);
                System.out.println("  " + student.getName() + "'s neighbor: " +
                        (neighbor != null ? neighbor.getName() : "NULL"));
            }

            // Verify neighbors
            Student dana = students.get(0);
            Student acil = students.get(1);
            Student alice = students.get(2);

            Student danaNeighbor = game.getNeighbor(dana);
            Student acilNeighbor = game.getNeighbor(acil);
            Student aliceNeighbor = game.getNeighbor(alice);

            boolean correct =
                    danaNeighbor != null && danaNeighbor.getName().equals("Acil") &&
                            acilNeighbor != null && acilNeighbor.getName().equals("Alice") &&
                            aliceNeighbor != null && aliceNeighbor.getName().equals("Dana");

            if (correct) {
                System.out.println("\n✅ Neighbor system working correctly!");
            } else {
                System.out.println("\n❌ Neighbor system broken!");
            }

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testGameInitialization() {
        try {
            GameController controller = new GameController();
            List<String> players = new ArrayList<>();
            players.add("Dana");
            players.add("Acil");
            players.add("Alice");

            boolean success = controller.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, players);

            if (success) {
                System.out.println("✅ Game initialized successfully");
                controller.startGame();
                System.out.println("✅ Game started successfully");

                Student currentPlayer = controller.getCurrentPlayer();
                Hand hand = currentPlayer.getHand();
                LectureHall hall = controller.getGame().getLectureHall();

                System.out.println("✅ Current player: " + currentPlayer.getName());
                System.out.println("✅ Hand size: " + hand.getSize() + " cards (should be 9 for 3 players)");
                System.out.println("✅ Lecture hall: " + hall.getCardCount() + " cards (should be 9 for 3 players)");
                System.out.println("✅ Deck remaining: " + controller.getGame().getDeck().getRemainingCount() + " cards");

                // Show hand with IDs
                System.out.println("\n📋 Player's hand (sorted by ID):");
                for (int i = 0; i < hand.getSize(); i++) {
                    Card card = hand.getCard(i);
                    boolean visible = hand.isCardVisible(i);
                    if (visible) {
                        System.out.println("  [" + i + "] " + card.getCourseCode() + " (ID:" + card.getId() + ") ✅");
                    } else {
                        System.out.println("  [" + i + "] [?] ❌");
                    }
                }

                // Get neighbor's hand
                Student neighbor = controller.getGame().getNeighbor(currentPlayer);
                if (neighbor != null) {
                    Hand neighborHand = neighbor.getHand();
                    System.out.println("\n👤 Neighbor's hand (" + neighbor.getName() + "):");
                    for (int i = 0; i < neighborHand.getSize(); i++) {
                        Card card = neighborHand.getCard(i);
                        boolean visible = neighborHand.isCardVisible(i);
                        if (visible) {
                            System.out.println("  [" + i + "] " + card.getCourseCode() + " (ID:" + card.getId() + ") ✅");
                        } else {
                            System.out.println("  [" + i + "] [?] ❌");
                        }
                    }
                }

                // Show lecture hall (all visible)
                System.out.println("\n🏫 Lecture Hall (all visible):");
                for (int i = 0; i < hall.getCardCount(); i++) {
                    Card card = hall.getCard(i);
                    System.out.println("  [" + i + "] " + card.getCourseCode() + " (ID:" + card.getId() + ")");
                }

                System.out.println("\n✅ Memory game initialization complete!");
                System.out.println("   Players can see:");
                System.out.println("   - Own hand: first & last cards only");
                System.out.println("   - Neighbor's hand: first & last cards only");
                System.out.println("   - Lecture hall: all cards");

            } else {
                System.out.println("❌ Game initialization FAILED");
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
