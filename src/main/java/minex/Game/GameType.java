package minex.Game;

public enum GameType {

    SOLO, DUO, TRIO, SQUADS;

    private static GameType type;

    public static void setState(GameType type){
        GameType.type = type;
    }

    public static boolean isState(GameType type) {
        return GameType.type == type;
    }

    public static GameType getState() {
        return type;
    }

}
