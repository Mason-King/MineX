package minex;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import minex.Events.*;
import minex.Commands.BankCommand;
import minex.Events.Quests.*;
import minex.Objects.Game;
import minex.Commands.GameCommand;
import minex.Commands.ShopCommand;
import minex.Utils.Items;
import minex.Managers.GameManager;
import minex.Managers.PlayerManager;
import minex.Commands.PartyCommand;
import minex.Objects.mPlayer;
import minex.Managers.QuestManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Jedis jedis;

    public QuestManager manager;

    public static MongoClient client = MongoClients.create("mongodb+srv://mason:Mjking68@minex.kx0a3.mongodb.net/MineX?retryWrites=true&w=majority");
    public static MongoDatabase database = client.getDatabase("MineX");
    public static MongoCollection<Document> gameCollection = database.getCollection("games");
    public static MongoCollection<Document> playerCollection = database.getCollection("players");


    @Override
    public void onEnable() {
        setupDB();

        Logger mongoLogger = Logger.getLogger( "com.mongodb" );
        mongoLogger.setLevel(Level.SEVERE);

        saveDefaultConfig();
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/GameGui.yml").exists()) this.saveResource("Guis/GameGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/MapGui.yml").exists())this.saveResource("Guis/MapGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/StashGui.yml").exists())this.saveResource("Guis/StashGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/BankGui.yml").exists())this.saveResource("Guis/BankGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/BankSelectionGui.yml").exists())this.saveResource("Guis/BankSelectionGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/SellGui.yml").exists())this.saveResource("Guis/SellGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/TaskGui.yml").exists())this.saveResource("Guis/TaskGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/TraderGui.yml").exists())this.saveResource("Guis/TraderGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Guis/UpgradeGui.yml").exists())this.saveResource("Guis/UpgradeGui.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Tasks.yml").exists())this.saveResource("Tasks.yml", false);
        if(!new File(this.getDataFolder().getAbsoluteFile() + "/Shops.yml").exists())this.saveResource("Shops.yml", false);
        // Plugin startup logic
        instance = this;

        Items.loadItem(this);

        manager = new QuestManager(this);
        manager.loadQuests();

        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChestPlace(), this);
        this.getServer().getPluginManager().registerEvents(new ExtractionListener(), this);
        this.getServer().getPluginManager().registerEvents(new RegionListener(), this);
        this.getServer().getPluginManager().registerEvents(new Destroy(), this);
        this.getServer().getPluginManager().registerEvents(new MobKill(), this);
        this.getServer().getPluginManager().registerEvents(new Place(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKill(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKillDistance(),this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(),this);

        new PartyCommand();
        new GameCommand();
        new BankCommand();
        new ShopCommand();

        Bukkit.getLogger().log(Level.INFO, "[MineX] MineX plugin enabled");
    }

    @Override
    public void onDisable() {
        for(Game g : GameManager.games) {
            g.reset();
        }

        for(mPlayer mp : PlayerManager.players) {
            if(mp.getBoard() == null) continue;
            mp.getBoard().delete();
        }
    }

    public static Main getInstance() {
        return instance;
    }

        private void setupDB() {
            jedis = new Jedis("redis-15116.c246.us-east-1-4.ec2.cloud.redislabs.com", 15116, 5000);
            jedis.auth("yY0FvzawpihJYeBCSZaZ2LGNsKmH09yS");

            Gson gson = new Gson();

            if (database.getCollection("games") == null) {
                database.createCollection("games");
            } else if(database.getCollection("players") == null) {
                database.createCollection("players");
            }

            gameCollection.find().forEach((Consumer<Document>) document -> {
                Game game = gson.fromJson(document.toJson(), Game.class);
                getServer().createWorld(new WorldCreator(game.getArena().getWorld()));
                getServer().createWorld(new WorldCreator(game.getLobby().getWorld()));
                GameManager.addGame(game);
            });

            playerCollection.find().forEach((Consumer<Document>) document -> {
                mPlayer mp = gson.fromJson(document.toJson(), mPlayer.class);

                PlayerManager.addPlayer(mp);
            });

        }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public QuestManager getManager() {
        return manager;
    }

}
