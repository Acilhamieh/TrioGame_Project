package enums;

/**
 * Enumeration representing the two difficulty levels in the game.
 * Difficulty affects VALIDATION RULES, NOT card distribution.
 * Card distribution is ALWAYS 36 cards regardless of difficulty.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 2.0 - Simple/Advanced only
 */
public enum Difficulty {
    SIMPLE("Simple"),
    ADVANCED("Advanced");

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
