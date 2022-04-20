package minex.Managers;

import minex.Game.Countdown;

import java.util.HashMap;
import java.util.Map;

public class CountdownManager {

    public static Map<String, Countdown> countdownMap = new HashMap<>();

    public static void addCountdown(String id, Countdown countdown) {
        countdownMap.put(id, countdown);
    }

    public static Countdown getCountdown(String id) {
        return countdownMap.get(id);
    }

}
