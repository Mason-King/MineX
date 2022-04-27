package minex.Player;

import it.unimi.dsi.fastutil.Hash;
import minex.Game.Game;
import minex.Party.Party;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class mPlayer {

    public static List<mPlayer> players = new ArrayList<>();
    public static Map<UUID, mPlayer> uuidPlayers = new HashMap<>();
    private List<ItemStack> fullStash = new ArrayList<>();
    private List<ItemStack> selectedStash = new ArrayList<>();

    private UUID id;
    private Party party;
    private Game currGame;
    private int stashSize = 9;
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

    public List<ItemStack> getFullStash() {
        return fullStash;
    }

    public void setFullStash(List<ItemStack> fullStash) {
        this.fullStash = fullStash;
    }

    public List<ItemStack> getSelectedStash() {
        return selectedStash;
    }

    public void setSelectedStash(List<ItemStack> selectedStash) {
        this.selectedStash = selectedStash;
    }

    public int getStashSize() {
        return stashSize;
    }

    public void setStashSize(int stashSize) {
        this.stashSize = stashSize;
    }
}
