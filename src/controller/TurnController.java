package controller;

import model.*;
import enums.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the execution of individual turns.
 * Manages card selection, trio formation, and turn progression.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class TurnController {
    private Game game;

    /**
     * Constructor for TurnController
     * @param game The game instance
     */
    public TurnController(Game game) {
        this.game = game;
    }

    /**
     * Execute a turn with selected cards
     * @param cardIndices Array of 3 indices: [handIndex1, handIndex2, hallIndex]
     * @return true if turn was successful
     */
    public boolean executeTurn(int[] cardIndices) {
        if (cardIndices == null || cardIndices.length != 3) {
            System.out.println("❌ Error: Must select exactly 3 cards");
            return false;
        }

        Student currentPlayer = game.getTurnManager().getCurrentStudent();

        // Extract indices
        int handIndex1 = cardIndices[0];
        int handIndex2 = cardIndices[1];
        int hallIndex = cardIndices[2];

        // Validate indices
        if (!validateIndices(currentPlayer, handIndex1, handIndex2, hallIndex)) {
            return false;
        }

        // Get the actual cards
        Card handCard1 = currentPlayer.getHand().getCard(handIndex1);
        Card handCard2 = currentPlayer.getHand().getCard(handIndex2);
        Card hallCard = game.getLectureHall().getCard(hallIndex);

        if (handCard1 == null || handCard2 == null || hallCard == null) {
            System.out.println("❌ Error: Invalid card selection");
            return false;
        }

        // Create list of selected cards
        List<Card> selectedCards = new ArrayList<>();
        selectedCards.add(handCard1);
        selectedCards.add(handCard2);
        selectedCards.add(hallCard);

        // Display selection
        System.out.println("\n📋 Selected cards:");
        System.out.println("  From hand: " + handCard1.getCourseCode() + ", " + handCard2.getCourseCode());
        System.out.println("  From hall: " + hallCard.getCourseCode());

        // Attempt to play the turn
        boolean success = game.playTurn(currentPlayer, selectedCards);

        if (success) {
            System.out.println("✅ Valid trio! Turn successful!");
            // Player gets bonus turn, don't advance
        } else {
            System.out.println("❌ Invalid trio. Turn passes to next player.");
            advanceTurn();
        }

        return success;
    }

    /**
     * Validate the selected card indices
     * @param player The current player
     * @param handIndex1 First hand card index
     * @param handIndex2 Second hand card index
     * @param hallIndex Lecture hall card index
     * @return true if all indices are valid
     */
    private boolean validateIndices(Student player, int handIndex1, int handIndex2, int hallIndex) {
        // Validate hand indices
        int handSize = player.getHand().getSize();
        if (handIndex1 < 0 || handIndex1 >= handSize) {
            System.out.println("❌ Error: Hand index 1 out of range (0-" + (handSize-1) + ")");
            return false;
        }
        if (handIndex2 < 0 || handIndex2 >= handSize) {
            System.out.println("❌ Error: Hand index 2 out of range (0-" + (handSize-1) + ")");
            return false;
        }
        if (handIndex1 == handIndex2) {
            System.out.println("❌ Error: Cannot select the same hand card twice");
            return false;
        }

        // Validate lecture hall index
        int hallSize = game.getLectureHall().getCardCount();
        if (hallIndex < 0 || hallIndex >= hallSize) {
            System.out.println("❌ Error: Hall index out of range (0-" + (hallSize-1) + ")");
            return false;
        }

        return true;
    }

    /**
     * Advance to the next player's turn
     */
    public void advanceTurn() {
        game.getTurnManager().nextTurn();
        Student nextPlayer = game.getTurnManager().getCurrentStudent();

        System.out.println("\n🔄 Turn passed to: " + nextPlayer.getName());
        System.out.println("Round: " + game.getTurnManager().getRoundNumber());
    }

    /**
     * Display current player's hand
     */
    public void displayCurrentHand() {
        Student currentPlayer = game.getTurnManager().getCurrentStudent();
        Hand hand = currentPlayer.getHand();

        System.out.println("\n🤚 " + currentPlayer.getName() + "'s Hand:");
        List<Card> cards = hand.getAllCards();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            System.out.println("  [" + i + "] " + card.getCourseCode() + " (" + card.getBranch() + ")");
        }
    }

    /**
     * Display the lecture hall cards
     */
    public void displayLectureHall() {
        LectureHall hall = game.getLectureHall();

        System.out.println("\n🏛️  Lecture Hall:");
        List<Card> cards = hall.getAllCards();

        // Display in 3x3 grid
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            System.out.print("  [" + i + "] " + card.getCourseCode());

            if ((i + 1) % 3 == 0) {
                System.out.println();
            } else {
                System.out.print("  ");
            }
        }
        System.out.println();
    }

    /**
     * Display current scores
     */
    public void displayScores() {
        System.out.println("\n📊 Current Scores:");

        if (game.getGameMode().isTeamMode()) {
            // Team mode
            for (Team team : game.getTeams()) {
                System.out.println("  " + team.getTeamName() + ": " + team.getTeamScore() + " ECTS");
                for (Student member : team.getMembers()) {
                    System.out.println("    - " + member.getName() + ": " +
                            member.getEctsCredits() + " ECTS");
                }
            }
        } else {
            // Individual mode
            for (Student student : game.getStudents()) {
                System.out.println("  " + student.getName() + ": " +
                        student.getEctsCredits() + " ECTS (" +
                        student.getTrioCount() + " trios)");
            }
        }
    }

    /**
     * Get a summary of the current turn state
     * @return Turn state information
     */
    public String getTurnInfo() {
        Student currentPlayer = game.getTurnManager().getCurrentStudent();

        StringBuilder info = new StringBuilder();
        info.append("Current Player: ").append(currentPlayer.getName()).append("\n");
        info.append("ECTS: ").append(currentPlayer.getEctsCredits()).append("/6\n");
        info.append("Trios: ").append(currentPlayer.getTrioCount()).append("\n");
        info.append("Hand Size: ").append(currentPlayer.getHand().getSize()).append("\n");

        return info.toString();
    }
}
