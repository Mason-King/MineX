package minex;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import minex.CustomEnchants.CustomEnchant;
import minex.CustomEnchants.CustomEnchantManager;
import minex.Events.ChestPlace;
import minex.Events.JoinEvent;
import minex.Game.BankCommand;
import minex.Game.Game;
import minex.Game.GameCommand;
import minex.LootChest.Items;
import minex.Managers.GameManager;
import minex.Managers.PlayerManager;
import minex.Party.PartyCommand;
import minex.Player.mPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Jedis jedis;

    public static MongoClient client = MongoClients.create("mongodb+srv://mason:Mjking68@minex.kx0a3.mongodb.net/MineX?retryWrites=true&w=majority");
    public static MongoDatabase database = client.getDatabase("MineX");
    public static MongoCollection<Document> gameCollection = database.getCollection("games");
    public static MongoCollection<Document> playerCollection = database.getCollection("players");


    @Override
    public void onEnable() {
        setupDB();

        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver.cluster" );
        mongoLogger.setLevel(Level.SEVERE);

        saveDefaultConfig();
        this.saveResource("schematics/lobby.schem", false);
        this.saveResource("Guis/GameSelector.yml", false);
        this.saveResource("Guis/MapSelector.yml", false);
        this.saveResource("Guis/Stash.yml", false);
        this.saveResource("Guis/Bank.yml", false);
        this.saveResource("Guis/BankSelection.yml", false);
        this.saveResource("game.yml", false);
        this.saveResource("Enchantments.yml", false);
        // Plugin startup logic
        instance = this;

        Items.loadItem(this);

        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChestPlace(), this);

        for(Player p : Bukkit.getOnlinePlayers()) {
            PlayerManager.createPlayer(p);
        }

        CustomEnchantManager manager = new CustomEnchantManager();
        manager.loadEnchants();
        manager.register();


        new PartyCommand();
        new GameCommand();
        new BankCommand();
    }

    @Override
    public void onDisable() {

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
                GameManager.addGame(game);
            });

            playerCollection.find().forEach((Consumer<Document>) document -> {
                mPlayer mp = gson.fromJson(document.toJson(), mPlayer.class);

                PlayerManager.addPlayer(mp);
            });

        }

}
