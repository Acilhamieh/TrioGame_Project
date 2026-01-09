package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Hand {
    private List<Card> cards;
    private Student owner;


    public Hand(Student owner) {
        this.owner = owner;
        this.cards = new ArrayList<>();
    }


    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
            sortCards();
        }
    }


    public boolean removeCard(Card card) {
        return cards.remove(card);
    }


    public Card removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.remove(index);
        }
        return null;
    }


    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    /*Sort cards by ID (HIGHEST to LOWEST: 12 → 1)
      This ensures predictable order for memory game
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

    // Get the number of cards in hand

    public int getSize() {
        return cards.size();
    }

    //Check if hand is empty

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Get all cards in hand (defensive copy)

    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    // Check if hand contains a specific card

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    //Clear all cards from hand

    public void clear() {
        cards.clear();
    }

    //Get the first card (highest ID) - always visible

    public Card getFirstCard() {
        return cards.isEmpty() ? null : cards.get(0);
    }

    //Get the last card (lowest ID) - always visible

    public Card getLastCard() {
        return cards.isEmpty() ? null : cards.get(cards.size() - 1);
    }

    public boolean isCardVisible(int index) {
        if (cards.isEmpty()) return false;
        return (index == 0) || (index == cards.size() - 1);
    }


    public boolean isPositionRevealable(int position) {
        return HandPositionHelper.canRevealPosition(this, position);
    }

    //NEW: Get all revealable positions (for highlighting UI)

    public List<Integer> getRevealablePositions() {
        return HandPositionHelper.getRevealablePositions(this);
    }


    public List<CardWithPosition> findDuplicates(String courseCode, int excludePosition) {
        List<CardWithPosition> duplicates = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            //  Skip the position that's already revealed
            if (i == excludePosition) {
                continue;
            }

            // Add other matching cards WITH their actual position
            // This fixes the indexOf() bug where it always returns the first match
            if (card.getCourseCode().equals(courseCode)) {
                duplicates.add(new CardWithPosition(card, i));
            }
        }
        return duplicates;
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
