package minex.Quests;

import minex.Main;
import minex.Quests.QuestTypes.Type.*;
import minex.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuestManager {

    private Main main;

    private List<Quest> allQuest;
    private List<Quest> blacksmith;
    private List<Quest> therapist;
    private List<Quest> alchemist;
    private List<Quest> warlord;

    public QuestManager(Main main) {
        this.main = main;

        this.allQuest = new ArrayList<>();
        this.blacksmith = new ArrayList<>();
        this.therapist = new ArrayList<>();
        this.alchemist = new ArrayList<>();
        this.warlord = new ArrayList<>();

    }

    public void loadQuests() {
        File f = new File(main.getDataFolder().getAbsolutePath() + "/Task.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

        config.getConfigurationSection("blacksmith").getKeys(false).forEach(key -> {
            QuestType type = QuestType.valueOf(config.getString("blacksmith." + key + ".type"));
            int amount = config.getInt("blacksmith." + key + ".amount");
            String m = (config.isSet("blacksmith." + key + ".id") ? (config.getString("blacksmith." + key + ".id")) : null);
            int karma = config.getInt("blacksmith." + key + ".karma");
            int distance = config.getInt("blacksmith." + key + ".distance");
            String name = config.getString("blacksmith." + key + ".name");
            List<String> lore = Utils.color(config.getStringList("blacksmith." + key + ".lore"));
            List<String> commands = config.getStringList("blacksmith." + key + ".commands");
            String area = config.getString("blacksmith." + key + ".area");

            Quest q = new Quest("blacksmith", type, amount, m, karma, distance, name, lore, commands, area, false, 0);
            blacksmith.add(q);
            allQuest.add(q);
        });
    }

    public List<Quest> dupeQuests() {
        List<Quest> quests = new ArrayList<>();
        for(Quest q : allQuest) {
            Quest newQ = new Quest(q.getTraderType(), q.getType(), q.getAmount(), q.getId(), q.getKarma(), q.getDistance(), q.getName(), q.getLore(), q.getCommands() , q.getArea(), false, 0);
            quests.add(newQ);
        }
        return quests;
    }

    public List<Quest> getAllQuest() {
        return this.allQuest;
    }



}
