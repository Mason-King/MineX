package minex.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import minex.Game.Game;
import minex.Main;
import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import redis.clients.jedis.Pipeline;

import javax.print.Doc;
import java.io.File;
import java.util.*;

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

    public static void addGame(Game game) {
        games.add(game);
        allGames.put(game.getId(), game);
    }

    public static List<Game> getFullest() {
        List<Game> preGames = games;
        Collections.sort(preGames, (o1, o2) -> o2.getCurrPlayers() - o1.getCurrPlayers());
        return preGames;
    }

    public static Game getGame(String id) {
        return allGames.get(id);
    }

    public static void save(Game game) {
        Gson gson = new Gson();
        String json = gson.toJson(game).trim();

        Pipeline pipeline = Main.jedis.pipelined();
        pipeline.set(game.getId(), json);
        pipeline.sync();

        Main.collection.replaceOne(Filters.eq("id", game.getId()), Document.parse(json), new UpdateOptions().upsert(true));

    }

}
