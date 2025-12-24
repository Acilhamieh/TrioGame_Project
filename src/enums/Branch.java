package model.enums;

/**
 * Enumeration representing the different engineering branches at UTBM.
 * Each branch corresponds to a type of course in the game.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0
 */
public enum Branch {
    COMPUTER_SCIENCE("Computer Science"),
    INDUSTRIAL_ENGINEERING("Industrial Engineering"),
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    ENERGY_ENGINEERING("Energy Engineering"),
    SPECIAL("Special");

    private final String displayName;

    /**
     * Constructor for Branch enum
     * @param displayName The human-readable name of the branch
     */
    Branch(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name of this branch
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
