package minex.Enums;

public enum LootType {

    uncommon("Uncommon"),
    common("Common"),
    rare("Rare");

    private final String text;

    LootType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}
