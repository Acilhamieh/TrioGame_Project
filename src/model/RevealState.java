package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the state of revealed cards during a player's turn.
 * In the revealing game mode, players reveal cards one by one.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0 - Revealing system
 */
public class RevealState {
    private List<RevealedCard> revealedCards;
    private int maxReveals = 3;

    /**
     * Inner class to track a single revealed card
     */
    public static class RevealedCard {
        public Card card;
        public String source; // "hand", "other_player", "hall"
        public String playerName; // For other_player source
        public int position;

        public RevealedCard(Card card, String source, String playerName, int position) {
            this.card = card;
            this.source = source;
            this.playerName = playerName;
            this.position = position;
        }
    }

    /**
     * Constructor
     */
    public RevealState() {
        this.revealedCards = new ArrayList<>();
    }

    /**
     * Add a revealed card
     * @param card The card
     * @param source Source location
     * @param playerName Player name (if from other player)
     * @param position Position in hand/hall
     * @return true if added successfully
     */
    public boolean addReveal(Card card, String source, String playerName, int position) {
        if (revealedCards.size() >= maxReveals) {
            return false;
        }
        revealedCards.add(new RevealedCard(card, source, playerName, position));
        return true;
    }

    /**
     * Get all revealed cards
     * @return List of revealed cards
     */
    public List<RevealedCard> getRevealedCards() {
        return new ArrayList<>(revealedCards);
    }

    /**
     * Get number of revealed cards
     * @return Count
     */
    public int getRevealCount() {
        return revealedCards.size();
    }

    /**
     * Check if all revealed cards match (form a trio)
     * @return true if all cards have same course code
     */
    public boolean isValidTrio() {
        if (revealedCards.size() != 3) {
            return false;
        }

        Card first = revealedCards.get(0).card;
        Card second = revealedCards.get(1).card;
        Card third = revealedCards.get(2).card;

        return first.matches(second) && second.matches(third);
    }

    /**
     * Check if last two revealed cards are different
     * @return true if mismatch detected
     */
    public boolean hasMismatch() {
        if (revealedCards.size() < 2) {
            return false;
        }

        // Check last two cards
        int size = revealedCards.size();
        Card last = revealedCards.get(size - 1).card;
        Card secondLast = revealedCards.get(size - 2).card;

        return !last.matches(secondLast);
    }

    /**
     * Get the last revealed card
     * @return Last card or null
     */
    public RevealedCard getLastRevealed() {
        if (revealedCards.isEmpty()) {
            return null;
        }
        return revealedCards.get(revealedCards.size() - 1);
    }

    /**
     * Clear all revealed cards
     */
    public void clear() {
        revealedCards.clear();
    }

    /**
     * Check if can reveal more cards
     * @return true if less than 3 cards revealed
     */
    public boolean canRevealMore() {
        return revealedCards.size() < maxReveals;
    }

    /**
     * Get course code of revealed cards (for display)
     * @return Course code or null
     */
    public String getRevealedCourseCode() {
        if (revealedCards.isEmpty()) {
            return null;
        }
        return revealedCards.get(0).card.getCourseCode();
    }
}
