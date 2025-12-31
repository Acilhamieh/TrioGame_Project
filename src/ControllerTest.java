import controller.*;
import model.*;
import enums.*;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for all controller components
 * Verifies that controllers work correctly with model classes
 */
public class ControllerTest {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   CONTROLLER TEST SUITE                ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        testGameStateMachine();
        testTrioValidator();
        testInputHandler();
        testGameController();
        testTurnController();
        testIntegration();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   ✅ ALL CONTROLLER TESTS PASSED!      ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void testGameStateMachine() {
        System.out.println("🔄 Testing GameStateMachine...");

        GameStateMachine sm = new GameStateMachine();

        // Initial state
        assert sm.getCurrentState() == GameState.SETUP : "Should start in SETUP";
        assert sm.isSetup() : "isSetup() should be true";
        System.out.println("  ✓ Initial state: SETUP");

        // Valid transition: SETUP → PLAYING
        assert sm.transitionTo(GameState.PLAYING) : "Should transition to PLAYING";
        assert sm.isPlaying() : "isPlaying() should be true";
        System.out.println("  ✓ Transition: SETUP → PLAYING");

        // Valid transition: PLAYING → CHECKING_VICTORY
        assert sm.transitionTo(GameState.CHECKING_VICTORY) : "Should transition to CHECKING_VICTORY";
        System.out.println("  ✓ Transition: PLAYING → CHECKING_VICTORY");

        // Valid transition: CHECKING_VICTORY → GAME_OVER
        assert sm.transitionTo(GameState.GAME_OVER) : "Should transition to GAME_OVER";
        assert sm.isGameOver() : "isGameOver() should be true";
        System.out.println("  ✓ Transition: CHECKING_VICTORY → GAME_OVER");

        // Reset
        sm.reset();
        assert sm.getCurrentState() == GameState.SETUP : "Reset should return to SETUP";
        System.out.println("  ✓ Reset to SETUP");

        System.out.println("✅ GameStateMachine working!\n");
    }

    private static void testTrioValidator() {
        System.out.println("🎯 Testing TrioValidator...");

        // Valid Simple mode trio
        Card c1 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card c2 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card c3 = new Card("SY41", Branch.COMPUTER_SCIENCE);

        assert TrioValidator.validateTrio(c1, c2, c3, GameMode.INDIVIDUAL_SIMPLE)
                : "Should be valid for Simple mode";
        System.out.println("  ✓ Valid Simple mode trio");

        assert TrioValidator.areCardsMatching(c1, c2, c3) : "Cards should match";
        System.out.println("  ✓ Card matching detection");

        assert TrioValidator.areSameBranch(c1, c2, c3) : "Cards should be same branch";
        System.out.println("  ✓ Branch matching detection");

        // ECTS calculation
        int ects = TrioValidator.calculateEcts(c1, c2, c3, GameMode.INDIVIDUAL_SIMPLE);
        assert ects == 2 : "Simple mode should award 2 ECTS";
        System.out.println("  ✓ ECTS calculation: " + ects);

        // PFE trio
        Card pfe1 = new Card("PFE", Branch.SPECIAL);
        Card pfe2 = new Card("PFE", Branch.SPECIAL);
        Card pfe3 = new Card("PFE", Branch.SPECIAL);

        assert TrioValidator.isPFETrio(pfe1, pfe2, pfe3) : "Should detect PFE trio";
        int pfeEcts = TrioValidator.calculateEcts(pfe1, pfe2, pfe3, GameMode.INDIVIDUAL_SIMPLE);
        assert pfeEcts == 6 : "PFE should award 6 ECTS";
        System.out.println("  ✓ PFE trio detection (6 ECTS)");

        // Invalid trio
        Card d1 = new Card("SY41", Branch.COMPUTER_SCIENCE);
        Card d2 = new Card("IA41", Branch.COMPUTER_SCIENCE);
        Card d3 = new Card("LO43", Branch.COMPUTER_SCIENCE);

        assert !TrioValidator.validateTrio(d1, d2, d3, GameMode.INDIVIDUAL_SIMPLE)
                : "Should be invalid (different cards)";
        System.out.println("  ✓ Invalid trio detection");

        // Detailed validation
        TrioValidator.ValidationResult result =
                TrioValidator.validateDetailed(c1, c2, c3, GameMode.INDIVIDUAL_SIMPLE);
        assert result.isValid() : "Detailed validation should succeed";
        assert result.getEctsAwarded() == 2 : "Should award 2 ECTS";
        System.out.println("  ✓ Detailed validation: " + result);

        System.out.println("✅ TrioValidator working!\n");
    }

