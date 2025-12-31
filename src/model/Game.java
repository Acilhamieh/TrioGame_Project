package model;

import enums.Difficulty;
import enums.GameMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game manager for Trio_UTBM.
 * FLEXIBLE RULES: Take ANY 3 cards from anywhere!
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.0 - Flexible card selection
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
     */
    public void configure(int numPlayers, GameMode mode, Difficulty difficulty, List<String> playerNames) {
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

        for (Student student : students) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = deck.dealCard();
                if (card != null) {
                    student.getHand().addCard(card);
                }
            }
        }

        for (int i = 0; i < lectureHallSize; i++) {
            Card card = deck.dealCard();
            if (card != null) {
                lectureHall.addCard(card);
            }
        }
    }

    public void startGame() {
        System.out.println("Game started!");
        System.out.println("Mode: " + gameMode.getDisplayName());
        System.out.println("Players: " + numberOfPlayers);
    }

    public Student getNeighbor(Student student) {
        int currentIndex = students.indexOf(student);
        if (currentIndex == -1) return null;
        int neighborIndex = (currentIndex + 1) % students.size();
        return students.get(neighborIndex);
    }

    /**
     * NEW: Play flexible turn - take ANY 3 cards from anywhere!
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
