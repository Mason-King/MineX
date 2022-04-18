package minex.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import minex.Game.Game;
import minex.Main;
import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import redis.clients.jedis.Pipeline;

import javax.print.Doc;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    public static List<Game> games = new ArrayList<>();
    public static Map<String, Game> allGames = new HashMap<>();

    public static Game createGame(String id) {
        Game game = new Game(id);
        games.add(game);
        allGames.put(id, game);

        save(game);

        return game;
    }

    public static Game getGame(String id) {
        return allGames.get(id);
    }

    public static void save(Game game) {
        Gson gson = new Gson();
        String json = gson.toJson(game).trim();;

        Pipeline pipeline = Main.jedis.pipelined();
        pipeline.set(game.getId(), json);
        pipeline.sync();

        Main.collection.insertOne(Document.parse(json));

        File file = new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "game.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = config.createSection(game.getId());

    }

}