    private static void testInputHandler() {
        System.out.println("⌨️  Testing InputHandler...");

        InputHandler handler = new InputHandler();

        // Parse player count
        assert handler.parsePlayerCount("3") == 3 : "Should parse 3 players";
        assert handler.parsePlayerCount("4") == 4 : "Should parse 4 players";
        assert handler.parsePlayerCount("5") == -1 : "Should reject 5 players";
        System.out.println("  ✓ Player count parsing");

        // Parse game mode
        assert handler.parseGameMode("1") == GameMode.INDIVIDUAL_SIMPLE : "Should parse mode 1";
        assert handler.parseGameMode("2") == GameMode.INDIVIDUAL_ADVANCED : "Should parse mode 2";
        System.out.println("  ✓ Game mode parsing");

        // Parse difficulty
        assert handler.parseDifficulty("1") == Difficulty.SIMPLE : "Should parse Easy";
        assert handler.parseDifficulty("2") == Difficulty.SIMPLE : "Should parse Normal";
        assert handler.parseDifficulty("3") == Difficulty.ADVANCED : "Should parse Hard";
        System.out.println("  ✓ Difficulty parsing");

        // Parse card selection
        int[] selection = handler.parseCardSelection("0 1 2");
        assert selection != null : "Should parse valid selection";
        assert selection.length == 3 : "Should have 3 indices";
        assert selection[0] == 0 && selection[1] == 1 && selection[2] == 2 : "Indices should match";
        System.out.println("  ✓ Card selection parsing");

        // Parse card selection with commas
        int[] selection2 = handler.parseCardSelection("0,1,2");
        assert selection2 != null : "Should parse comma-separated selection";
        System.out.println("  ✓ Comma-separated parsing");

        // Validate player names
        assert handler.isValidPlayerName("Dana") : "Valid name should pass";
        assert !handler.isValidPlayerName("") : "Empty name should fail";
        assert !handler.isValidPlayerName("ThisNameIsWayTooLongForAPlayer") : "Long name should fail";
        System.out.println("  ✓ Player name validation");

        // Number validation
        assert handler.isNumber("123") : "Should recognize number";
        assert !handler.isNumber("abc") : "Should reject non-number";
        System.out.println("  ✓ Number validation");

        // Menu choice parsing
        assert handler.parseMenuChoice("1", 3) == 1 : "Should parse valid choice";
        assert handler.parseMenuChoice("5", 3) == -1 : "Should reject out-of-range";
        System.out.println("  ✓ Menu choice parsing");

        System.out.println("✅ InputHandler working!\n");
    }

    private static void testGameController() {
        System.out.println("🎮 Testing GameController...");

        GameController gc = new GameController();

        // Initialize game
        List<String> names = Arrays.asList("Dana", "Acil", "Alice");
        boolean success = gc.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, names);

        assert success : "Game initialization should succeed";
        System.out.println("  ✓ Game initialization");

        // Check state
        assert gc.getGameState() == GameState.SETUP : "Should be in SETUP state";
        System.out.println("  ✓ Initial state: SETUP");

        // Start game
        gc.startGame();
        assert gc.getGameState() == GameState.PLAYING : "Should be in PLAYING state";
        assert gc.isGameRunning() : "Game should be running";
        System.out.println("  ✓ Game started");

        // Get current player
        Student currentPlayer = gc.getCurrentPlayer();
        assert currentPlayer != null : "Should have current player";
        System.out.println("  ✓ Current player: " + currentPlayer.getName());

        // Get game info
        String info = gc.getGameInfo();
        assert info.contains("Game Mode") : "Info should contain game mode";
        System.out.println("  ✓ Game info retrieved");

        System.out.println("✅ GameController working!\n");
    }

    private static void testTurnController() {
        System.out.println("🔄 Testing TurnController...");

        // Set up game
        Game game = new Game();
        List<String> names = Arrays.asList("Dana", "Acil", "Alice");
        game.configure(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, names);
        game.initialize();

        TurnController tc = new TurnController(game);

        // Display methods should not crash
        tc.displayCurrentHand();
        System.out.println("  ✓ Display hand");

        tc.displayLectureHall();
        System.out.println("  ✓ Display lecture hall");

        tc.displayScores();
        System.out.println("  ✓ Display scores");

        String turnInfo = tc.getTurnInfo();
        assert turnInfo.contains("Current Player") : "Turn info should contain player";
        System.out.println("  ✓ Get turn info");

        System.out.println("✅ TurnController working!\n");
    }

    private static void testIntegration() {
        System.out.println("🔗 Testing Controller Integration...");

        // Create full game with all controllers
        GameController gc = new GameController();

        List<String> names = Arrays.asList("Dana", "Acil", "Alice");
        gc.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, names);
        gc.startGame();

        // Verify all components work together
        assert gc.isGameRunning() : "Game should be running";
        assert gc.getCurrentPlayer() != null : "Should have current player";
        assert gc.getGame().getDeck().getRemainingCount() > 0 : "Deck should have cards";
        assert gc.getGame().getLectureHall().getCardCount() == 9 : "Hall should have 9 cards";

        System.out.println("  ✓ All controllers integrate properly");
        System.out.println("  ✓ Game state consistent");
        System.out.println("  ✓ Controllers communicate correctly");

        System.out.println("✅ Integration tests passed!\n");
    }
}
