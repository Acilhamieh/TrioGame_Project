package controller;

import model.*;
import enums.*;
import java.util.List;

/**
 * Main game controller - manages the overall game flow and state.
 * Coordinates between model components and handles game logic.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class GameController {
    private Game game;
    private GameStateMachine stateMachine;
    private TurnController turnController;
    private boolean gameRunning;

    /**
     * Constructor for GameController
     */
    public GameController() {
        this.game = new Game();
        this.stateMachine = new GameStateMachine();
        this.gameRunning = false;
    }

    /**
     * Initialize a new game with player configuration
     * @param numPlayers Number of players (2-6)
     * @param mode Game mode
     * @param difficulty Difficulty level
     * @param playerNames List of player names
     * @return true if initialization successful
     */
    public boolean initializeGame(int numPlayers, GameMode mode, Difficulty difficulty, List<String> playerNames) {
        try {
            // Validate inputs
            if (numPlayers < 2 || numPlayers > 6) {
                System.out.println("Error: Must have 2-6 players");
                return false;
            }

            if (playerNames == null || playerNames.size() != numPlayers) {
                System.out.println("Error: Player names don't match player count");
                return false;
            }

            // Team mode requires even number of players
            if (mode.isTeamMode() && numPlayers % 2 != 0) {
                System.out.println("Error: Team mode requires even number of players");
                return false;
            }

            // Configure and initialize game
            game.configure(numPlayers, mode, difficulty, playerNames);
            game.initialize();

            // Initialize turn controller
            this.turnController = new TurnController(game);

            // Set initial state
            stateMachine.transitionTo(GameState.SETUP);

            return true;

        } catch (Exception e) {
            System.out.println("Error initializing game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Start the game - transition to PLAYING state
     */
    public void startGame() {
        if (stateMachine.getCurrentState() == GameState.SETUP) {
            stateMachine.transitionTo(GameState.PLAYING);
            gameRunning = true;
            game.startGame();
            System.out.println("\n🎮 Game started!");
            System.out.println("Mode: " + game.getGameMode().getDisplayName());
            System.out.println("Players: " + game.getNumberOfPlayers());
            System.out.println("First player: " + getCurrentPlayer().getName());
        }
    }

    /**
     * Execute a player's turn
     * @param cardIndices Indices of 3 cards selected (2 from hand, 1 from hall)
     * @return true if turn was successful
     */
    public boolean executeTurn(int[] cardIndices) {
        if (!gameRunning || stateMachine.getCurrentState() != GameState.PLAYING) {
            System.out.println("Game is not in playing state");
            return false;
        }

        // Delegate to TurnController
        boolean success = turnController.executeTurn(cardIndices);

        if (success) {
            // Check victory after successful turn
            checkVictory();
        }

        return success;
    }

    /**
     * Check if any player/team has won
     */
    public void checkVictory() {
        stateMachine.transitionTo(GameState.CHECKING_VICTORY);

        Student winner = game.checkVictoryConditions();

        if (winner != null) {
            // Someone won!
            stateMachine.transitionTo(GameState.GAME_OVER);
            gameRunning = false;
            announceWinner(winner);
        } else {
            // No winner yet, continue playing
            stateMachine.transitionTo(GameState.PLAYING);
        }
    }

    /**
     * Announce the winner and end the game
     * @param winner The winning student
     */
    private void announceWinner(Student winner) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          🎓 GRADUATION! 🎓             ║");
        System.out.println("╚════════════════════════════════════════╝");

        if (game.getGameMode().isTeamMode() && winner.getTeam() != null) {
            Team team = winner.getTeam();
            System.out.println("\n🏆 " + team.getTeamName() + " has graduated!");
            System.out.println("Team ECTS: " + team.getTeamScore());
            System.out.println("Members:");
            for (Student member : team.getMembers()) {
                System.out.println("  - " + member.getName() + " (" + member.getEctsCredits() + " ECTS)");
            }
        } else {
            System.out.println("\n🏆 " + winner.getName() + " has graduated!");
            System.out.println("ECTS Credits: " + winner.getEctsCredits());
            System.out.println("Trios completed: " + winner.getTrioCount());
        }

        game.endGame();
    }

    /**
     * Get the current player
     * @return Current student whose turn it is
     */
    public Student getCurrentPlayer() {
        return game.getTurnManager().getCurrentStudent();
    }

    /**
     * Get the game state
     * @return Current game state
     */
    public GameState getGameState() {
        return stateMachine.getCurrentState();
    }

    /**
     * Check if game is running
     * @return true if game is active
     */
    public boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Get the game object
     * @return The game instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get game information summary
     * @return String with game info
     */
    public String getGameInfo() {
        StringBuilder info = new StringBuilder();
        info.append("═══════════════════════════════════════\n");
        info.append("Game Mode: ").append(game.getGameMode().getDisplayName()).append("\n");
        info.append("Players: ").append(game.getNumberOfPlayers()).append("\n");
        info.append("Round: ").append(game.getTurnManager().getRoundNumber()).append("\n");
        info.append("Current Player: ").append(getCurrentPlayer().getName()).append("\n");
        info.append("Deck Remaining: ").append(game.getDeck().getRemainingCount()).append(" cards\n");
        info.append("═══════════════════════════════════════\n");
        return info.toString();
    }

    /**
     * Force end the game
     */
    public void endGame() {
        gameRunning = false;
        stateMachine.transitionTo(GameState.GAME_OVER);
        game.endGame();
    }
}
