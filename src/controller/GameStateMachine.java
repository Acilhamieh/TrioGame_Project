package controller;

import enums.GameState;


public class GameStateMachine {
    private GameState currentState;


    public GameStateMachine() {
        this.currentState = GameState.SETUP;
    }


    public boolean transitionTo(GameState newState) {
        if (isValidTransition(currentState, newState)) {
            GameState oldState = currentState;
            currentState = newState;

            logTransition(oldState, newState);
            return true;
        } else {
            System.out.println(" Invalid state transition: " +
                    currentState + " -> " + newState);
            return false;
        }
    }


    private boolean isValidTransition(GameState from, GameState to) {
        // Define valid state transitions
        switch (from) {
            case SETUP:
                // From SETUP, can only go to PLAYING
                return to == GameState.PLAYING;

            case PLAYING:
                // From PLAYING, can go to CHECKING_VICTORY or GAME_OVER
                return to == GameState.CHECKING_VICTORY || to == GameState.GAME_OVER;

            case CHECKING_VICTORY:
                // From CHECKING_VICTORY, can go back to PLAYING or to GAME_OVER
                return to == GameState.PLAYING || to == GameState.GAME_OVER;

            case GAME_OVER:
                // From GAME_OVER, can only restart to SETUP
                return to == GameState.SETUP;

            default:
                return false;
        }
    }

    /* Get the current game state*/

    public GameState getCurrentState() {
        return currentState;
    }

    /*Check if game is in playing state*/
    public boolean isPlaying() {
        return currentState == GameState.PLAYING;
    }

    /* Check if game is over*/
    public boolean isGameOver() {
        return currentState == GameState.GAME_OVER;
    }

    /* Check if game is in setup*/
    public boolean isSetup() {
        return currentState == GameState.SETUP;
    }

    /*Reset state machine to initial state*/

    public void reset() {
        currentState = GameState.SETUP;
        System.out.println("🔄 State machine reset to SETUP");
    }


    private void logTransition(GameState from, GameState to) {
        // Uncomment for debugging
        // System.out.println("State: " + from + " → " + to);
    }

    /*Get state description*/

    public String getStateDescription() {
        switch (currentState) {
            case SETUP:
                return "Setting up the game...";
            case PLAYING:
                return "Game in progress";
            case CHECKING_VICTORY:
                return "Checking for winner...";
            case GAME_OVER:
                return "Game has ended";
            default:
                return "Unknown state";
        }
    }

    @Override
    public String toString() {
        return "GameState: " + currentState + " (" + getStateDescription() + ")";
    }
}
