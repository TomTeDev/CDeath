package mucho.more.MuchoHelpers;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

public class ConfigHandler {
    private final FileConfiguration cfg;
    public final String basePath = "data";

    public ConfigHandler(FileConfiguration cfg) {
        this.cfg = cfg;

    }
    public FileConfiguration getCfg(){
        return this.cfg;
    }

    public String craftPath(String path){
        return basePath+"."+path;
    }

    public int getInt(String path,int reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getInt(craftPath(path)):reserveValue;
    }

    public String getString(String path,String reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getString(craftPath(path)):reserveValue;
    }

    public boolean getBoolean(String path,boolean reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getBoolean(craftPath(path)):reserveValue;
    }

    public List<String> getStringList(String path,List<String> reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getStringList(craftPath(path)):reserveValue;
    }

    public List<Integer> getIntList(String path,List<Integer> reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getIntegerList(craftPath(path)):reserveValue;
    }

    public List<Integer> getIntegerList(String path,List<Integer> reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getIntegerList(craftPath(path)):reserveValue;
    }

    public List<Float> getFloatList(String path,List<Float> reserveValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getFloatList(craftPath(path)):reserveValue;
    }

    public double getDouble(String path, double reservedValue){
        return (cfg.contains(craftPath(path))&&(cfg.get(craftPath(path))!=null))?cfg.getDouble(craftPath(path)):reservedValue;
    }
}



