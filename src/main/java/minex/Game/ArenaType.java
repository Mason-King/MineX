package minex.Game;

public enum ArenaType {

    //Class used to determine if arena is a lobby or a game arena!
    LOBBY, GAME;

    private static ArenaType type;

    public static void setState(ArenaType type){
        ArenaType.type = type;
    }

    public static boolean isState(ArenaType type) {
        return ArenaType.type == type;
    }

    public static ArenaType getState() {
        return type;
    }

}
