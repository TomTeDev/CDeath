package mucho.more.Commands;

import mucho.more.CDeath;
import mucho.more.MuchoHelpers.MuchoDebuger;
import mucho.more.PlayerDeathHandlers.ItemsUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BaseCommands implements CommandExecutor {
    private CDeath plugin = CDeath.getPlugin(CDeath.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player testPlayer){
            permisje
            if(!testPlayer.isOp())return false;
        }
        if(args.length==0||(args.length==1&&args[0].equals("help"))){
            if(sender instanceof Player p){
                p.sendMessage(ChatColor.BLUE+"/cdeath [help]"+ChatColor.GRAY+" - "+ChatColor.YELLOW+"shows available commands");
                p.sendMessage(ChatColor.BLUE+"/cdeath [reload]"+ChatColor.GRAY+" - "+ChatColor.YELLOW+"reloads config");
                p.sendMessage(ChatColor.BLUE+"/cdeath [give][player_name][amount] "+ChatColor.GRAY+" - "+ChatColor.YELLOW+"gives certain amount of revive items to player (or just one if not set)");
                return true;
            }else{
                MuchoDebuger.info(ChatColor.BLUE+"/cdeath [help]"+ChatColor.GRAY+" - "+ChatColor.YELLOW+"shows available commands");
                MuchoDebuger.info(ChatColor.BLUE+"/cdeath [reload]"+ChatColor.GRAY+" - "+ChatColor.YELLOW+"reloads config");
                MuchoDebuger.info(ChatColor.BLUE+"/cdeath [give][player_name][amount] "+ChatColor.GRAY+" - "+ChatColor.YELLOW+"gives certain amount of revive items to player (or just one if not set)");
                return true;
            }
        }
        if(args.length==1&&args[0].equals("reload")){
            plugin.getDataHolder().reloadConfig();
            sender.sendMessage(ChatColor.GREEN+"Config reloaded!");
            return true;
        }
        if(args.length>=2&&args[0].equals("give")){
            int amount = 1;
            if(args.length==3){
                try {
                    amount = Integer.parseInt(args[2]);
                }catch (Exception e){
                    sender.sendMessage(ChatColor.RED+"Specify proper amount of items to give!");
                    return true;
                }
            }
            Player g = Bukkit.getPlayer(args[1]);
            if(g==null||!g.isOnline()){
                sender.sendMessage(ChatColor.RED+"This player does not exist!");
                return true;
            }
            ItemStack reviveItem = new ItemsUtils().getReviveItem();
            reviveItem.setAmount(amount);
            HashMap<Integer,ItemStack> map = g.getInventory().addItem(reviveItem);
            if(!map.isEmpty()){
                sender.sendMessage(ChatColor.YELLOW+"Player didnt receive item bcs he has no space in his inventory");
                return true;
            }
            if(plugin.getDataHolder().itemReciverMessageEnabled()){
                sendReciveItemMessage(g);
            }
            if(plugin.getDataHolder().itemGiverMessageEnabled()){
                sender.sendMessage(ChatColor.GREEN+"Player received an item!");
            }
        }
        return false;
    }
    private void sendReciveItemMessage(Player g){
        String message = plugin.getDataHolder().getReciveItemMessage();
        if(message==null)return;
        if(plugin.getDataHolder().recivingItemSoundEnabled()){
            Sound sound  = plugin.getDataHolder().getRecivingItemSound();
            if(sound!=null){
                g.playSound(g.getLocation(),sound,5f,1f);
            }
        }
        g.sendMessage(message);
    }
}
