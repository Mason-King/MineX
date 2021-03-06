package minex.Managers;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import minex.Main;
import minex.Objects.mPlayer;
import minex.Objects.Quest;
import org.bson.Document;
import org.bukkit.entity.Player;
import redis.clients.jedis.Pipeline;

import java.util.*;

public class PlayerManager {

    Main main = Main.getInstance();

    public static List<mPlayer> players = new ArrayList<>();
    public static Map<UUID, mPlayer> uuidPlayers = new HashMap<>();


    public static mPlayer createPlayer(Player p) {
        mPlayer mp = new mPlayer(p);
        players.add(mp);
        uuidPlayers.put(p.getUniqueId(), mp);

        loadQuests(mp);

        save(mp);

        return mp;
    }

    public static mPlayer getmPlayer(UUID id) {
        return uuidPlayers.get(id);
    }

    public static void addPlayer(mPlayer mp) {
        players.add(mp);
        uuidPlayers.put(mp.getId(), mp);
    }

    public static void save(mPlayer mp) {
        Gson gson = new Gson();
        String json = gson.toJson(mp).trim();

        Pipeline pipeline = Main.jedis.pipelined();
        pipeline.set(mp.getId().toString(), json);
        pipeline.sync();

        Main.playerCollection.replaceOne(Filters.eq("id", mp.getId().toString()), Document.parse(json), new UpdateOptions().upsert(true));

    }

    public static void loadQuests(mPlayer mp) {
        for(Quest q : Main.getInstance().getManager().dupeQuests()) {
            q.setPlayer(mp.getId().toString());
            mp.addQuest(q);
        }
        save(mp);
    }

}
