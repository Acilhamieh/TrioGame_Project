package model;

import java.util.ArrayList;
import java.util.List;

public class HandPositionHelper {


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

    //Check if a specific position can be revealed

    public static boolean canRevealPosition(Hand hand, int position) {
        List<Integer> revealable = getRevealablePositions(hand);
        return revealable.contains(position);
    }

    //Get first revealable positions (for other players - only first or last)

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
