package model;

import enums.Branch;
import enums.GameMode;

/**
 * Represents a trio of three matching cards in Trio_UTBM.
 * A valid trio earns ECTS credits for the student/team.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class Trio {
    private Card card1;
    private Card card2;
    private Card card3;
    private int ectsAwarded;
    private Branch branch;

    /**
     * Constructor for a trio
     * @param card1 First card
     * @param card2 Second card
     * @param card3 Third card
     */
    public Trio(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.branch = card1.getBranch();
        this.ectsAwarded = 0;
    }

    /**
     * Check if this trio is valid (all three cards match)
     * @return true if all cards have the same course code
     */
    public boolean isValid() {
        return card1.equals(card2) && card2.equals(card3);
    }

    /**
     * Check if this trio is valid for the specified game mode
     * @param mode The game mode being played
     * @return true if trio meets mode requirements
     */
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

    /**
     * Check if this is a PFE trio (instant win)
     * @return true if all three cards are PFE
     */
    public boolean isPFETrio() {
        return card1.isPFE() && card2.isPFE() && card3.isPFE();
    }

    /**
     * Calculate ECTS credits for this trio based on game mode
     * @param mode The game mode being played
     * @return ECTS credits awarded (2, 3, or 6 for PFE)
     */
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

    /**
     * Get the first card
     * @return First card in trio
     */
    public Card getCard1() {
        return card1;
    }

    /**
     * Get the second card
     * @return Second card in trio
     */
    public Card getCard2() {
        return card2;
    }

    /**
     * Get the third card
     * @return Third card in trio
     */
    public Card getCard3() {
        return card3;
    }

    /**
     * Get the ECTS credits awarded for this trio
     * @return ECTS credits
     */
    public int getEctsAwarded() {
        return ectsAwarded;
    }

    /**
     * Get the branch of this trio
     * @return The branch
     */
    public Branch getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        return "Trio: " + card1.getCourseCode() + " + " +
                card2.getCourseCode() + " + " + card3.getCourseCode() +
                " (" + ectsAwarded + " ECTS)";
    }
}
