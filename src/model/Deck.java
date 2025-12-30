package model;

import enums.Branch;
import enums.Difficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of 36 cards in Trio_UTBM.
 * Contains 12 different courses, each with unique ID (1-12).
 *
 * @author Dana SLEIMAN
 * @version 2.0 - Added ID system
 */
public class Deck {
    private List<Card> cards;
    private Difficulty difficulty;

    /**
     * Constructor for the deck
     * @param difficulty The difficulty level
     */
    public Deck(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.cards = new ArrayList<>();
        initialize();
    }

    /**
     * Initialize the deck with 36 cards with IDs:
     * SY41 → ID: 12, IA41 → ID: 11, SY48 → ID: 10, AP4B → ID: 9
     * GI21 → ID: 8, GI28 → ID: 7, GI41 → ID: 6
     * MQ18 → ID: 5, MQ41 → ID: 4, MQ51 → ID: 3
     * EN21 → ID: 2, PFE → ID: 1
     *
     * Each course appears 3 times, total 36 cards
     */
    private void initialize() {
        // Computer Science courses (IDs: 12, 11, 10, 9)
        addCourseCards("SY41", Branch.COMPUTER_SCIENCE, 12, 3);
        addCourseCards("IA41", Branch.COMPUTER_SCIENCE, 11, 3);
        addCourseCards("SY48", Branch.COMPUTER_SCIENCE, 10, 3);
        addCourseCards("AP4B", Branch.COMPUTER_SCIENCE, 9, 3);

        // Industrial Engineering courses (IDs: 8, 7, 6)
        addCourseCards("GI21", Branch.INDUSTRIAL_ENGINEERING, 8, 3);
        addCourseCards("GI28", Branch.INDUSTRIAL_ENGINEERING, 7, 3);
        addCourseCards("GI41", Branch.INDUSTRIAL_ENGINEERING, 6, 3);

        // Mechanical Engineering courses (IDs: 5, 4, 3)
        addCourseCards("MQ18", Branch.MECHANICAL_ENGINEERING, 5, 3);
        addCourseCards("MQ41", Branch.MECHANICAL_ENGINEERING, 4, 3);
        addCourseCards("MQ51", Branch.MECHANICAL_ENGINEERING, 3, 3);

        // Energy Engineering courses (ID: 2)
        addCourseCards("EN21", Branch.ENERGY_ENGINEERING, 2, 3);

        // PFE - Final Project (ID: 1)
        addCourseCards("PFE", Branch.SPECIAL, 1, 3);
    }

    /**
     * Helper method to add multiple cards of the same course with ID
     * @param courseCode The course code
     * @param branch The branch
     * @param id The card ID (1-12)
     * @param count Number of cards to add (should be 3)
     */
    private void addCourseCards(String courseCode, Branch branch, int id, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new Card(courseCode, branch, id));
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
