package mucho.more;

import mucho.more.DataContainer.DataHolder;
import org.bukkit.plugin.java.JavaPlugin;

public final class CDeath extends JavaPlugin {
    private DataHolder dataHolder;
    private CDeath plugin;
    @Override
    public void onEnable() {
        this.dataHolder = new DataHolder();
        this.dataHolder.loadFiles();
        plugin = this;

    }

    @Override
    public void onDisable() {
        this.dataHolder.saveFiles();
        plugin = null;
    }
    public DataHolder getDataHolder(){
        return this.dataHolder;
    }
}
