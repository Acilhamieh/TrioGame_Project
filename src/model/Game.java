package model;

import enums.Difficulty;
import enums.GameMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game manager for Trio_UTBM.
 * Supports TWO modes:
 * 1. FLEXIBLE: Select 3 cards, validate, remove (old system)
 * 2. REVEALING: Reveal cards one by one (new Trio game system)
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 4.0 - Revealing system + Flexible system
 */
public class Game {
    private List<Student> students;
    private List<Team> teams;
    private Deck deck;
    private LectureHall lectureHall;
    private ScoreBoard scoreBoard;
    private TurnManager turnManager;
    private GameMode gameMode;
    private Difficulty difficulty;
    private int numberOfPlayers;

    // NEW: Revealing system state
    private RevealState currentRevealState;
    private boolean isRevealingMode = true; // TRUE = revealing mode, FALSE = flexible mode

    /**
     * Constructor for Game
     */
    public Game() {
        this.students = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.lectureHall = new LectureHall();
        this.scoreBoard = new ScoreBoard();
        this.currentRevealState = new RevealState();
    }

    /**
     * Configure the game with players and settings
     */
    public void configure(int numPlayers, GameMode mode, Difficulty difficulty, List<String> playerNames) {
        // ✅ CRITICAL: Clear any previous game state before starting new game
        this.students.clear();
        this.teams.clear();
        this.lectureHall = new LectureHall();
        this.scoreBoard = new ScoreBoard();
        this.currentRevealState = new RevealState();

        this.numberOfPlayers = numPlayers;
        this.gameMode = mode;
        this.difficulty = difficulty;

        for (String name : playerNames) {
            Student student = new Student(name);
            students.add(student);
            scoreBoard.registerStudent(student);
        }

        if (mode.isTeamMode()) {
            createTeams();
        }

        this.turnManager = new TurnManager(students);
    }

    private void createTeams() {
        for (int i = 0; i < students.size(); i += 2) {
            if (i + 1 < students.size()) {
                String teamName = "Team " + ((i / 2) + 1);
                Team team = new Team(teamName);
                team.addMember(students.get(i));
                team.addMember(students.get(i + 1));
                teams.add(team);
                scoreBoard.registerTeam(team);
            }
        }
    }

