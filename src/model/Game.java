package model;

import enums.Difficulty;
import enums.GameMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game manager for Trio_UTBM.
 * NEW RULES: Trio = 1 from your hand + 1 from neighbor's hand + 1 from lecture hall
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game with neighbor selection
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

    /**
     * Constructor for Game
     */
    public Game() {
        this.students = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.lectureHall = new LectureHall();
        this.scoreBoard = new ScoreBoard();
    }

    /**
     * Configure the game with players and settings
     * @param numPlayers Number of players (2-6)
     * @param mode Game mode
     * @param difficulty Difficulty level
     * @param playerNames List of player names
     */
    public void configure(int numPlayers, GameMode mode, Difficulty difficulty, List<String> playerNames) {
        this.numberOfPlayers = numPlayers;
        this.gameMode = mode;
        this.difficulty = difficulty;

        // Create students
        for (String name : playerNames) {
            Student student = new Student(name);
            students.add(student);
            scoreBoard.registerStudent(student);
        }

        // Create teams if team mode
        if (mode.isTeamMode()) {
            createTeams();
        }

        // Initialize turn manager
        this.turnManager = new TurnManager(students);
    }

    /**
     * Create teams for team mode
     */
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
        deck.shuffle();

        int cardsPerPlayer;
        int lectureHallSize;

        switch (numberOfPlayers) {
            case 3:
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
            case 2:
                cardsPerPlayer = 9;
                lectureHallSize = 9;
                break;
            default:
                cardsPerPlayer = 5;
                lectureHallSize = 9;
        }

        // Deal cards to players
        for (Student student : students) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = deck.dealCard();
                if (card != null) {
                    student.getHand().addCard(card);
                }
            }
        }

        // Fill lecture hall
        for (int i = 0; i < lectureHallSize; i++) {
            Card card = deck.dealCard();
            if (card != null) {
                lectureHall.addCard(card);
            }
        }
    }

    /**
     * Start the game
     */
    public void startGame() {
        System.out.println("Game started!");
        System.out.println("Mode: " + gameMode.getDisplayName());
        System.out.println("Players: " + numberOfPlayers);
    }

    /**
     * Get the neighbor (next player in turn order)
     * @param student Current player
     * @return The next player (neighbor)
     */
    public Student getNeighbor(Student student) {
        int currentIndex = students.indexOf(student);
        if (currentIndex == -1) return null;

        int neighborIndex = (currentIndex + 1) % students.size();
        return students.get(neighborIndex);
    }

    /**
     * Play a turn - NEW RULES!
     * Student selects 3 cards:
     * - 1 from OWN hand
     * - 1 from ANY OTHER PLAYER's hand
     * - 1 from LECTURE HALL
     *
     * @param student The student playing
     * @param otherPlayer The other player whose card was selected
     * @param selectedCards The 3 cards selected [0]=from own hand, [1]=from other player, [2]=from hall
     * @return true if turn was successful
     */
    public boolean playTurnWithPlayer(Student student, Student otherPlayer, List<Card> selectedCards) {
        if (selectedCards == null || selectedCards.size() != 3) {
            return false;
        }

        if (otherPlayer == null || otherPlayer.equals(student)) {
            System.out.println("ERROR: Invalid other player selection!");
            return false;
        }

        Card cardFromHand = selectedCards.get(0);
        Card cardFromOtherPlayer = selectedCards.get(1);
        Card cardFromHall = selectedCards.get(2);

        // CRITICAL VALIDATION: Verify cards are from correct sources
        if (!student.getHand().contains(cardFromHand)) {
            System.out.println("ERROR: Card " + cardFromHand.getCourseCode() + " not in player's hand!");
            return false;
        }

        if (!otherPlayer.getHand().contains(cardFromOtherPlayer)) {
            System.out.println("ERROR: Card " + cardFromOtherPlayer.getCourseCode() + " not in " + otherPlayer.getName() + "'s hand!");
            return false;
        }

        if (!lectureHall.contains(cardFromHall)) {
            System.out.println("ERROR: Card " + cardFromHall.getCourseCode() + " not in lecture hall!");
            return false;
        }

        // Validate trio
        Trio trio = new Trio(cardFromHand, cardFromOtherPlayer, cardFromHall);
        boolean isValid = validateTrio(trio);

        if (isValid) {
            // Calculate ECTS
            int ects = trio.calculateEcts(gameMode);

            // Award ECTS to student
            student.addEcts(ects);
            student.addCompletedTrio(trio);

            // Update scoreboard
            scoreBoard.updateScore(student, ects);
            scoreBoard.recordTrio(student, trio);

            // Update team if in team mode
            if (gameMode.isTeamMode() && student.getTeam() != null) {
                Team team = student.getTeam();
                team.addCompletedTrio(trio);
                scoreBoard.updateTeamScore(team, ects);
                scoreBoard.recordTeamTrio(team, trio);
            }

            // Remove cards: 1 from student's hand, 1 from other player's hand, 1 from lecture hall
            student.getHand().removeCard(cardFromHand);
            otherPlayer.getHand().removeCard(cardFromOtherPlayer);
            lectureHall.removeCard(cardFromHall);

            // Refill both player's and other player's hands
            refillPlayerHand(student);
            refillPlayerHand(otherPlayer);

            // Refill lecture hall
            refillLectureHall();

            // Give bonus turn if successful (don't advance turn)
            return true;
        }

        // Invalid trio - turn passes to next player
        turnManager.nextTurn();
        return false;
    }

    /**
     * Play a turn - LEGACY METHOD (for backward compatibility)
     * Assumes "other player" is the next neighbor
     */
    public boolean playTurn(Student student, List<Card> selectedCards) {
        Student neighbor = getNeighbor(student);
        return playTurnWithPlayer(student, neighbor, selectedCards);
    }

    /**
     * Validate if a trio meets the current game mode requirements
     * @param trio The trio to validate
     * @return true if valid
     */
    public boolean validateTrio(Trio trio) {
        return trio.isValidForMode(gameMode);
    }

    /**
     * Refill lecture hall to correct size based on player count
     */
    private void refillLectureHall() {
        int targetSize;

        switch (numberOfPlayers) {
            case 3:
                targetSize = 9;
                break;
            case 4:
                targetSize = 8;
                break;
            case 5:
            case 6:
                targetSize = 6;
                break;
            case 2:
                targetSize = 9;
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

    /**
     * Refill player's hand to correct size based on player count
     * @param student The student whose hand to refill
     */
    private void refillPlayerHand(Student student) {
        int targetSize;

        switch (numberOfPlayers) {
            case 3:
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
            case 2:
                targetSize = 9;
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
     * Check if any student or team has won
     * @return The winning student, or null if no winner yet
     */
    public Student checkVictoryConditions() {
        if (!gameMode.isTeamMode()) {
            for (Student student : students) {
                if (student.hasGraduated()) {
                    return student;
                }
            }
        } else {
            for (Team team : teams) {
                if (team.hasGraduated()) {
                    return team.getMembers().get(0);
                }
            }
        }
        return null;
    }

    /**
     * End the game and display results
     */
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

    // Getters

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
}
