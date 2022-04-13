package minex.Game;

public enum GameState {

    IN_LOBBY, IN_GAME, RESETTING;

    private static GameState state;

    public static void setState(GameState state){
        GameState.state = state;
    }

    public static boolean isState(GameState state) {
        return GameState.state == state;
    }

    public static GameState getState() {
        return state;
    }



}
