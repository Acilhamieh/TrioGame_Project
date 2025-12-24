package model.enums;

/**
 * Enumeration representing the four game modes in Trio_UTBM.
 * Defines the rules for trio formation and victory conditions.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0
 */
public enum GameMode {
    INDIVIDUAL_SIMPLE("Individual Simple", false, 3, 2),
    INDIVIDUAL_ADVANCED("Individual Advanced", false, 2, 3),
    TEAM_SIMPLE("Team Simple", true, 3, 2),
    TEAM_ADVANCED("Team Advanced", true, 2, 3);

    private final String displayName;
    private final boolean isTeamMode;
    private final int triosRequired;
    private final int ectsPerTrio;

    /**
     * Constructor for GameMode enum
     * @param displayName Human-readable name
     * @param isTeamMode Whether this is a team-based mode
     * @param triosRequired Number of trios needed to graduate
     * @param ectsPerTrio ECTS credits awarded per trio
     */
    GameMode(String displayName, boolean isTeamMode, int triosRequired, int ectsPerTrio) {
        this.displayName = displayName;
        this.isTeamMode = isTeamMode;
        this.triosRequired = triosRequired;
        this.ectsPerTrio = ectsPerTrio;
    }

    /**
     * Check if this is a team mode
     * @return true if team mode, false if individual
     */
    public boolean isTeamMode() {
        return isTeamMode;
    }

    /**
     * Get the number of trios required to win
     * @return Number of trios needed
     */
    public int getTriosRequired() {
        return triosRequired;
    }

    /**
     * Get ECTS credits awarded per trio
     * @return ECTS per trio
     */
    public int getEctsPerTrio() {
        return ectsPerTrio;
    }

    /**
     * Check if this is an advanced mode
     * @return true if advanced mode (requires same branch)
     */
    public boolean isAdvancedMode() {
        return this == INDIVIDUAL_ADVANCED || this == TEAM_ADVANCED;
    }

    /**
     * Get total ECTS required to graduate
     * @return Total ECTS needed
     */
    public int getTotalEctsRequired() {
        return triosRequired * ectsPerTrio;
    }

    /**
     * Get the display name
     * @return Display name as string
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
