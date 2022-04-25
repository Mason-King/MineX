package minex.LootChest;

import minex.Game.Game;
import minex.Main;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LootChest {

    Main main = Main.getInstance();

    private String location;
    private Game game;
    private LootType type;
    private int maxItems;
    private int minItems;

    public LootChest(String location, Game game, LootType type, int maxItems, int minItems) {
        this.location = location;
        this.game = game;
        this.type = type;
        this.maxItems = maxItems;
        this.minItems = minItems;
    }

    public LootChest(String location, LootType type) {
        this.location = location;
        this.type = type;

        loadMinMax();
    }

    public void loadMinMax() {
        this.maxItems = main.getConfig().getInt("lootChest." + type.toString() + ".maxItems");
        this.minItems = main.getConfig().getInt("lootChest." + type.toString() + ".minItems");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LootType getType() {
        return type;
    }

    public void setType(LootType type) {
        this.type = type;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

}
