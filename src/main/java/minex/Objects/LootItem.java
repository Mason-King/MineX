package minex.Objects;

import org.bukkit.inventory.ItemStack;

public class LootItem {

    private ItemStack itemStack;
    public LootItem(ItemStack stack) {
        this.itemStack = stack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }


}
