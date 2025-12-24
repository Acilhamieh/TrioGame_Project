package model;

import model.enums.Branch;

/**
 * Represents a course credit card in the Trio_UTBM game.
 * Each card represents a UTBM course with a code and branch.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class Card {
    private String courseCode;
    private Branch branch;
    private boolean isPFE;

    /**
     * Constructor for a course card
     * @param courseCode The course code (e.g., "SY41", "IA41")
     * @param branch The engineering branch this course belongs to
     */
    public Card(String courseCode, Branch branch) {
        this.courseCode = courseCode;
        this.branch = branch;
        this.isPFE = courseCode.equals("PFE");
    }

    /**
     * Get the course code
     * @return The course code as a string
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Get the branch this card belongs to
     * @return The Branch enum value
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Check if this is a PFE (final project) card
     * @return true if this is a PFE card
     */
    public boolean isPFE() {
        return isPFE;
    }

    /**
     * Compare this card with another card for matching
     * Two cards match if they have the same course code
     * @param other The other card to compare with
     * @return true if cards match
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Card card = (Card) other;
        return courseCode.equals(card.courseCode);
    }

    @Override
    public String toString() {
        return courseCode + " (" + branch + ")";
    }
}
