package minex.Game;

import minex.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Game {

    //Likely to add some more info here!
    private String id;
    private boolean started;
    private boolean full;
    private int players;
    private int maxPlayers;
    private String lobbySpawn;
    private List<String> spawnLocations =  new ArrayList<>();

    File file = new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "game.yml");
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    public Game(String id, boolean started, boolean full, int players, int maxPlayers, String lobbySpawn, List<String> allSpawns) {
        this.id = id;
        this.started = started;
        this.full = full;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.lobbySpawn = lobbySpawn;
        this.spawnLocations = allSpawns;
    }

    public Game(String id) {
        this.id = id;
        this.started = false;
        this.full = false;
        this.players = 0;
        this.maxPlayers = 25;
        this.lobbySpawn = "";
        this.spawnLocations = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(String lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public void addSpawn(String s) {
        this.spawnLocations.add(s);
    }

    public List<String> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(List<String> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }
}
