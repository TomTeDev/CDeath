package mucho.more.DataContainer;

import mucho.more.MuchoHelpers.CheckForNull;
import mucho.more.MuchoHelpers.ConfigStatic;
import mucho.more.MuchoHelpers.MuchoDebuger;
import mucho.more.MuchoHelpers.MuchoSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public class DataHolder {
    private String highlyConfigurableConfigName = "highlyConfigurableConfig.yml";
    private FileConfiguration highlyConfigurableConfig = null;
    /**
     * Stores serialized death location and player uuid
     */
    private HashMap<UUID,String> deathLocPlayerIdPair = new HashMap<>();
    private HashMap<UUID,UUID> playerIDArmorStandIDPair = new HashMap<>();
    private HashMap<UUID,UUID> playerIDBatIDPair = new HashMap<>();
    private HashMap<UUID,Integer> playerIDSmashRunnableIDPair = new HashMap<>();

    public void addPlayerArmorStandId(UUID playerID,UUID armorstandID){
        playerIDArmorStandIDPair.put(playerID,armorstandID);
    }
    public void removePlayerArmorStandId(UUID playerID){
        playerIDArmorStandIDPair.remove(playerID);
    }
    @CheckForNull
    public UUID getPlayerArmorStandID(UUID playerID){
        return playerIDArmorStandIDPair.getOrDefault(playerID,null);
    }
    public boolean hasArmorStand(UUID playerID){
        return playerIDArmorStandIDPair.containsKey(playerID);
    }

    public void addPlayerBatID(UUID playerID,UUID batID){
        playerIDBatIDPair.put(playerID,batID);
    }
    public void removePlayerBatID(UUID playerID){
        playerIDBatIDPair.remove(playerID);
    }
    @CheckForNull
    public UUID getPlayerBatID(UUID playerID){
        return playerIDBatIDPair.getOrDefault(playerID,null);
    }
    public boolean hasBat(UUID playerID){
        return playerIDBatIDPair.containsKey(playerID);
    }
    /**
     * Loads deathLocations and configurable config
     */
    public void loadFiles(){
        reloadConfig();
    }
    public void reloadConfig(){
        ConfigStatic configStatic = new ConfigStatic(null,highlyConfigurableConfigName);
        highlyConfigurableConfig = configStatic.getConfig();
    }

    public Collection<UUID> getDeathPlayersIds(){
        return deathLocPlayerIdPair.keySet();
    }
    public HashMap<UUID,String> getDeathLocPlayerIdPairMap(){
        return deathLocPlayerIdPair;
    }
    public void removePlayerDeathLoc(UUID playerID){
        for(Map.Entry<UUID,String> entry: deathLocPlayerIdPair.entrySet()){
            String s = entry.getValue();
            UUID id = entry.getKey();
            if(id.equals(playerID)){
                deathLocPlayerIdPair.remove(id);
                break;
            }
        }
    }
    public void addPlayerDeathLoc(UUID playerID, Location loc){
        String locString = MuchoSerializer.serializeLocation(loc,false);
        deathLocPlayerIdPair.put(playerID,locString);
    }
    public void addSmashPlayerTaskId(UUID playerID,int taskID){
        playerIDSmashRunnableIDPair.put(playerID,taskID);
    }
    public int getSmashPlayerTaskId(UUID playerID){
        return playerIDSmashRunnableIDPair.getOrDefault(playerID,0);
    }
    public void removeSmashPlayerTaskID(UUID playerID){
        playerIDSmashRunnableIDPair.remove(playerID);
    }


    public int getDeathTimer(){

    }
    public String getHologramName(String playerName){
    }
    public Material getItemMaterial(){}
    public String getItemName(){}
    public List<String> getItemLore(){}

}
