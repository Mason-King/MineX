package minex.Quests;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import minex.Main;
import org.bukkit.Material;

import java.util.List;

public class Quest {

    private String traderType;
    private QuestType type;
    private int amount;
    private String id;
    private int karma;
    private int distance;
    private String name;
    private List<String> lore;
    private List<String> commands;
    private String area;
    private boolean completed;
    private int progress;

    public Quest(String traderType, QuestType type, int amount, String id, int karma, int distance, String name, List<String> lore, List<String> commands, String area, boolean completed, int progress) {
        this.traderType = traderType;
        this.type = type;
        this.amount = amount;
        this.id = id;
        this.karma = karma;
        this.distance = distance;
        this.name = name;
        this.lore = lore;
        this.commands = commands;
        this.area = area;
        this.completed = completed;
        this.progress = progress;
    }

    public QuestType getType() {
        return type;
    }

    public void setType(QuestType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTraderType() {
        return traderType;
    }

    public void setTraderType(String traderType) {
        this.traderType = traderType;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void incProgress() {
        progress++;
        System.out.println(progress);
    }

}
