package model.enums;

/**
 * Enumeration representing the three difficulty levels in the game.
 * Difficulty affects game parameters like card distribution.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0
 */
public enum Difficulty {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard");

    private final String displayName;

    /**
     * Constructor for Difficulty enum
     * @param displayName The human-readable name of the difficulty
     */
    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name of this difficulty level
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
