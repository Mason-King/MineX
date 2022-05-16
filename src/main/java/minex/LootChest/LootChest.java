package minex.LootChest;

import minex.Game.Game;
import minex.Main;
import minex.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LootChest {

    private String location;
    private String game;
    private LootType type;
    private int maxItems;
    private int minItems;

    public LootChest(String location, String game, LootType type, int maxItems, int minItems) {
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
        this.maxItems = Main.getInstance().getConfig().getInt("lootChest.settings." + type.toString() + ".maxItems");
        this.minItems = Main.getInstance().getConfig().getInt("lootChest.settings." + type.toString() + ".minItems");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
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

    public void fill() {
        Location loc = Utils.fromString(this.location);
        Block b = loc.getBlock();
        Chest chest = (Chest) b.getState();
        Inventory inventory = chest.getBlockInventory();

        inventory.clear();

        int items = ThreadLocalRandom.current().nextInt(minItems, maxItems);

        for(int i = 0; i < items; i++) {

            inventory.setItem(ThreadLocalRandom.current().nextInt(0, inventory.getSize()), Items.getItems(type).get(ThreadLocalRandom.current().nextInt(0, Items.getItems(type).size())).getItemStack() );
        }

    }

}
