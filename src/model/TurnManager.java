package model;

import java.util.List;


public class TurnManager {
    private List<Student> students;
    private int currentPlayerIndex;
    private int roundNumber;


    public TurnManager(List<Student> students) {
        this.students = students;
        this.currentPlayerIndex = 0;
        this.roundNumber = 1;
    }

    //Get the current student whose turn it is

    public Student getCurrentStudent() {
        if (students == null || students.isEmpty()) {
            return null;
        }
        return students.get(currentPlayerIndex);
    }

    /*
      Advance to the next player's turn
      Increments round number when returning to first player
     */
    public void nextTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= students.size()) {
            currentPlayerIndex = 0;
            roundNumber++;
        }
    }

    //Get the current player index

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    //Get the current round number

    public int getRoundNumber() {
        return roundNumber;
    }

    //Get the total number of players

    public int getPlayerCount() {
        return students != null ? students.size() : 0;
    }

    //Reset the turn manager to initial state

    public void reset() {
        this.currentPlayerIndex = 0;
        this.roundNumber = 1;
    }

    //Check if it's a specific student's turn

    public boolean isCurrentPlayer(Student student) {
        return getCurrentStudent() != null && getCurrentStudent().equals(student);
    }

    @Override
    public String toString() {
        return "Round " + roundNumber + " - Current Player: " +
                (getCurrentStudent() != null ? getCurrentStudent().getName() : "None");
    }
}
