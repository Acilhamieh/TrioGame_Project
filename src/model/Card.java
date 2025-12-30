package model;

import enums.Branch;

/**
 * Represents a course card in Trio_UTBM.
 * Each card has a course code, branch, and unique ID for sorting.
 *
 * BACKWARD COMPATIBLE: Supports both old and new constructors
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 2.1 - Backward compatible with ID system
 */
public class Card {
    private final String courseCode;
    private final Branch branch;
    private final int id;

    /**
     * NEW Constructor with ID (for memory game)
     * @param courseCode The course code (e.g., "AP4B", "SY41")
     * @param branch The engineering branch
     * @param id The card ID for sorting (1-12)
     */
    public Card(String courseCode, Branch branch, int id) {
        this.courseCode = courseCode;
        this.branch = branch;
        this.id = id;
    }

    /**
     * OLD Constructor WITHOUT ID (backward compatible)
     * Automatically assigns ID based on course code
     * @param courseCode The course code
     * @param branch The engineering branch
     */
    public Card(String courseCode, Branch branch) {
        this.courseCode = courseCode;
        this.branch = branch;
        this.id = getIdFromCourseCode(courseCode);
    }

    /**
     * Auto-assign ID based on course code (for backward compatibility)
     * @param courseCode The course code
     * @return The corresponding ID (1-12)
     */
    private static int getIdFromCourseCode(String courseCode) {
        switch (courseCode.toUpperCase()) {
            case "SY41": return 12;
            case "IA41": return 11;
            case "SY48": return 10;
            case "AP4B": return 9;
            case "GI21": return 8;
            case "GI28": return 7;
            case "GI41": return 6;
            case "MQ18": return 5;
            case "MQ41": return 4;
            case "MQ51": return 3;
            case "EN21": return 2;
            case "PFE":  return 1;
            default:     return 0;
        }
    }

    /**
     * Get the course code
     * @return Course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Get the branch
     * @return Branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Get the card ID for sorting
     * @return Card ID (1-12)
     */
    public int getId() {
        return id;
    }

    /**
     * Check if this is a PFE card (for old tests)
     * @return true if this is a PFE card
     */
    public boolean isPFE() {
        return "PFE".equalsIgnoreCase(courseCode);
    }

    /**
     * Check if this card matches another card (same course code)
     * @param other The other card to compare
     * @return true if course codes match
     */
    public boolean matches(Card other) {
        if (other == null) return false;
        return this.courseCode.equals(other.getCourseCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return courseCode.equals(card.courseCode) && branch == card.branch;
    }

    @Override
    public int hashCode() {
        int result = courseCode.hashCode();
        result = 31 * result + branch.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return courseCode + " (ID:" + id + ")";
    }

    /**
     * Get display string with branch info
     * @return Formatted string
     */
    public String toDisplayString() {
        return courseCode + "\nID:" + id + "\n" + branch.name();
    }
}
