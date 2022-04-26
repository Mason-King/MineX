package minex.Player;

import it.unimi.dsi.fastutil.Hash;
import minex.Game.Game;
import minex.Party.Party;
import org.bukkit.entity.Player;

import java.util.*;

public class mPlayer {

    public static List<mPlayer> players = new ArrayList<>();
    public static Map<UUID, mPlayer> uuidPlayers = new HashMap<>();

    private UUID id;
    private Party party;
    private Game currGame;
    private String teamName;

    public mPlayer(UUID id) {
        this.id = id;

        players.add(this);
        uuidPlayers.put(id, this);
    }

    public mPlayer(Player player) {
        this.id = player.getUniqueId();

        players.add(this);
        uuidPlayers.put(id, this);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Game getCurrGame() {
        return currGame;
    }

    public void setCurrGame(Game currGame) {
        this.currGame = currGame;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
