package model;

import enums.Branch;
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
     * TEAM MODE: All cards distributed to players, NO lecture hall
     * INDIVIDUAL MODE: Cards to players + lecture hall
     */
    public void initialize() {
        deck = new Deck(difficulty);
        System.out.println("=== INITIALIZE DEBUG ===");
        System.out.println("Deck created with difficulty: " + difficulty);
        System.out.println("Initial deck size: " + deck.getRemainingCount());
        System.out.println("Game mode: " + (gameMode.isTeamMode() ? "TEAM" : "INDIVIDUAL"));

        deck.shuffle();
        System.out.println("After shuffle: " + deck.getRemainingCount());

        int cardsPerPlayer;
        int lectureHallSize;

        // ✅ TEAM MODE: All 36 cards distributed to players, NO lecture hall
        if (gameMode.isTeamMode()) {
            // Team mode: Divide all cards among players
            cardsPerPlayer = 36 / numberOfPlayers; // Example: 6 players = 6 cards each
            lectureHallSize = 0; // NO LECTURE HALL in team mode!

            System.out.println("TEAM MODE: All 36 cards distributed among players");
        } else {
            // Individual mode: Players + lecture hall
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
        }

        System.out.println("Config: " + numberOfPlayers + " players, " + cardsPerPlayer + " cards each, " + lectureHallSize + " in hall");
        System.out.println("Expected total: " + (numberOfPlayers * cardsPerPlayer + lectureHallSize));

        // Deal cards to players
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

        // ✅ Deal to lecture hall ONLY in individual mode
        if (!gameMode.isTeamMode()) {
            for (int i = 0; i < lectureHallSize; i++) {
                Card card = deck.dealCard();
                if (card != null) {
                    lectureHall.addCard(card);
                }
            }
            System.out.println("Lecture hall has: " + lectureHall.getCardCount() + " cards");
        } else {
            System.out.println("Lecture hall: EMPTY (team mode)");
        }

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
    /**
     * NEW: Reveal a single card during turn
     * ✅ FEATURE: Auto-reveals duplicates from the same player
     *
     * @param currentPlayer The player revealing
     * @param card The card to reveal
     * @param source "hand", "other_player", "hall"
     * @param playerName For other_player source
     * @param position Position in hand/hall
     * @return Result: "revealed", "mismatch", "trio_complete", "invalid", "auto_reveal"
     */
    public String revealCard(Student currentPlayer, Card card, String source, String playerName, int position) {
        // Validate the reveal is legal
        if (!validateReveal(currentPlayer, card, source, playerName, position)) {
            System.out.println("DEBUG: validateReveal FAILED!");
            System.out.println("  Card: " + card.getCourseCode());
            System.out.println("  Source: " + source);
            System.out.println("  PlayerName: " + playerName);
            System.out.println("  Position: " + position);
            return "invalid";
        }

        // Add to reveal state
        boolean added = currentRevealState.addReveal(card, source, playerName, position);
        if (!added) {
            return "invalid"; // Duplicate blocked
        }

        // ✅ AUTO-REVEAL DUPLICATES: Check if the player has more of this card
        String courseCode = card.getCourseCode();
        System.out.println("DEBUG AUTO-REVEAL: Looking for duplicates of " + courseCode + " from " + source);
        Student sourcePlayer;

        if (source.equals("hand")) {
            sourcePlayer = currentPlayer;
            System.out.println("  Source player: " + sourcePlayer.getName() + " (current player)");
        } else if (source.equals("other_player")) {
            sourcePlayer = findStudentByName(playerName);
            System.out.println("  Source player: " + (sourcePlayer != null ? sourcePlayer.getName() : "NULL") + " (other player: " + playerName + ")");
        } else {
            sourcePlayer = null;
            System.out.println("  Source player: NULL (from lecture hall)");
        }

        // If revealing from a player's hand, check for duplicates
        if (sourcePlayer != null && currentRevealState.getRevealCount() < 3) {
            System.out.println("  Searching " + sourcePlayer.getName() + "'s hand for duplicates of " + courseCode + " (excluding position " + position + ")");

            List<CardWithPosition> duplicates = sourcePlayer.getHand().findDuplicates(courseCode, position);
            System.out.println("  Found " + duplicates.size() + " duplicates");

            // Auto-reveal duplicates (up to 3 total)
            for (CardWithPosition cardWithPos : duplicates) {
                if (currentRevealState.getRevealCount() >= 3) break;

                // ✅ CRITICAL FIX: Use the actual position from CardWithPosition, not indexOf()
                // indexOf() always returns the FIRST occurrence, which is wrong for duplicates!
                int dupPosition = cardWithPos.position;
                Card duplicate = cardWithPos.card;

                // Try to add this duplicate
                boolean dupAdded = currentRevealState.addReveal(duplicate, source, playerName, dupPosition);
                if (dupAdded) {
                    System.out.println("AUTO-REVEAL: Found duplicate " + courseCode + " at position " + dupPosition);
                }
            }
        }

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

        // Check if auto-reveals happened
        if (currentRevealState.getRevealCount() > 1) {
            return "auto_reveal"; // GUI should highlight the auto-revealed cards
        }

        return "revealed"; // Continue revealing
    }

    /**
     * Validate that a reveal is legal
     */
    private boolean validateReveal(Student currentPlayer,
                                   Card card,
                                   String source,
                                   String playerName,
                                   int position) {

        // ===== REVEAL FROM CURRENT PLAYER HAND =====
        if (source.equals("hand")) {

            // Card must belong to current player
            if (!currentPlayer.getHand().contains(card)) {
                return false;
            }

            // ✅ IMPORTANT FIX:
            // Always allow revealing from own hand.
            // Matching / mismatch is handled AFTER reveal, not here.
            return true;
        }

        // ===== REVEAL FROM OTHER PLAYER =====
        else if (source.equals("other_player")) {

            // Cannot reveal from yourself as other_player
            if (playerName != null && playerName.equals(currentPlayer.getName())) {
                return false;
            }

            Student otherPlayer = findStudentByName(playerName);
            if (otherPlayer == null || !otherPlayer.getHand().contains(card)) {
                return false;
            }

            // Only first or last card can be revealed
            List<Integer> firstLast =
                    HandPositionHelper.getFirstLastPositions(otherPlayer.getHand());

            return firstLast.contains(position);
        }

        // ===== REVEAL FROM LECTURE HALL =====
        else if (source.equals("hall")) {

            return lectureHall.contains(card);
        }

        return false;
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
        // ✅ CRITICAL FIX: Group by player, then remove in reverse position order
        List<Student> playersToRefill = new ArrayList<>();
        playersToRefill.add(currentPlayer);

        // DEBUG: Show what we're about to remove
        System.out.println("=== REMOVAL DEBUG ===");
        System.out.println("Current player: " + currentPlayer.getName());
        for (int i = 0; i < revealed.size(); i++) {
            RevealState.RevealedCard rc = revealed.get(i);
            System.out.println("Card " + (i+1) + ": " + rc.card.getCourseCode() +
                    " from " + rc.source +
                    " (player: " + rc.playerName + ", position: " + rc.position + ")");
        }
        System.out.println("=================");

        // Group removals by player to handle position shifting
        java.util.Map<String, List<RevealState.RevealedCard>> removalsByPlayer = new java.util.HashMap<>();

        for (RevealState.RevealedCard rc : revealed) {
            String key;
            if (rc.source.equals("hand")) {
                key = "CURRENT_PLAYER";
            } else if (rc.source.equals("other_player")) {
                key = rc.playerName;
                Student otherPlayer = findStudentByName(rc.playerName);
                if (otherPlayer != null && !playersToRefill.contains(otherPlayer)) {
                    playersToRefill.add(otherPlayer);
                }
            } else {
                key = "LECTURE_HALL";
            }

            removalsByPlayer.computeIfAbsent(key, k -> new ArrayList<>()).add(rc);
        }

        // For each player, remove cards in DESCENDING position order (highest first)
        for (java.util.Map.Entry<String, List<RevealState.RevealedCard>> entry : removalsByPlayer.entrySet()) {
            String playerKey = entry.getKey();
            List<RevealState.RevealedCard> removals = entry.getValue();

            // Sort by position DESCENDING (remove highest index first)
            removals.sort((a, b) -> Integer.compare(b.position, a.position));

            for (RevealState.RevealedCard rc : removals) {
                System.out.println("DEBUG: Removing card " + rc.card.getCourseCode() +
                        " from " + rc.source +
                        " (player: " + rc.playerName + ")" +
                        " at position " + rc.position);

                if (rc.source.equals("hand")) {
                    Card removed = currentPlayer.getHand().removeCard(rc.position);
                    System.out.println("  Removed from current player: " + (removed != null ? removed.getCourseCode() : "FAILED!"));
                } else if (rc.source.equals("other_player")) {
                    Student otherPlayer = findStudentByName(rc.playerName);
                    if (otherPlayer != null) {
                        Card removed = otherPlayer.getHand().removeCard(rc.position);
                        System.out.println("  Removed from " + rc.playerName + ": " + (removed != null ? removed.getCourseCode() : "FAILED!"));
                    } else {
                        System.out.println("  ERROR: Player " + rc.playerName + " not found!");
                    }
                } else if (rc.source.equals("hall")) {
                    Card removed = lectureHall.removeCard(rc.position);
                    System.out.println("  Removed from hall: " + (removed != null ? removed.getCourseCode() : "FAILED!"));
                }
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
        // ✅ TEAM MODE: No lecture hall, skip refill
        if (gameMode.isTeamMode()) {
            return; // No lecture hall in team mode
        }

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

        // ✅ TEAM MODE: All cards distributed among players (36 / numPlayers)
        if (gameMode.isTeamMode()) {
            targetSize = 36 / numberOfPlayers; // Example: 6 players = 6 cards each
        } else {
            // Individual mode: Standard distribution
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
     *
     * INDIVIDUAL MODE: Check each student
     * TEAM MODE: Check team totals (all members' trios combined)
     */
    public Student checkVictoryConditions() {
        if (!gameMode.isTeamMode()) {
            // INDIVIDUAL MODE: Check each student individually
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
            // TEAM MODE: Check team victories (combine both members' trios)
            System.out.println("=== CHECKING TEAM VICTORY ===");
            for (Team team : teams) {
                System.out.println("Checking team: " + team.getTeamName());
                List<Student> members = team.getMembers();

                // Debug: Show each member's trios
                for (Student member : members) {
                    System.out.println("  " + member.getName() + " has " + member.getTrioCount() + " trios");
                }

                // Combine all trios from both team members
                List<Trio> allTeamTrios = new ArrayList<>();
                for (Student member : members) {
                    allTeamTrios.addAll(member.getCompletedTrios());
                }

                System.out.println("  Total team trios: " + allTeamTrios.size());

                // Check for PFE trio (instant win)
                for (Trio trio : allTeamTrios) {
                    if (trio.isPFETrio()) {
                        System.out.println("TEAM WIN: PFE Trio!");
                        return members.get(0); // Return first member as winner
                    }
                }

                if (difficulty == Difficulty.SIMPLE) {
                    // Simple: 3 trios total (combined from both members)
                    if (allTeamTrios.size() >= 3) {
                        System.out.println("TEAM WIN: 3+ trios (" + allTeamTrios.size() + " trios)");
                        return members.get(0);
                    }
                } else if (difficulty == Difficulty.ADVANCED) {
                    // Advanced: 2 linked trios (same branch, combined from both members)
                    int csCount = 0, ieCount = 0, meCount = 0, eeCount = 0;

                    for (Trio trio : allTeamTrios) {
                        Branch branch = trio.getBranch();
                        if (branch == Branch.SPECIAL) continue; // Skip PFE

                        if (branch == Branch.COMPUTER_SCIENCE) csCount++;
                        else if (branch == Branch.INDUSTRIAL_ENGINEERING) ieCount++;
                        else if (branch == Branch.MECHANICAL_ENGINEERING) meCount++;
                        else if (branch == Branch.ENERGY_ENGINEERING) eeCount++;
                    }

                    if (csCount >= 2 || ieCount >= 2 || meCount >= 2 || eeCount >= 2) {
                        System.out.println("TEAM WIN: 2+ linked trios (CS=" + csCount + ", IE=" + ieCount + ", ME=" + meCount + ", EE=" + eeCount + ")");
                        return members.get(0);
                    }
                }

                // Also check ECTS graduation (6+)
                if (difficulty == Difficulty.SIMPLE) {
                    // Also check ECTS graduation (6+)
                    if (team.hasGraduated()) {
                        System.out.println("TEAM WIN: 6+ ECTS");
                        return members.get(0);
                    }
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
