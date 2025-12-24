package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Lecture Hall - the central area with 9 visible cards.
 * Players select cards from here to form trios.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class LectureHall {
    private List<Card> visibleCards;
    private static final int MAX_CAPACITY = 9;

    /**
     * Constructor for Lecture Hall
     * Initializes with empty card list
     */
    public LectureHall() {
        this.visibleCards = new ArrayList<>();
    }

    /**
     * Add a card to the lecture hall
     * @param card The card to add
     * @return true if card was added successfully, false if hall is full
     */
    public boolean addCard(Card card) {
        if (card != null && visibleCards.size() < MAX_CAPACITY) {
            visibleCards.add(card);
            return true;
        }
        return false;
    }

    /**
     * Remove a card from the lecture hall
     * @param card The card to remove
     * @return true if card was removed successfully
     */
    public boolean removeCard(Card card) {
        return visibleCards.remove(card);
    }

    /**
     * Remove a card at a specific index
     * @param index The index of the card to remove
     * @return The removed card, or null if index invalid
     */
    public Card removeCard(int index) {
        if (index >= 0 && index < visibleCards.size()) {
            return visibleCards.remove(index);
        }
        return null;
    }

    /**
     * Get a card at a specific index without removing it
     * @param index The index of the card
     * @return The card at that index, or null if invalid
     */
    public Card getCard(int index) {
        if (index >= 0 && index < visibleCards.size()) {
            return visibleCards.get(index);
        }
        return null;
    }

    /**
     * Get all visible cards (defensive copy)
     * @return List of all visible cards
     */
    public List<Card> getAllCards() {
        return new ArrayList<>(visibleCards);
    }

    /**
     * Get the number of cards currently visible
     * @return Number of cards
     */
    public int getCardCount() {
        return visibleCards.size();
    }

    /**
     * Check if the lecture hall is full
     * @return true if 9 cards are present
     */
    public boolean isFull() {
        return visibleCards.size() >= MAX_CAPACITY;
    }

    /**
     * Check if the lecture hall is empty
     * @return true if no cards present
     */
    public boolean isEmpty() {
        return visibleCards.isEmpty();
    }

    /**
     * Get the maximum capacity
     * @return Maximum number of cards (9)
     */
    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    /**
     * Clear all cards from lecture hall
     */
    public void clear() {
        visibleCards.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lecture Hall (").append(visibleCards.size()).append("/").append(MAX_CAPACITY).append("):\n");
        for (int i = 0; i < visibleCards.size(); i++) {
            sb.append("[").append(i).append("] ").append(visibleCards.get(i).getCourseCode());
            if ((i + 1) % 3 == 0) {
                sb.append("\n");
            } else {
                sb.append("  ");
            }
        }
        return sb.toString();
    }
}
