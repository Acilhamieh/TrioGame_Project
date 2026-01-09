package model;

import enums.Branch;
import enums.GameMode;


public class Trio {
    private Card card1;
    private Card card2;
    private Card card3;
    private int ectsAwarded;


    public Trio(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.ectsAwarded = 0;
    }

    // Check if this trio is valid (all three cards match)

    public boolean isValid() {
        return card1.equals(card2) && card2.equals(card3);
    }

    public boolean isValidForMode(GameMode mode) {
        // First check basic validity (3 matching cards)
        if (!isValid()) {
            return false;
        }

        // Advanced modes require all cards from same branch
        if (mode.isAdvancedMode()) {
            Branch b1 = card1.getBranch();
            Branch b2 = card2.getBranch();
            Branch b3 = card3.getBranch();
            return b1 == b2 && b2 == b3;
        }

        // Simple modes accept any valid trio
        return true;
    }

    //Check if this is a PFE trio (instant win)

    public boolean isPFETrio() {
        return card1.isPFE() && card2.isPFE() && card3.isPFE();
    }

    //Calculate ECTS credits for this trio based on game mode

    public int calculateEcts(GameMode mode) {
        if (isPFETrio()) {
            this.ectsAwarded = 6; // PFE trio = instant graduation
            return 6;
        }

        if (isValidForMode(mode)) {
            this.ectsAwarded = mode.getEctsPerTrio();
            return mode.getEctsPerTrio();
        }

        this.ectsAwarded = 0;
        return 0;
    }

    //Get the first card

    public Card getCard1() {
        return card1;
    }

    //Get the second card

    public Card getCard2() {
        return card2;
    }

    //Get the third card

    public Card getCard3() {
        return card3;
    }

    //Get the ECTS credits awarded for this trio

    public int getEctsAwarded() {
        return ectsAwarded;
    }


    public Branch getBranch() {
        // Return the actual branch from card1
        // (all three cards have same branch in a valid trio)
        return card1.getBranch();
    }

    @Override
    public String toString() {
        return "Trio: " + card1.getCourseCode() + " + " +
                card2.getCourseCode() + " + " + card3.getCourseCode() +
                " (" + ectsAwarded + " ECTS)";
    }
}
