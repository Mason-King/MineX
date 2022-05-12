package minex.Player;

import it.unimi.dsi.fastutil.Hash;
import minex.Game.Game;
import minex.Game.Team;
import minex.Managers.PlayerManager;
import minex.Party.Party;
import minex.Quests.Quest;
import minex.Quests.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class mPlayer {

    private List<ItemStack> fullStash = new ArrayList<>();
    private List<ItemStack> selectedStash = new ArrayList<>();

    private UUID id;
    private Party party;
    private Game currGame;
    private int stashSize = 9;
    private Team team;
    private int balance;
    private int karma;
    private List<Quest> quests;


    public mPlayer(UUID id) {
        this.id = id;
        this.balance = 0;

        this.quests = new ArrayList<>();
    }

    public mPlayer(Player player) {
        this.id = player.getUniqueId();
        this.balance = 0;
        this.quests = new ArrayList<>();
    }

    public void addSelectedItem(ItemStack stack) {
        selectedStash.add(stack);
    }

    public void removeSelectedItem(ItemStack stack) {
        selectedStash.remove(stack);
    }

    public void addItem(ItemStack stack) {
        fullStash.add(stack);
    }

    public void removeItem(ItemStack stack) {
        fullStash.remove(stack);
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team t) {
        this.team = t;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addQuest(Quest q) {
        quests.add(q);
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public List<Quest> getQuestByType(QuestType type) {
        List<Quest> temp = new ArrayList<>();
        for(Quest q : quests) {
            if(q.getType().equals(type)) {
                temp.add(q);
            }
        }
        return temp;
    }

}


