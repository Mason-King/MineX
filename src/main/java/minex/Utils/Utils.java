package minex.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Utils {

    public static String toString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
    }

    public static Location fromString(String s) {
        String[] split = s.split(";");
        return new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
    }

}
