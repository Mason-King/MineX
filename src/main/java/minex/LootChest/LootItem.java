package minex.LootChest;

import org.bukkit.inventory.ItemStack;

public class LootItem {

    private ItemStack itemStack;
    private double chance;

    public LootItem(ItemStack stack, double chance) {
        this.itemStack = stack;
        this.chance = chance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

}
