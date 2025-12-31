package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to determine which card positions can be revealed.
 * Rules: First card + consecutive duplicates, OR Last card + consecutive duplicates from end
 *
 * Example: Hand [2, 2, 2, 5, 7]
 * - Can reveal: positions 0,1,2 (all three 2's) OR position 4 (the 7)
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0 - First/Last + Duplicates logic
 */
public class HandPositionHelper {

    /**
     * Get all positions that can be revealed from a hand
     * @param hand The player's hand
     * @return List of revealable positions
     */
    public static List<Integer> getRevealablePositions(Hand hand) {
        List<Integer> positions = new ArrayList<>();
        List<Card> cards = hand.getAllCards();

        if (cards.isEmpty()) {
            return positions;
        }

        // FIRST card + consecutive duplicates
        Card firstCard = cards.get(0);
        positions.add(0); // First is always revealable

        // Add consecutive duplicates from start
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getId() == firstCard.getId()) {
                positions.add(i);
            } else {
                break; // Stop at first different card
            }
        }

        // LAST card + consecutive duplicates from end (if not already added)
        int lastIndex = cards.size() - 1;
        if (!positions.contains(lastIndex)) {
            Card lastCard = cards.get(lastIndex);
            positions.add(lastIndex);

            // Add consecutive duplicates from end
            for (int i = lastIndex - 1; i >= 0; i--) {
                if (cards.get(i).getId() == lastCard.getId()) {
                    if (!positions.contains(i)) {
                        positions.add(i);
                    }
                } else {
                    break; // Stop at first different card
                }
            }
        }

        return positions;
    }

    /**
     * Check if a specific position can be revealed
     * @param hand The player's hand
     * @param position The position to check
     * @return true if position can be revealed
     */
    public static boolean canRevealPosition(Hand hand, int position) {
        List<Integer> revealable = getRevealablePositions(hand);
        return revealable.contains(position);
    }

    /**
     * Get first revealable positions (for other players - only first or last)
     * @param hand The player's hand
     * @return List containing only first and last positions
     */
    public static List<Integer> getFirstLastPositions(Hand hand) {
        List<Integer> positions = new ArrayList<>();
        List<Card> cards = hand.getAllCards();

        if (cards.isEmpty()) {
            return positions;
        }

        positions.add(0); // First

        int lastIndex = cards.size() - 1;
        if (lastIndex > 0) {
            positions.add(lastIndex); // Last (if different from first)
        }

        return positions;
    }
}
