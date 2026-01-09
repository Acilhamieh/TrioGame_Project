package enums;


public enum GameMode {
    INDIVIDUAL_SIMPLE("Individual Simple", false, 3, 2),
    INDIVIDUAL_ADVANCED("Individual Advanced", false, 2, 3),
    TEAM_SIMPLE("Team Simple", true, 3, 2),
    TEAM_ADVANCED("Team Advanced", true, 2, 3);

    private final String displayName;
    private final boolean isTeamMode;
    private final int triosRequired;
    private final int ectsPerTrio;

    GameMode(String displayName, boolean isTeamMode, int triosRequired, int ectsPerTrio) {
        this.displayName = displayName;
        this.isTeamMode = isTeamMode;
        this.triosRequired = triosRequired;
        this.ectsPerTrio = ectsPerTrio;
    }

    /*Check if this is a team mode
     */
    public boolean isTeamMode() {
        return isTeamMode;
    }

    /*Get the number of trios required to win
     */
    public int getTriosRequired() {
        return triosRequired;
    }

    /*Get ECTS credits awarded per trio
     */
    public int getEctsPerTrio() {
        return ectsPerTrio;
    }

    /*Check if this is an advanced mode
     */
    public boolean isAdvancedMode() {
        return this == INDIVIDUAL_ADVANCED || this == TEAM_ADVANCED;
    }

    /*Get total ECTS required to graduate
     */
    public int getTotalEctsRequired() {
        return triosRequired * ectsPerTrio;
    }

    /* Get the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
