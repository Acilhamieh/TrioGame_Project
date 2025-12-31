package enums;

/**
 * Enumeration representing the different states of the game.
 * Used for managing game flow and transitions.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0
 */
public enum GameState {
    SETUP("Setup"),
    PLAYING("Playing"),
    CHECKING_VICTORY("Checking Victory"),
    GAME_OVER("Game Over");

    private final String displayName;

    /**
     * Constructor for GameState enum
     * @param displayName The human-readable name of the state
     */
    GameState(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Check if the game is currently active (playing)
     * @return true if in PLAYING state
     */
    public boolean isActive() {
        return this == PLAYING;
    }

    /**
     * Check if the game is over
     * @return true if in GAME_OVER state
     */
    public boolean isGameOver() {
        return this == GAME_OVER;
    }

    /**
     * Get the display name of this state
     * @return The display name as a string
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
