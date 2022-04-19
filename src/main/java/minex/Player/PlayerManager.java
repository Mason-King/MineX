package minex.Player;

import it.unimi.dsi.fastutil.Hash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static Map<UUID, mPlayer> mPlayers = new HashMap<>();

    public void createPlayer(UUID id) {
        mPlayer mPlayer = new mPlayer(id, false, null, null);
    }

    public static mPlayer getPlayer(UUID u) {
        return mPlayers.get(u);
    }

}
