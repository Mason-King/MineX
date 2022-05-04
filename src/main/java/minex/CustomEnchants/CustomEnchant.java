package minex.CustomEnchants;

import minex.Main;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class CustomEnchant extends Enchantment implements Listener {

    private String name;
    private int maxLevel;
    private int startLevel;
    private EnchantmentTarget target;
    private String description;
    private EnchantmentType type;



    public CustomEnchant(int id, String name, String displayName, int maxLevel, int startLevel, String description, EnchantmentType type) {
        super(id);

        this.name = name;
        this.maxLevel = maxLevel;
        this.startLevel = startLevel;
        this.target = EnchantmentTarget.ALL;
        this.description = description;
        this.type = type;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getStartLevel() {
        return this.getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return this.target;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }

    //Here we will handle all ~26 types lmao
    @EventHandler
    public void arrowTypes(EntityDamageByEntityEvent e) {
//        System.out.println(e.getEntity() + " entity");
//        System.out.println(e.getDamager() + " damager");
//        System.out.println(e.getCause() + " cause");
//        System.out.println(arr.getShooter() + " shooter");
        //check if the cause is not a projectile and go ahead and return
        if(!e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) return;
        //then make sure the damager is an arrow, could be snwoball etc.
        if(!(e.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getDamager();
        if(arrow.getShooter() instanceof Player) {
            Player shooter = (Player) arrow.getShooter();
            ItemStack bow = shooter.getItemInHand();
            //Bow doesnt have this enchant on it no need to continue!
            if(!bow.containsEnchantment(this)) return;
            if(e.getEntity() instanceof Player) {
                // BOW Type - Player hits Player
                // DEFENSE_BOW - Player hits Player
                if(type.equals(EnchantmentType.BOW)) {

                } else if(type.equals(EnchantmentType.DEFENSE_BOW)) {

                }
            } else {
                // BOW_MOB type - Player hits Mob
            }
        } else {
            //a mob
            if(e.getEntity() instanceof Player) {
                // DEFENSE_MOB_BOW - Player gets hit by Mob
            }
        }
    }

}
