package model;

import enums.Branch;

public class Card {
    private final String courseCode;
    private final Branch branch;
    private final int id;

    public Card(String courseCode, Branch branch, int id) {
        this.courseCode = courseCode;
        this.branch = branch;
        this.id = id;
    }


    public Card(String courseCode, Branch branch) {
        this.courseCode = courseCode;
        this.branch = branch;
        this.id = getIdFromCourseCode(courseCode);
    }


    private static int getIdFromCourseCode(String courseCode) {
        switch (courseCode.toUpperCase()) {
            case "SY41": return 12;
            case "IA41": return 11;
            case "SY48": return 10;
            case "AP4B": return 9;
            case "GI21": return 8;
            case "PFE":  return 7;  // CORRECTED: PFE = "The 7"
            case "GI41": return 6;
            case "MQ18": return 5;
            case "MQ41": return 4;
            case "MQ51": return 3;
            case "EN21": return 2;
            case "GI28": return 1;  // CORRECTED: GI28 = 1
            default:     return 0;
        }
    }

    /* Get the course code */
    public String getCourseCode() {
        return courseCode;
    }

    /* Get the branch
     */
    public Branch getBranch() {
        return branch;
    }

    /*Get the card ID for sorting
     */
    public int getId() {
        return id;
    }

    /* Check if this is a PFE card
     PFE = ID:7 = "The 7" = Special victory card
     @return true if this is a PFE card
     */
    public boolean isPFE() {
        return "PFE".equalsIgnoreCase(courseCode) || id == 7;
    }

    /*Check if this card matches another card (same course code)
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

    /* Get display string with branch info
     */
    public String toDisplayString() {
        return courseCode + "\nID:" + id + "\n" + branch.name();
    }
}
