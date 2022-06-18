package mucho.more;

import mucho.more.Commands.BaseCommands;
import mucho.more.DataContainer.DataHolder;
import mucho.more.Listeners.DismountEntity;
import mucho.more.Listeners.OnInteract;
import mucho.more.Listeners.PlayerDeathListener;
import mucho.more.Listeners.RightClickEntity;
import mucho.more.MuchoHelpers.MuchoDebuger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CDeath extends JavaPlugin {
    private DataHolder dataHolder;
    private CDeath plugin;
    public boolean turnMeOff = true;
    @Override
    public void onEnable() {
        this.dataHolder = new DataHolder();
        this.dataHolder.loadFiles();
        plugin = this;
        setExecutor("cdeath",new BaseCommands());
        registerEvents(this,new RightClickEntity(),new PlayerDeathListener(),new DismountEntity(),
                new OnInteract());
    }

    @Override
    public void onDisable() {
        plugin = null;
    }
    public CDeath getPlugin(){
        return this.plugin;
    }
    public void setExecutor(String command, CommandExecutor executor){
        PluginCommand pluginCommand =  getCommand(command);
        if(pluginCommand==null){
            MuchoDebuger.error("Wrong command: "+command);
            return;
        }
        pluginCommand.setExecutor(executor);
    }
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
    public DataHolder getDataHolder(){
        return this.dataHolder;
    }
}
