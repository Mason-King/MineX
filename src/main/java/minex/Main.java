package minex;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import minex.Game.Game;
import minex.Game.GameCommand;
import minex.Managers.GameManager;
import minex.Party.PartyCommand;
import minex.Player.mPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Jedis jedis;

    public static MongoClient client = MongoClients.create("mongodb+srv://mason:Mjking68@minex.kx0a3.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
    public static MongoDatabase database = client.getDatabase("MineX");
    public static MongoCollection<Document> collection = database.getCollection("games");


    @Override
    public void onEnable() {
        setupDB();

        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver.cluster" );
        mongoLogger.setLevel(Level.SEVERE);

        saveDefaultConfig();
        this.saveResource("schematics/lobby.schem", false);
        this.saveResource("game.yml", false);
        this.saveResource("Guis/GameSelector.yml", false);

        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        for(Player p : Bukkit.getOnlinePlayers()) {
            new mPlayer(p);
        }

        instance = this;
        new PartyCommand();
        new GameCommand();
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

        collection.find().forEach((Consumer<Document>) document -> {
            GameManager.load(gson.fromJson(document.toJson(), Game.class));
        });

    }

}
