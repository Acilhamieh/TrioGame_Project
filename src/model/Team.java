package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team of two students in Team mode.
 * Teams share ECTS credits and work together to graduate.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class Team {
    private String teamName;
    private List<Student> members;
    private int teamScore;
    private List<Trio> completedTrios;
    private static final int MAX_MEMBERS = 2;

    /**
     * Constructor for a team
     * @param teamName The name of the team
     */
    public Team(String teamName) {
        this.teamName = teamName;
        this.members = new ArrayList<>();
        this.teamScore = 0;
        this.completedTrios = new ArrayList<>();
    }

    /**
     * Add a student member to the team
     * @param student The student to add
     * @return true if student was added successfully
     */
    public boolean addMember(Student student) {
        if (members.size() < MAX_MEMBERS && !members.contains(student)) {
            members.add(student);
            student.setTeam(this); // Bidirectional relationship
            return true;
        }
        return false;
    }

    /**
     * Remove a member from the team
     * @param student The student to remove
     * @return true if student was removed
     */
    public boolean removeMember(Student student) {
        if (members.remove(student)) {
            student.setTeam(null);
            return true;
        }
        return false;
    }

    /**
     * Get the team name
     * @return The team name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Get all team members
     * @return List of team members
     */
    public List<Student> getMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Get the team's total score
     * @return Team score in ECTS
     */
    public int getTeamScore() {
        return teamScore;
    }

    /**
     * Add ECTS to team score
     * @param ects ECTS credits to add
     */
    public void addToTeamScore(int ects) {
        this.teamScore += ects;
    }

    /**
     * Add a completed trio to team's record
     * @param trio The trio to add
     */
    public void addCompletedTrio(Trio trio) {
        completedTrios.add(trio);
    }

    /**
     * Get all completed trios by this team
     * @return List of completed trios
     */
    public List<Trio> getCompletedTrios() {
        return new ArrayList<>(completedTrios);
    }

    /**
     * Get the number of trios completed by team
     * @return Number of trios
     */
    public int getTrioCount() {
        return completedTrios.size();
    }

    /**
     * Check if team has graduated (reached 6 ECTS)
     * @return true if team has 6 or more ECTS
     */
    public boolean hasGraduated() {
        return teamScore >= 6;
    }

    /**
     * Check if team is full (has 2 members)
     * @return true if team has maximum members
     */
    public boolean isFull() {
        return members.size() >= MAX_MEMBERS;
    }

    /**
     * Check if team is empty
     * @return true if team has no members
     */
    public boolean isEmpty() {
        return members.isEmpty();
    }

    /**
     * Get number of members
     * @return Member count
     */
    public int getMemberCount() {
        return members.size();
    }

    /**
     * Reset team's game state
     */
    public void reset() {
        this.teamScore = 0;
        this.completedTrios.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team ").append(teamName).append(" - ").append(teamScore).append(" ECTS\n");
        sb.append("Members: ");
        for (int i = 0; i < members.size(); i++) {
            sb.append(members.get(i).getName());
            if (i < members.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