    /**
     * Initialize the game - create deck, shuffle, and deal cards
     */
    public void initialize() {
        deck = new Deck(difficulty);
        System.out.println("=== INITIALIZE DEBUG ===");
        System.out.println("Deck created with difficulty: " + difficulty);
        System.out.println("Initial deck size: " + deck.getRemainingCount());

        deck.shuffle();
        System.out.println("After shuffle: " + deck.getRemainingCount());

        int cardsPerPlayer;
        int lectureHallSize;

        switch (numberOfPlayers) {
            case 3:
            case 2:
                cardsPerPlayer = 9;
                lectureHallSize = 9;
                break;
            case 4:
                cardsPerPlayer = 7;
                lectureHallSize = 8;
                break;
            case 5:
                cardsPerPlayer = 6;
                lectureHallSize = 6;
                break;
            case 6:
                cardsPerPlayer = 5;
                lectureHallSize = 6;
                break;
            default:
                cardsPerPlayer = 5;
                lectureHallSize = 9;
        }

        System.out.println("Config: " + numberOfPlayers + " players, " + cardsPerPlayer + " cards each, " + lectureHallSize + " in hall");
        System.out.println("Expected total: " + (numberOfPlayers * cardsPerPlayer + lectureHallSize));

        for (Student student : students) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = deck.dealCard();
                if (card != null) {
                    student.getHand().addCard(card);
                }
            }
            System.out.println(student.getName() + " dealt " + student.getHand().getSize() + " cards");
        }

        System.out.println("After dealing to players, deck has: " + deck.getRemainingCount());

        for (int i = 0; i < lectureHallSize; i++) {
            Card card = deck.dealCard();
            if (card != null) {
                lectureHall.addCard(card);
            }
        }

        System.out.println("Lecture hall has: " + lectureHall.getCardCount() + " cards");
        System.out.println("Deck remaining: " + deck.getRemainingCount());

        // FINAL CHECK
        int totalCards = 0;
        for (Student student : students) {
            totalCards += student.getHand().getSize();
        }
        totalCards += lectureHall.getCardCount();
        System.out.println("TOTAL CARDS IN GAME: " + totalCards + " (should be 36)");
        System.out.println("=== END INITIALIZE ===");
    }

    public void startGame() {
        System.out.println("Game started!");
        System.out.println("Mode: " + gameMode.getDisplayName());
        System.out.println("Players: " + numberOfPlayers);
        System.out.println("System: " + (isRevealingMode ? "REVEALING" : "FLEXIBLE"));
    }

    // ==================== REVEALING SYSTEM (NEW) ====================

    /**
     * NEW: Reveal a single card during turn
     * @param currentPlayer The player revealing
     * @param card The card to reveal
     * @param source "hand", "other_player", "hall"
     * @param playerName For other_player source
     * @param position Position in hand/hall
     * @return Result: "revealed", "mismatch", "trio_complete", "invalid"
     */
    public String revealCard(Student currentPlayer, Card card, String source, String playerName, int position) {
        // Validate the reveal is legal
        if (!validateReveal(currentPlayer, card, source, playerName, position)) {
            return "invalid";
        }

        // Add to reveal state
        currentRevealState.addReveal(card, source, playerName, position);

        // Check for mismatch after 2+ cards
        if (currentRevealState.hasMismatch()) {
            return "mismatch"; // GUI should flip cards back after 2 seconds
        }

        // Check if trio complete (3 cards revealed)
        if (currentRevealState.getRevealCount() == 3) {
            if (currentRevealState.isValidTrio()) {
                return "trio_complete"; // GUI should process trio
            } else {
                return "mismatch"; // Shouldn't happen if mismatch caught earlier
            }
        }

        return "revealed"; // Continue revealing
    }

    /**
     * Validate that a reveal is legal
     */
    private boolean validateReveal(Student currentPlayer, Card card, String source, String playerName, int position) {
        if (source.equals("hand")) {
            // Must be from current player's hand
            if (!currentPlayer.getHand().contains(card)) {
                return false;
            }
            // Must be revealable position (first/last + duplicates)
            if (!currentPlayer.getHand().isPositionRevealable(position)) {
                return false;
            }
        } else if (source.equals("other_player")) {
            // Find other player
            Student otherPlayer = findStudentByName(playerName);
            if (otherPlayer == null || !otherPlayer.getHand().contains(card)) {
                return false;
            }
            // For other players, only first/last allowed (not duplicates from UI perspective)
            List<Integer> firstLast = HandPositionHelper.getFirstLastPositions(otherPlayer.getHand());
            if (!firstLast.contains(position)) {
                return false;
            }
        } else if (source.equals("hall")) {
            // Must be in lecture hall
            if (!lectureHall.contains(card)) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Complete a trio after 3 cards revealed
     * Called by GUI after "trio_complete" result
     */
    public boolean completeRevealedTrio(Student currentPlayer) {
        if (currentRevealState.getRevealCount() != 3 || !currentRevealState.isValidTrio()) {
            return false;
        }

        List<RevealState.RevealedCard> revealed = currentRevealState.getRevealedCards();

        // Create trio
        Trio trio = new Trio(
                revealed.get(0).card,
                revealed.get(1).card,
                revealed.get(2).card
        );

        // Award ECTS
        int ects = trio.calculateEcts(gameMode);
        currentPlayer.addEcts(ects);
        currentPlayer.addCompletedTrio(trio);
        scoreBoard.updateScore(currentPlayer, ects);
        scoreBoard.recordTrio(currentPlayer, trio);

        if (gameMode.isTeamMode() && currentPlayer.getTeam() != null) {
            Team team = currentPlayer.getTeam();
            team.addCompletedTrio(trio);
            scoreBoard.updateTeamScore(team, ects);
            scoreBoard.recordTeamTrio(team, trio);
        }

        // Remove cards from sources
        List<Student> playersToRefill = new ArrayList<>();
        playersToRefill.add(currentPlayer);

        for (RevealState.RevealedCard rc : revealed) {
            if (rc.source.equals("hand")) {
                currentPlayer.getHand().removeCard(rc.card);
            } else if (rc.source.equals("other_player")) {
                Student otherPlayer = findStudentByName(rc.playerName);
                if (otherPlayer != null) {
                    otherPlayer.getHand().removeCard(rc.card);
                    if (!playersToRefill.contains(otherPlayer)) {
                        playersToRefill.add(otherPlayer);
                    }
                }
            } else if (rc.source.equals("hall")) {
                lectureHall.removeCard(rc.card);
            }
        }

        // Refill hands
        for (Student player : playersToRefill) {
            refillPlayerHand(player);
        }
        refillLectureHall();

        // Clear reveal state
        currentRevealState.clear();

        return true; // Bonus turn! Don't advance turn
    }

    /**
     * Handle mismatch - flip cards back and end turn
     * Called by GUI after "mismatch" result
     */
    public void handleMismatch() {
        currentRevealState.clear();
        turnManager.nextTurn(); // Advance to next player
    }

    /**
     * Get current reveal state (for GUI display)
     */
    public RevealState getRevealState() {
        return currentRevealState;
    }

    /**
     * Clear reveal state (for ending turn early or errors)
     */
    public void clearRevealState() {
        currentRevealState.clear();
    }

    // ==================== FLEXIBLE SYSTEM (OLD) ====================

    /**
     * FLEXIBLE: Play turn by selecting 3 cards at once
     * @param currentPlayer The player making the move
     * @param selectedCards The 3 cards (from anywhere)
     * @param sources Array of sources: "hand", "other_player", "hall"
     * @param playerNames Array of player names (for other_player sources)
     * @return true if valid trio
     */
    public boolean playFlexibleTurn(Student currentPlayer, List<Card> selectedCards,
                                    String[] sources, String[] playerNames) {
        if (selectedCards == null || selectedCards.size() != 3) {
            return false;
        }

        // Track which players need refilling
        List<Student> playersToRefill = new ArrayList<>();
        playersToRefill.add(currentPlayer);

        // Validate each card is from its claimed source
        for (int i = 0; i < 3; i++) {
            Card card = selectedCards.get(i);
            String source = sources[i];

            if (source.equals("hand")) {
                if (!currentPlayer.getHand().contains(card)) {
                    System.out.println("ERROR: Card not in player's hand!");
                    return false;
                }
            } else if (source.equals("other_player")) {
                Student otherPlayer = findStudentByName(playerNames[i]);
                if (otherPlayer == null || !otherPlayer.getHand().contains(card)) {
                    System.out.println("ERROR: Card not in other player's hand!");
                    return false;
                }
                if (!playersToRefill.contains(otherPlayer)) {
                    playersToRefill.add(otherPlayer);
                }
            } else if (source.equals("hall")) {
                if (!lectureHall.contains(card)) {
                    System.out.println("ERROR: Card not in lecture hall!");
                    return false;
                }
            }
        }

        // Validate trio
        Trio trio = new Trio(selectedCards.get(0), selectedCards.get(1), selectedCards.get(2));
        boolean isValid = validateTrio(trio);

        if (isValid) {
            int ects = trio.calculateEcts(gameMode);

            currentPlayer.addEcts(ects);
            currentPlayer.addCompletedTrio(trio);

            scoreBoard.updateScore(currentPlayer, ects);
            scoreBoard.recordTrio(currentPlayer, trio);

            if (gameMode.isTeamMode() && currentPlayer.getTeam() != null) {
                Team team = currentPlayer.getTeam();
                team.addCompletedTrio(trio);
                scoreBoard.updateTeamScore(team, ects);
                scoreBoard.recordTeamTrio(team, trio);
            }

            // Remove cards from their sources
            for (int i = 0; i < 3; i++) {
                Card card = selectedCards.get(i);
                String source = sources[i];

                if (source.equals("hand")) {
                    currentPlayer.getHand().removeCard(card);
                } else if (source.equals("other_player")) {
                    Student otherPlayer = findStudentByName(playerNames[i]);
                    if (otherPlayer != null) {
                        otherPlayer.getHand().removeCard(card);
                    }
                } else if (source.equals("hall")) {
                    lectureHall.removeCard(card);
                }
            }

            // Refill all affected players
            for (Student player : playersToRefill) {
                refillPlayerHand(player);
            }

            // Refill lecture hall
            refillLectureHall();

            return true; // Bonus turn!
        }

        // Invalid trio
        turnManager.nextTurn();
        return false;
    }

    // ==================== SHARED METHODS ====================

    /**
     * Find student by name
     */
    private Student findStudentByName(String name) {
        if (name == null) return null;
        for (Student student : students) {
            if (student.getName().equals(name)) {
                return student;
            }
        }
        return null;
    }

    public Student getNeighbor(Student student) {
        int currentIndex = students.indexOf(student);
        if (currentIndex == -1) return null;
        int neighborIndex = (currentIndex + 1) % students.size();
        return students.get(neighborIndex);
    }

    /**
     * LEGACY: Play turn with specific player (backward compatibility)
     */
    public boolean playTurnWithPlayer(Student student, Student otherPlayer, List<Card> selectedCards) {
        String[] sources = {"hand", "other_player", "hall"};
        String[] playerNames = {null, otherPlayer.getName(), null};
        return playFlexibleTurn(student, selectedCards, sources, playerNames);
    }

    /**
     * LEGACY: Play turn with neighbor (backward compatibility)
     */
    public boolean playTurn(Student student, List<Card> selectedCards) {
        Student neighbor = getNeighbor(student);
        return playTurnWithPlayer(student, neighbor, selectedCards);
    }

    public boolean validateTrio(Trio trio) {
        return trio.isValidForMode(gameMode);
    }

    private void refillLectureHall() {
        int targetSize;
        switch (numberOfPlayers) {
            case 3:
            case 2:
                targetSize = 9;
                break;
            case 4:
                targetSize = 8;
                break;
            case 5:
            case 6:
                targetSize = 6;
                break;
            default:
                targetSize = 9;
        }

        while (lectureHall.getCardCount() < targetSize && !deck.isEmpty()) {
            Card card = deck.dealCard();
            if (card != null) {
                lectureHall.addCard(card);
            }
        }
    }

    private void refillPlayerHand(Student student) {
        int targetSize;
        switch (numberOfPlayers) {
            case 3:
            case 2:
                targetSize = 9;
                break;
            case 4:
                targetSize = 7;
                break;
            case 5:
                targetSize = 6;
                break;
            case 6:
                targetSize = 5;
                break;
            default:
                targetSize = 5;
        }

        while (student.getHand().getSize() < targetSize && !deck.isEmpty()) {
            Card card = deck.dealCard();
            if (card != null) {
                student.getHand().addCard(card);
            }
        }
    }

    /**
     * Check victory conditions
     * Simple mode: 3 trios OR trio of 7 (PFE)
     * Advanced mode (Picante): 2 linked trios OR trio of 7 (PFE)
     */
    public Student checkVictoryConditions() {
        if (!gameMode.isTeamMode()) {
            for (Student student : students) {
                // Check based on difficulty
                if (difficulty == Difficulty.SIMPLE) {
                    // Simple: 3 trios OR trio of PFE
                    if (student.hasWonSimple()) {
                        return student;
                    }
                } else if (difficulty == Difficulty.ADVANCED) {
                    // Advanced: 2 linked trios OR trio of PFE
                    if (student.hasWonPicante()) {
                        return student;
                    }
                }

                // Also check ECTS graduation (6+)
                if (student.hasGraduated()) {
                    return student;
                }
            }
        } else {
            // Team mode - check team victories
            for (Team team : teams) {
                if (team.hasGraduated()) {
                    return team.getMembers().get(0);
                }
            }
        }
        return null;
    }

    public void endGame() {
        System.out.println("\n=== GAME OVER ===");
        System.out.println(scoreBoard.toString());

        if (!gameMode.isTeamMode()) {
            Student winner = scoreBoard.getWinner();
            if (winner != null) {
                System.out.println("\nWinner: " + winner.getName() + " with " +
                        winner.getEctsCredits() + " ECTS!");
            }
        } else {
            Team winner = scoreBoard.getWinningTeam();
            if (winner != null) {
                System.out.println("\nWinning Team: " + winner.getTeamName() + " with " +
                        winner.getTeamScore() + " ECTS!");
            }
        }
    }

    // ==================== GETTERS/SETTERS ====================

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    public Deck getDeck() {
        return deck;
    }

    public LectureHall getLectureHall() {
        return lectureHall;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isRevealingMode() {
        return isRevealingMode;
    }

    public void setRevealingMode(boolean revealingMode) {
        this.isRevealingMode = revealingMode;
    }
}
