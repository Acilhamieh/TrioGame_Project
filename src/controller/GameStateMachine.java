package controller;

import enums.GameState;

/**
 * Manages game state transitions.
 * Ensures valid state changes and tracks current state.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class GameStateMachine {
    private GameState currentState;

    /**
     * Constructor - initializes to SETUP state
     */
    public GameStateMachine() {
        this.currentState = GameState.SETUP;
    }

    /**
     * Transition to a new state
     * @param newState The state to transition to
     * @return true if transition was valid and successful
     */
    public boolean transitionTo(GameState newState) {
        if (isValidTransition(currentState, newState)) {
            GameState oldState = currentState;
            currentState = newState;

            logTransition(oldState, newState);
            return true;
        } else {
            System.out.println("⚠️  Invalid state transition: " +
                    currentState + " → " + newState);
            return false;
        }
    }

    /**
     * Check if a state transition is valid
     * @param from Current state
     * @param to Target state
     * @return true if transition is allowed
     */
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

    /**
     * Get the current game state
     * @return Current state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Check if game is in playing state
     * @return true if currently playing
     */
    public boolean isPlaying() {
        return currentState == GameState.PLAYING;
    }

    /**
     * Check if game is over
     * @return true if game has ended
     */
    public boolean isGameOver() {
        return currentState == GameState.GAME_OVER;
    }

    /**
     * Check if game is in setup
     * @return true if in setup phase
     */
    public boolean isSetup() {
        return currentState == GameState.SETUP;
    }

    /**
     * Reset state machine to initial state
     */
    public void reset() {
        currentState = GameState.SETUP;
        System.out.println("🔄 State machine reset to SETUP");
    }

    /**
     * Log state transitions for debugging
     * @param from Previous state
     * @param to New state
     */
    private void logTransition(GameState from, GameState to) {
        // Uncomment for debugging
        // System.out.println("State: " + from + " → " + to);
    }

    /**
     * Get state description
     * @return Human-readable state description
     */
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
