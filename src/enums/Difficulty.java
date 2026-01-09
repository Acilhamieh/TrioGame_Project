package enums;


public enum Difficulty {
    SIMPLE("Simple"),
    ADVANCED("Advanced");

    private final String displayName;


    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
