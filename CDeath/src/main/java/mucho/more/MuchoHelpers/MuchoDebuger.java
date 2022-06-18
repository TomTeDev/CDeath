package mucho.more.MuchoHelpers;

import mucho.more.CDeath;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class MuchoDebuger {
    private static CDeath plugin = CDeath.getPlugin(CDeath.class);
    public static void error(String message){
        plugin.getLogger().log(Level.SEVERE,ChatColor.RED+message);
    }
    public static void warning(String message){
        plugin.getLogger().log(Level.WARNING,ChatColor.YELLOW+message);
    }
    public static void info(String message){
        plugin.getLogger().log(Level.INFO,ChatColor.YELLOW+message);
    }
    public static void broadcast(String message){
        Bukkit.broadcastMessage(message);
    }
}
