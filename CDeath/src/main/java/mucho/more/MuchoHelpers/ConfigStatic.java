package mucho.more.MuchoHelpers;

import mucho.more.CDeath;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigStatic {
    private final Plugin plugin;
    private FileConfiguration fileConfig = null;
    private File file = null;
    String fileName;

    public ConfigStatic(FileConfiguration cfg, String fileName) {
        this.plugin = CDeath.getPlugin(CDeath.class);
        this.fileName = fileName;
        //Create File
        if(fileName==null){
            MuchoDebuger.error("[ConfigStatyczny] There cant be null value in file name!");
            return;
        }
        saveDeafaultConfig();
        //Assign FileConfiguration value
        if(cfg==null){
            try {
                this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
            }catch (Exception e){
                MuchoDebuger.error("[ConfigStatyczny] File cant be created! File name: "+fileName);
            }
        }
        else{
            this.fileConfig = cfg;
        }
    }

    public void reloadConfig() {
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), fileName);
        }
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
        InputStream defaultStream = this.plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.fileConfig.setDefaults(yamlConfiguration);
        }
    }

    public FileConfiguration getConfig() {
        if (this.fileConfig == null) reloadConfig();
        return this.fileConfig;
    }

    public void saveConfig() {
        if (this.fileConfig == null || this.file == null) return;
        try {
            this.getConfig().save(file);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, ChatColor.DARK_RED + "Failed on trying to save "+fileName, exception);
        }
    }

    private void saveDeafaultConfig() {
        MuchoDebuger.broadcast("Save Default config!");
        System.out.println("Save default config");
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), fileName);
        }
        if (!this.file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
