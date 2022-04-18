package minex;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import minex.Game.GameCommand;
import minex.Party.PartyCommand;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Jedis jedis;


    @Override
    public void onEnable() {
        setupJedis();

        saveDefaultConfig();
        this.saveResource("schematics/lobby.schem", false);
        // Plugin startup logic
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

    private void setupJedis() {
        jedis = new Jedis("localhost", 6379, 5000);
    }

}
