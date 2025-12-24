package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student player in Trio_UTBM.
 * Students accumulate ECTS credits by forming trios.
 *
 * @author Acil HAMIEH
 * @version 1.0
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

    /**
     * Get the student's name
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the student's hand
     * @return The hand object
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Get the student's current ECTS credits
     * @return ECTS credits
     */
    public int getEctsCredits() {
        return ectsCredits;
    }

    /**
     * Add ECTS credits to the student
     * Also updates team score if student is in a team
     * @param ects ECTS credits to add
     */
    public void addEcts(int ects) {
        this.ectsCredits += ects;
        if (team != null) {
            team.addToTeamScore(ects);
        }
    }

    /**
     * Add a completed trio to the student's record
     * @param trio The trio to add
     */
    public void addCompletedTrio(Trio trio) {
        completedTrios.add(trio);
    }

    /**
     * Get all completed trios
     * @return List of completed trios
     */
    public List<Trio> getCompletedTrios() {
        return new ArrayList<>(completedTrios);
    }

    /**
     * Get the number of trios completed
     * @return Number of trios
     */
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
     * Set the student's team
     * @param team The team to join
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Get the student's team
     * @return The team, or null if not in a team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Check if student is in a team
     * @return true if student has a team
     */
    public boolean isInTeam() {
        return team != null;
    }

    /**
     * Reset student's game state
     */
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
