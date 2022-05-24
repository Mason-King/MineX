package minex.Events;

import minex.Objects.Game;
import minex.Objects.LootChest;
import minex.Enums.LootType;
import minex.Managers.GameManager;
import minex.Enums.Message;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestPlace implements Listener {

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        Player clicked = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            //placing a block
            if(clicked.getItemInHand().getType().equals(Material.CHEST)) {
                if(!clicked.hasPermission("minex.chest.place")) {
                    clicked.sendMessage(Message.NO_PERMISSION.getMessage());
                    return;
                }
                if(!clicked.getWorld().getName().contains("Game")) {
                    clicked.sendMessage(Message.GAME_WORLD.getMessage());
                }

                ItemStack nbtStack = CraftItemStack.asNMSCopy(clicked.getItemInHand());
                NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());

                if(tag.get("type") != null) {
                    e.setCancelled(true);
                    //make sure it has NBT of the item!
                    LootChest lootChest = new LootChest(Utils.toString(e.getClickedBlock().getLocation().add(0, 1, 0)), LootType.valueOf(tag.getString("type")));
                    e.getClickedBlock().getLocation().getWorld().getBlockAt(e.getClickedBlock().getLocation().add(0, 1, 0)).setType(Material.CHEST);
                    Game game = GameManager.getGame(clicked.getWorld().getName().replace("Game", ""));
                    game.addChest(lootChest);
                    clicked.sendMessage(Message.LOOTCHEST_PLACED.getMessage().replace("{type}", tag.getString("type")));
                }
            }
        }

    }

}
