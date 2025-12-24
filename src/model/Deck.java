package model;

import enums.Branch;
import enums.Difficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of 36 cards in Trio_UTBM.
 * Contains UTBM courses distributed across branches.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class Deck {
    private List<Card> cards;
    private Difficulty difficulty;

    /**
     * Constructor for the deck
     * @param difficulty The difficulty level (affects gameplay, not deck composition)
     */
    public Deck(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.cards = new ArrayList<>();
        initialize();
    }

    /**
     * Initialize the deck with 36 cards according to game rules:
     * - 11 different courses, each appearing 3 times
     * - PFE appears 3 times
     * Total: 36 cards
     */
    private void initialize() {
        // Computer Science courses (3 cards each)
        addCourseCards("SY41", Branch.COMPUTER_SCIENCE, 3);
        addCourseCards("IA41", Branch.COMPUTER_SCIENCE, 3);
        addCourseCards("SY48", Branch.COMPUTER_SCIENCE, 3);
        addCourseCards("AP4B", Branch.COMPUTER_SCIENCE, 3);

        // Industrial Engineering courses (3 cards each)
        addCourseCards("GI40", Branch.INDUSTRIAL_ENGINEERING, 3);
        addCourseCards("QO40", Branch.INDUSTRIAL_ENGINEERING, 3);

        // Mechanical Engineering courses (3 cards each)
        addCourseCards("MQ40", Branch.MECHANICAL_ENGINEERING, 3);
        addCourseCards("TN40", Branch.MECHANICAL_ENGINEERING, 3);

        // Energy Engineering courses (3 cards each)
        addCourseCards("ER40", Branch.ENERGY_ENGINEERING, 3);
        addCourseCards("TF40", Branch.ENERGY_ENGINEERING, 3);

        // Mixed/General courses (3 cards each)
        addCourseCards("MT40", Branch.MECHANICAL_ENGINEERING, 3);

        // PFE - Final Project (3 cards)
        addCourseCards("PFE", Branch.SPECIAL, 3);
    }

    /**
     * Helper method to add multiple cards of the same course
     * @param courseCode The course code
     * @param branch The branch
     * @param count Number of cards to add
     */
    private void addCourseCards(String courseCode, Branch branch, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new Card(courseCode, branch));
        }
    }

    /**
     * Shuffle the deck randomly
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Deal one card from the deck
     * @return The top card, or null if deck is empty
     */
    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    /**
     * Get the number of cards remaining in the deck
     * @return Number of cards left
     */
    public int getRemainingCount() {
        return cards.size();
    }

    /**
     * Check if the deck is empty
     * @return true if no cards remain
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Get all remaining cards (for testing/display)
     * @return List of remaining cards
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
}
