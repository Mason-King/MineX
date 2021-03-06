package minex.Objects;

import minex.Enums.QuestType;
import minex.Fastboard.FastBoard;
import minex.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class mPlayer {

    private List<ItemStack> fullStash = new ArrayList<>();

    private UUID id;
    private Party party;
    private int farmLimit;
    private int farmLevel = 1;
    private int currGPU;
    private Game currGame;
    private int stashSize;
    private int stashLevel = 1;
    private Team team;
    private int balance;
    private int karma;
    private List<Quest> quests;
    private FastBoard board;


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

    public int getStashSize() {
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/Guis/UpgradeGui.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return (stashSize == 0) ? config.getInt("upgrades.stash.1.amount") : stashSize;
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

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
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

    public void setQuests(List<Quest> quests) {
        this.quests = quests;
    }

    public FastBoard getBoard() {
        return board;
    }

    public void setBoard(FastBoard board) {
        this.board = board;
    }

    public int getFarmLimit() {
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/Guis/UpgradeGui.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return (farmLimit == 0) ? config.getInt("upgrades.farm.1.amount") : farmLimit;
    }

    public void setFarmLimit(int farmLimit) {
        this.farmLimit = farmLimit;
    }

    public int getCurrGPU() {
        return currGPU;
    }

    public void setCurrGPU(int currGPU) {
        this.currGPU = currGPU;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getStashLevel() {
        return stashLevel;
    }

    public void setStashLevel(int stashLevel) {
        this.stashLevel = stashLevel;
    }
}


