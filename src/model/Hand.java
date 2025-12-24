package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a student's hand of cards in Trio_UTBM.
 * Cards are kept sorted alphabetically by course code.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class Hand {
    private List<Card> cards;
    private Student owner;

    /**
     * Constructor for a hand
     * @param owner The student who owns this hand
     */
    public Hand(Student owner) {
        this.owner = owner;
        this.cards = new ArrayList<>();
    }

    /**
     * Add a card to the hand and sort
     * @param card The card to add
     */
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
            sortCards();
        }
    }

    /**
     * Remove a card from the hand
     * @param card The card to remove
     * @return true if card was removed successfully
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    /**
     * Remove a card at a specific index
     * @param index The index of the card to remove
     * @return The removed card, or null if index invalid
     */
    public Card removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.remove(index);
        }
        return null;
    }

    /**
     * Get a card at a specific index without removing it
     * @param index The index of the card
     * @return The card at that index, or null if invalid
     */
    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    /**
     * Sort cards alphabetically by course code
     */
    private void sortCards() {
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.getCourseCode().compareTo(c2.getCourseCode());
            }
        });
    }

    /**
     * Get the number of cards in hand
     * @return Number of cards
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Check if hand is empty
     * @return true if no cards in hand
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Get all cards in hand (defensive copy)
     * @return List of all cards
     */
    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Check if hand contains a specific card
     * @param card The card to check for
     * @return true if card is in hand
     */
    public boolean contains(Card card) {
        return cards.contains(card);
    }

    /**
     * Clear all cards from hand
     */
    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hand (").append(cards.size()).append(" cards): ");
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i).getCourseCode());
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
