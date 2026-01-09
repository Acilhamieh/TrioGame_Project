package controller;

import model.*;
import enums.*;
import java.util.List;


public class TrioValidator {


    public static boolean validateTrio(Card card1, Card card2, Card card3, GameMode mode) {
        if (card1 == null || card2 == null || card3 == null) {
            return false;
        }

        // Create trio object
        Trio trio = new Trio(card1, card2, card3);

        // Use trio's built-in validation for the mode
        return trio.isValidForMode(mode);
    }


    public static boolean validateTrio(List<Card> cards, GameMode mode) {
        if (cards == null || cards.size() != 3) {
            return false;
        }

        return validateTrio(cards.get(0), cards.get(1), cards.get(2), mode);
    }

    public static boolean validateTrio(Trio trio, GameMode mode) {
        if (trio == null || mode == null) {
            return false;
        }

        return trio.isValidForMode(mode);
    }


    public static boolean areCardsMatching(Card card1, Card card2, Card card3) {
        if (card1 == null || card2 == null || card3 == null) {
            return false;
        }

        return card1.equals(card2) && card2.equals(card3);
    }

    /* Check if three cards are all from the same branch */
    public static boolean areSameBranch(Card card1, Card card2, Card card3) {
        if (card1 == null || card2 == null || card3 == null) {
            return false;
        }

        Branch b1 = card1.getBranch();
        Branch b2 = card2.getBranch();
        Branch b3 = card3.getBranch();

        return b1 == b2 && b2 == b3;
    }

    /* true if all three are PFE cards*/

    public static boolean isPFETrio(Card card1, Card card2, Card card3) {
        if (card1 == null || card2 == null || card3 == null) {
            return false;
        }

        return card1.isPFE() && card2.isPFE() && card3.isPFE();
    }

    /* Validate card selection for Simple mode: Just need 3 matching cards */


    public static boolean isValidSimpleMode(Card card1, Card card2, Card card3) {
        return areCardsMatching(card1, card2, card3);
    }

    /* Validate card selection for Advanced mode: 3 matching cards from the same branch */
    public static boolean isValidAdvancedMode(Card card1, Card card2, Card card3) {
        // Must match AND be from same branch
        return areCardsMatching(card1, card2, card3) && areSameBranch(card1, card2, card3);
    }

    /* Error message explaining why trio is invalid */
    public static String getValidationError(Card card1, Card card2, Card card3, GameMode mode) {
        if (card1 == null || card2 == null || card3 == null) {
            return "One or more cards are null";
        }

        // Check if cards match
        if (!areCardsMatching(card1, card2, card3)) {
            return "Cards don't match: " + card1.getCourseCode() + ", " +
                    card2.getCourseCode() + ", " + card3.getCourseCode();
        }

        // For advanced mode, check branch requirement
        if (mode.isAdvancedMode() && !areSameBranch(card1, card2, card3)) {
            return "Advanced mode requires all cards from same branch. Branches: " +
                    card1.getBranch() + ", " + card2.getBranch() + ", " + card3.getBranch();
        }

        return "Valid trio";
    }

    /* Calculate ECTS credits that would be awarded for this trio*/

    public static int calculateEcts(Card card1, Card card2, Card card3, GameMode mode) {
        if (!validateTrio(card1, card2, card3, mode)) {
            return 0;
        }

        Trio trio = new Trio(card1, card2, card3);
        return trio.calculateEcts(mode);
    }

    /* Provide detailed validation result */

    public static ValidationResult validateDetailed(Card card1, Card card2, Card card3, GameMode mode) {
        boolean isValid = validateTrio(card1, card2, card3, mode);
        int ects = isValid ? calculateEcts(card1, card2, card3, mode) : 0;
        String message = getValidationError(card1, card2, card3, mode);
        boolean isPFE = isPFETrio(card1, card2, card3);

        return new ValidationResult(isValid, ects, message, isPFE);
    }

    /* Inner class to hold validation results */
    public static class ValidationResult {
        private boolean valid;
        private int ectsAwarded;
        private String message;
        private boolean isPFETrio;

        public ValidationResult(boolean valid, int ectsAwarded, String message, boolean isPFETrio) {
            this.valid = valid;
            this.ectsAwarded = ectsAwarded;
            this.message = message;
            this.isPFETrio = isPFETrio;
        }

        public boolean isValid() {
            return valid;
        }

        public int getEctsAwarded() {
            return ectsAwarded;
        }

        public String getMessage() {
            return message;
        }

        public boolean isPFETrio() {
            return isPFETrio;
        }

        @Override
        public String toString() {
            return "Valid: " + valid + ", ECTS: " + ectsAwarded +
                    ", PFE: " + isPFETrio + ", Message: " + message;
        }
    }
}
