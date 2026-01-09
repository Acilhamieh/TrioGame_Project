package model;

import java.util.*;


public class ScoreBoard {
    private Map<Student, Integer> studentScores;
    private Map<Student, List<Trio>> studentTrios;
    private Map<Team, Integer> teamScores;
    private Map<Team, List<Trio>> teamTrios;

    //Constructor for ScoreBoard

    public ScoreBoard() {
        this.studentScores = new HashMap<>();
        this.studentTrios = new HashMap<>();
        this.teamScores = new HashMap<>();
        this.teamTrios = new HashMap<>();
    }

    //Register a student in the scoreboard

    public void registerStudent(Student student) {
        studentScores.put(student, 0);
        studentTrios.put(student, new ArrayList<>());
    }

    //Register a team in the scoreboard

    public void registerTeam(Team team) {
        teamScores.put(team, 0);
        teamTrios.put(team, new ArrayList<>());
    }

    //Update a student's score

    public void updateScore(Student student, int ects) {
        int currentScore = studentScores.getOrDefault(student, 0);
        studentScores.put(student, currentScore + ects);
    }

    //Update a team's score

    public void updateTeamScore(Team team, int ects) {
        int currentScore = teamScores.getOrDefault(team, 0);
        teamScores.put(team, currentScore + ects);
    }

    // Record a trio for a student

    public void recordTrio(Student student, Trio trio) {
        List<Trio> trios = studentTrios.get(student);
        if (trios != null) {
            trios.add(trio);
        }
    }

    //Record a trio for a team

    public void recordTeamTrio(Team team, Trio trio) {
        List<Trio> trios = teamTrios.get(team);
        if (trios != null) {
            trios.add(trio);
        }
    }

    // Get a student's score

    public int getScore(Student student) {
        return studentScores.getOrDefault(student, 0);
    }

    // Get a team's score

    public int getTeamScore(Team team) {
        return teamScores.getOrDefault(team, 0);
    }

    // Get student rankings sorted by score (highest first)

    public List<Student> getRankings() {
        List<Student> students = new ArrayList<>(studentScores.keySet());
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Integer.compare(studentScores.get(s2), studentScores.get(s1));
            }
        });
        return students;
    }

    // Get team rankings sorted by score (highest first)
    public List<Team> getTeamRankings() {
        List<Team> teams = new ArrayList<>(teamScores.keySet());
        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                return Integer.compare(teamScores.get(t2), teamScores.get(t1));
            }
        });
        return teams;
    }

    //Get the winner (student with highest score)

    public Student getWinner() {
        List<Student> rankings = getRankings();
        return rankings.isEmpty() ? null : rankings.get(0);
    }

    //Get the winning team

    public Team getWinningTeam() {
        List<Team> rankings = getTeamRankings();
        return rankings.isEmpty() ? null : rankings.get(0);
    }

    // Get number of trios for a student

    public int getTrioCount(Student student) {
        List<Trio> trios = studentTrios.get(student);
        return trios != null ? trios.size() : 0;
    }

    // Get number of trios for a team
    public int getTeamTrioCount(Team team) {
        List<Trio> trios = teamTrios.get(team);
        return trios != null ? trios.size() : 0;
    }

    // Clear all scores and reset scoreboard

    public void reset() {
        studentScores.clear();
        studentTrios.clear();
        teamScores.clear();
        teamTrios.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SCOREBOARD ===\n");

        if (!studentScores.isEmpty()) {
            sb.append("\nStudent Scores:\n");
            List<Student> rankings = getRankings();
            for (int i = 0; i < rankings.size(); i++) {
                Student s = rankings.get(i);
                sb.append((i + 1)).append(". ").append(s.getName())
                        .append(": ").append(studentScores.get(s)).append(" ECTS\n");
            }
        }

        if (!teamScores.isEmpty()) {
            sb.append("\nTeam Scores:\n");
            List<Team> teamRankings = getTeamRankings();
            for (int i = 0; i < teamRankings.size(); i++) {
                Team t = teamRankings.get(i);
                sb.append((i + 1)).append(". ").append(t.getTeamName())
                        .append(": ").append(teamScores.get(t)).append(" ECTS\n");
            }
        }

        return sb.toString();
    }
}
