package enums;

public enum GameState {
    SETUP("Setup"),
    PLAYING("Playing"),
    CHECKING_VICTORY("Checking Victory"),
    GAME_OVER("Game Over");

    private final String displayName;


    GameState(String displayName) {
        this.displayName = displayName;
    }

    /*Check if the game is currently active (playing)
     */
    public boolean isActive() {
        return this == PLAYING;
    }

    /*Check if the game is over
     */
    public boolean isGameOver() {
        return this == GAME_OVER;
    }

    /* Get the display name of this state
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
