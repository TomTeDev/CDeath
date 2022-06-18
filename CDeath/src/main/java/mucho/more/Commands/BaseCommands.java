package mucho.more.Commands;

import mucho.more.CDeath;
import mucho.more.MuchoHelpers.MuchoDebuger;
import mucho.more.PlayerDeathHandlers.ItemsUtils;
import mucho.more.PlayerDeathHandlers.PlayerDeathUtils;
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
        if(args.length==0||(args.length==1&&args[0].equals("help"))){
            if(sender instanceof Player p){
                if (!p.hasPermission("cdeath.help")) {
                    p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                    return false;
                }
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
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.reload")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
            plugin.getDataHolder().reloadConfig();
            sender.sendMessage(ChatColor.GREEN+"Config reloaded!");
            return true;
        }
        if(args.length>=2&&args[0].equals("give")){
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.give")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
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
        if(args.length==1&&args[0].equals("disable")){
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.disable")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
            if(plugin.turnMeOff){
                plugin.turnMeOff = false;
                sender.sendMessage(ChatColor.GREEN+"Plugin is now off");
            }else{
                plugin.turnMeOff = true;
                sender.sendMessage(ChatColor.GREEN+"Plugin is working now");
            }
            return true;
        }
        if(args.length==1&&args[0].equals("reviveall")){
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.reviveall")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
            for(Player p: Bukkit.getOnlinePlayers()){
                PlayerDeathUtils pdu = new PlayerDeathUtils(p,"ADMIN");
                if(!pdu.isPlayerDeadAlready())continue;
                pdu.revivePlayer();
            }
            sender.sendMessage(ChatColor.GREEN+"All players revived!");
            return true;
        }
        if(args.length>=2&&args[0].equals("revive")){
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.revive")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
            Player revived = Bukkit.getPlayer(args[1]);
            if(revived==null||!revived.isOnline()){
                sender.sendMessage(ChatColor.GREEN+"Player not found!");
                return true;
            }
            PlayerDeathUtils pdu = new PlayerDeathUtils(revived,"ADMIN");
            if(!pdu.isPlayerDeadAlready()){
                sender.sendMessage(ChatColor.YELLOW+"This player is not in freeze state");
                return true;
            }
            pdu.revivePlayer();
            sender.sendMessage(ChatColor.GREEN+"Player revived!");
            return true;
        }
        if(args.length>=2&&args[0].equals("freeze")){
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.freeze")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
            Player revived = Bukkit.getPlayer(args[1]);
            if(revived==null||!revived.isOnline()){
                sender.sendMessage(ChatColor.GREEN+"Player not found!");
                return true;
            }
            PlayerDeathUtils pdu = new PlayerDeathUtils(revived,"ADMIN");
            if(pdu.isPlayerDeadAlready()){
                sender.sendMessage(ChatColor.YELLOW+"This is already in freeze state!");
                return true;
            }
            pdu.freezePlayer();
            sender.sendMessage(ChatColor.GREEN+"Player freezed!");
            return true;
        }
        if(args.length>=2&&args[0].equals("smash")){
            if ((sender instanceof Player p)&&!p.hasPermission("cdeath.smash")) {
                p.sendMessage(ChatColor.RED+"You do not have permission to use that command");
                return false;
            }
            Player revived = Bukkit.getPlayer(args[1]);
            if(revived==null||!revived.isOnline()){
                sender.sendMessage(ChatColor.GREEN+"Player not found!");
                return true;
            }
            PlayerDeathUtils pdu = new PlayerDeathUtils(revived,"ADMIN");
            if(!pdu.isPlayerDeadAlready()){
                sender.sendMessage(ChatColor.YELLOW+"This player is not in freeze state");
                return true;
            }
            pdu.smashPlayer();
            sender.sendMessage(ChatColor.GREEN+"Player smashed");
            return true;
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
