package enums;


public enum Branch {
    COMPUTER_SCIENCE("Computer Science"),
    INDUSTRIAL_ENGINEERING("Industrial Engineering"),
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    ENERGY_ENGINEERING("Energy Engineering"),
    SPECIAL("Special");

    private final String displayName;


    Branch(String displayName) {
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
