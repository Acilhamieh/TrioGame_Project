package model;

import enums.Branch;
import enums.Difficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck {
    private List<Card> cards;
    private Difficulty difficulty;  // FIXED: Was "Difficult" - missing 'y'


    public Deck(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.cards = new ArrayList<>();
        initialize();
    }

    /**
      Initialize the deck with 36 cards with IDs:
      CORRECTED IDs:
      SY41 = 12, IA41 = 11, SY48 = 10, AP4B = 9
      GI21 = 8, PFE = 7, GI41 = 6
      MQ18 = 5, MQ41 = 4, MQ51 = 3
      EN21 = 2, GI28 = 1
      ALWAYS 36 cards (12 courses × 3 each)
      Difficulty ONLY affects validation, NOT card distribution!
     */
    private void initialize() {
        // Computer Science courses (IDs: 12, 11, 10, 9)
        addCourseCards("SY41", Branch.COMPUTER_SCIENCE, 12, 3);
        addCourseCards("IA41", Branch.COMPUTER_SCIENCE, 11, 3);
        addCourseCards("SY48", Branch.COMPUTER_SCIENCE, 10, 3);
        addCourseCards("AP4B", Branch.COMPUTER_SCIENCE, 9, 3);

        // Industrial Engineering courses (IDs: 8, 6, 1)
        addCourseCards("GI21", Branch.INDUSTRIAL_ENGINEERING, 8, 3);
        addCourseCards("GI41", Branch.INDUSTRIAL_ENGINEERING, 6, 3);
        addCourseCards("GI28", Branch.INDUSTRIAL_ENGINEERING, 1, 3); // CORRECTED: GI28 = 1

        // Mechanical Engineering courses (IDs: 5, 4, 3)
        addCourseCards("MQ18", Branch.MECHANICAL_ENGINEERING, 5, 3);
        addCourseCards("MQ41", Branch.MECHANICAL_ENGINEERING, 4, 3);
        addCourseCards("MQ51", Branch.MECHANICAL_ENGINEERING, 3, 3);

        // Energy Engineering courses (ID: 2)
        addCourseCards("EN21", Branch.ENERGY_ENGINEERING, 2, 3);

        // PFE - Final Project (ID: 7) - CORRECTED: PFE = 7 = "The 7"
        addCourseCards("PFE", Branch.SPECIAL, 7, 3);
    }

    /* Helper method to add multiple cards of the same course with ID
     */
    private void addCourseCards(String courseCode, Branch branch, int id, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new Card(courseCode, branch, id));
        }
    }

    /* Shuffle the deck randomly */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /* Deal one card from the deck
     */
    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    /* Get the number of cards remaining in the deck
     */
    public int getRemainingCount() {
        return cards.size();
    }

    /* Check if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /* Get all remaining cards (for testing/display)
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
}
