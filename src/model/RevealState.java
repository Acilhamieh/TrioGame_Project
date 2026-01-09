package model;

import java.util.ArrayList;
import java.util.List;


public class RevealState {
    private List<RevealedCard> revealedCards;
    private int maxReveals = 3;

    // Inner class to track a single revealed card

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


    public RevealState() {
        this.revealedCards = new ArrayList<>();
    }

    //Add a revealed card Prevents revealing the same card (source + position) twice

    public boolean addReveal(Card card, String source, String playerName, int position) {
        if (revealedCards.size() >= maxReveals) {
            return false;
        }

        //  CRITICAL FIX: Check for duplicate (same source + position)
        for (RevealedCard existing : revealedCards) {
            // Check if same source
            if (!existing.source.equals(source)) {
                continue;
            }

            // For hand and hall, check position only
            if (source.equals("hand") || source.equals("hall")) {
                if (existing.position == position) {
                    System.out.println("WARNING: Duplicate reveal blocked - same " + source + " position " + position);
                    return false; // Don't add duplicate!
                }
            }
            // For other_player, check player name AND position
            else if (source.equals("other_player")) {
                if (existing.playerName != null &&
                        existing.playerName.equals(playerName) &&
                        existing.position == position) {
                    System.out.println("WARNING: Duplicate reveal blocked - same card from " + playerName + " position " + position);
                    return false; // Don't add duplicate!
                }
            }
        }

        // No duplicate found, add the card
        revealedCards.add(new RevealedCard(card, source, playerName, position));
        return true;
    }

    // Get all revealed cards

    public List<RevealedCard> getRevealedCards() {
        return new ArrayList<>(revealedCards);
    }

    //Get number of revealed cards

    public int getRevealCount() {
        return revealedCards.size();
    }

    //Check if all revealed cards match (form a trio)

    public boolean isValidTrio() {
        if (revealedCards.size() != 3) {
            return false;
        }

        Card first = revealedCards.get(0).card;
        Card second = revealedCards.get(1).card;
        Card third = revealedCards.get(2).card;

        return first.matches(second) && second.matches(third);
    }

    //Check if last two revealed cards are different

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

    // Get the last revealed card

    public RevealedCard getLastRevealed() {
        if (revealedCards.isEmpty()) {
            return null;
        }
        return revealedCards.get(revealedCards.size() - 1);
    }

    //Clear all revealed cards

    public void clear() {
        revealedCards.clear();
    }

    //check if can reveal more cards

    public boolean canRevealMore() {
        return revealedCards.size() < maxReveals;
    }

    //Get course code of revealed cards (for display)

    public String getRevealedCourseCode() {
        if (revealedCards.isEmpty()) {
            return null;
        }
        return revealedCards.get(0).card.getCourseCode();
    }
}
