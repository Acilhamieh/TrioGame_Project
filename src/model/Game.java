package model;

import model.enums.Difficulty;
import model.enums.GameMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game manager for Trio_UTBM.
 * Coordinates all game components and manages game flow.
 *
 * @author Acil HAMIEH
 * @version 1.0
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
     * @param numPlayers Number of players (3-4)
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
     * Pairs students into teams
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
        // Create and shuffle deck
        deck = new Deck(difficulty);
        deck.shuffle();

        // Deal cards to players
        int cardsPerPlayer = (numberOfPlayers == 3) ? 5 : 4;
        for (Student student : students) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = deck.dealCard();
                if (card != null) {
                    student.getHand().addCard(card);
                }
            }
        }

        // Fill lecture hall with 9 cards
        for (int i = 0; i < 9; i++) {
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
     * Play a turn - student selects 3 cards to form a trio
     * @param student The student playing
     * @param selectedCards The 3 cards selected (2 from hand, 1 from hall)
     * @return true if turn was successful
     */
    public boolean playTurn(Student student, List<Card> selectedCards) {
        if (selectedCards == null || selectedCards.size() != 3) {
            return false;
        }

        // Validate trio
        Trio trio = new Trio(selectedCards.get(0), selectedCards.get(1), selectedCards.get(2));
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

            // Remove cards from hand and lecture hall
            for (Card card : selectedCards) {
                student.getHand().removeCard(card);
                lectureHall.removeCard(card);
            }

            // Replace cards in lecture hall
            refillLectureHall();

            // Give bonus turn if successful (don't advance turn)
            return true;
        }

        // Invalid trio - turn passes to next player
        turnManager.nextTurn();
        return false;
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
     * Refill lecture hall to 9 cards after cards are removed
     */
    private void refillLectureHall() {
        while (!lectureHall.isFull() && !deck.isEmpty()) {
            Card card = deck.dealCard();
            if (card != null) {
                lectureHall.addCard(card);
            }
        }
    }

    /**
     * Check if any student or team has won
     * @return The winning student, or null if no winner yet
     */
    public Student checkVictoryConditions() {
        // Check individual mode
        if (!gameMode.isTeamMode()) {
            for (Student student : students) {
                if (student.hasGraduated()) {
                    return student;
                }
            }
        } else {
            // Check team mode
            for (Team team : teams) {
                if (team.hasGraduated()) {
                    // Return first member of winning team
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
