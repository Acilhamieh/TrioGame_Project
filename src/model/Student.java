package model;

import enums.Branch;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student player in Trio_UTBM.
 * Students accumulate ECTS credits by forming trios.
 *
 * @author Acil HAMIEH
 * @version 1.2 - Fixed Advanced mode: PFE trios excluded from linked trio counting
 */
public class Student {
    private String name;
    private Hand hand;
    private int ectsCredits;
    private List<Trio> completedTrios;
    private Team team;

    /**
     * Constructor for a student
     * @param name The student's name
     */
    public Student(String name) {
        this.name = name;
        this.hand = new Hand(this);
        this.ectsCredits = 0;
        this.completedTrios = new ArrayList<>();
        this.team = null;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getEctsCredits() {
        return ectsCredits;
    }

    public void addEcts(int ects) {
        this.ectsCredits += ects;
        if (team != null) {
            team.addToTeamScore(ects);
        }
    }

    public void addCompletedTrio(Trio trio) {
        completedTrios.add(trio);
    }

    public List<Trio> getCompletedTrios() {
        return new ArrayList<>(completedTrios);
    }

    public int getTrioCount() {
        return completedTrios.size();
    }

    /**
     * Check if student has graduated (reached 6 ECTS)
     * @return true if student has 6 or more ECTS
     */
    public boolean hasGraduated() {
        return ectsCredits >= 6;
    }

    /**
     * NEW: Check if student has won trio of 7 (PFE)
     * Victory condition: Win trio of PFE (ID:7)
     * @return true if has won trio of PFE
     */
    public boolean hasWonTrioOfSeven() {
        for (Trio trio : completedTrios) {
            if (trio.isPFETrio()) { // PFE has ID:7
                return true;
            }
        }
        return false;
    }

    /**
     * NEW: Check if student has 3 trios (victory condition)
     * @return true if has 3 or more trios
     */
    public boolean hasThreeTrios() {
        return completedTrios.size() >= 3;
    }

    /**
     * NEW: Check if student has won 2 linked trios (Advanced/Picante mode)
     * Linked = same branch (EXCLUDING PFE trios)
     *
     * RULES:
     * - CS + CS = WIN ✅
     * - ME + ME = WIN ✅
     * - CS + ME = NO WIN ❌
     * - PFE trios don't count (they have instant-win rule)
     *
     * @return true if has 2 trios from same branch
     */
    public boolean hasTwoLinkedTrios() {
        if (completedTrios.size() < 2) {
            return false;
        }

        // DEBUG: Print all trios and their branches
        System.out.println("=== CHECKING LINKED TRIOS FOR " + name + " ===");
        for (Trio trio : completedTrios) {
            System.out.println("  Trio: " + trio.getCard1().getCourseCode() +
                    " x3, Branch: " + trio.getBranch());
        }

        // Count trios by branch (EXCLUDE PFE/SPECIAL!)
        int csCount = 0, ieCount = 0, meCount = 0, eeCount = 0;

        for (Trio trio : completedTrios) {
            Branch branch = trio.getBranch();

            // ✅ CRITICAL FIX: Skip PFE trios (they have their own instant-win rule)
            if (branch == Branch.SPECIAL) {
                System.out.println("  Skipping PFE trio (special branch)");
                continue;
            }

            if (branch == Branch.COMPUTER_SCIENCE) csCount++;
            else if (branch == Branch.INDUSTRIAL_ENGINEERING) ieCount++;
            else if (branch == Branch.MECHANICAL_ENGINEERING) meCount++;
            else if (branch == Branch.ENERGY_ENGINEERING) eeCount++;
        }

        System.out.println("  Branch counts: CS=" + csCount + ", IE=" + ieCount +
                ", ME=" + meCount + ", EE=" + eeCount);

        // Check if any branch has 2+ trios
        boolean result = csCount >= 2 || ieCount >= 2 || meCount >= 2 || eeCount >= 2;
        System.out.println("  Has 2 linked trios: " + result);
        System.out.println("=== END CHECK ===");

        return result;
    }

    /**
     * NEW: Check victory for simple mode
     * Win: 3 trios OR trio of 7 (PFE)
     * @return true if won
     */
    public boolean hasWonSimple() {
        return hasThreeTrios() || hasWonTrioOfSeven();
    }

    /**
     * NEW: Check victory for Advanced/Picante mode
     * Win: 2 linked trios (same branch) OR trio of 7 (PFE)
     *
     * EXAMPLES:
     * - 2 CS trios = WIN ✅
     * - 2 ME trios = WIN ✅
     * - 1 CS + 1 ME = NO WIN ❌
     * - 1 PFE trio = INSTANT WIN ✅
     *
     * @return true if won
     */
    public boolean hasWonPicante() {
        return hasTwoLinkedTrios() || hasWonTrioOfSeven();
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isInTeam() {
        return team != null;
    }

    public void reset() {
        this.ectsCredits = 0;
        this.completedTrios.clear();
        this.hand.clear();
    }

    @Override
    public String toString() {
        return name + " - " + ectsCredits + " ECTS (" + completedTrios.size() + " trios)";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return name.equals(student.name);
    }
}
