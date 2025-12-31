package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a student's hand of cards in Trio_UTBM.
 * Cards are kept sorted by ID (HIGHEST to LOWEST: 12 → 1).
 *
 * @author Dana SLEIMAN
 * @version 2.1 - Added revealing position support
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
     * Add a card to the hand and sort by ID (highest to lowest)
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
     * Sort cards by ID (HIGHEST to LOWEST: 12 → 1)
     * This ensures predictable order for memory game
     */
    private void sortCards() {
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                // Sort descending: highest ID first
                return Integer.compare(c2.getId(), c1.getId());
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
     * @return List of all cards (sorted by ID)
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

    /**
     * Get the first card (highest ID) - always visible
     * @return First card or null if empty
     */
    public Card getFirstCard() {
        return cards.isEmpty() ? null : cards.get(0);
    }

    /**
     * Get the last card (lowest ID) - always visible
     * @return Last card or null if empty
     */
    public Card getLastCard() {
        return cards.isEmpty() ? null : cards.get(cards.size() - 1);
    }

    /**
     * Check if a card at position should be visible (OLD METHOD - for display only)
     * Only first and last cards are visible in memory game mode
     * @param index The position (0-based)
     * @return true if should be visible
     */
    public boolean isCardVisible(int index) {
        if (cards.isEmpty()) return false;
        return (index == 0) || (index == cards.size() - 1);
    }

    /**
     * NEW: Check if a position can be revealed (for clicking)
     * Uses HandPositionHelper to determine first/last + duplicates
     * @param position The position to check
     * @return true if position can be revealed
     */
    public boolean isPositionRevealable(int position) {
        return HandPositionHelper.canRevealPosition(this, position);
    }

    /**
     * NEW: Get all revealable positions (for highlighting UI)
     * @return List of positions that can be revealed
     */
    public List<Integer> getRevealablePositions() {
        return HandPositionHelper.getRevealablePositions(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hand (").append(cards.size()).append(" cards): ");
        for (int i = 0; i < cards.size(); i++) {
            if (isCardVisible(i)) {
                sb.append(cards.get(i).toString());
            } else {
                sb.append("[?]");
            }
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
