package mucho.more.DataContainer;

import mucho.more.MuchoHelpers.*;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DataHolder {
    private String highlyConfigurableConfigName = "highlyConfigurableConfig.yml";
    private FileConfiguration highlyConfigurableConfig = null;
    private ConfigHandler ch = null;
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
        ch = new ConfigHandler(highlyConfigurableConfig);
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
        return ch.getInt("deathTimer",0);
    }
    public String getHologramName(String playerName){
        String val = ch.getString("hologramName",null);
        if(val==null)return "Here is dead "+playerName;
        return craftMessage(val,playerName);
    }
    public Material getItemMaterial(){
        String val = ch.getString("reviveItemMaterial",null);
        if(val==null)return Material.GOLDEN_APPLE;
        Material material = Material.GOLDEN_APPLE;
        try {
            material = Material.valueOf(val);
        }catch (Exception ignored){

        }
        return material;
    }
    public String getItemName(){
        String val = ch.getString("reviveItemName","ReviveItem");
        return craftMessage(val,null);
    }
    public List<String> getItemLore(){
        List<String> list = ch.getStringList("reviveItemLore",new ArrayList<>());
        List<String> colloredList = new ArrayList<>();
        for(String s : list){
            colloredList.add(craftMessage(s,null));
        }
        return colloredList;
    }
    public boolean itemReciverMessageEnabled(){
        return ch.getBoolean("messageOnReceiveItemEnable",false);
    }
    public String getReciveItemMessage(){
        return craftMessage(ch.getString("messageOnReceiveItem",null),null);
    }
    public boolean itemGiverMessageEnabled(){
        return ch.getBoolean("messageOnGivingItemEnable",false);
    }
    public boolean recivingItemSoundEnabled(){
        return ch.getBoolean("soundOnReceiveItemEnabled",false);
    }
    public Sound getRecivingItemSound(){
        String s = ch.getString("soundOnReceiveItem",null);
        Sound sound = getSoundByString(s);
        return sound;
    }
    public boolean playSoundToReviver(){
        return ch.getBoolean("soundOnRevivingSm1Enabled",false);
    }
    public Sound getReviverSound(){
        String s = ch.getString("soundOnRevivingSm1",null);
        Sound sound = getSoundByString(s);
        return sound;
    }
    public boolean sendMessageToReviver(){
        return ch.getBoolean("messageOnRevivingSm1Enabled",false);
    }
    public String getReviverMessageAfterRevive(String revivedName){
        String message = ch.getString("messageOnRevivingSm1",null);
        if(message==null)return null;
        return craftMessage(message,revivedName);
    }
    public boolean sendMessageOnSmash(){
        return ch.getBoolean("messageOnSmashEnabled",false);
    }
    public String getMessageOnSmash(String smasher){
        String message = ch.getString("messageOnSmash",null);
        if(message==null)return null;
        return craftMessage(message,smasher);
    }
    public boolean playSoundOnSmash(){
        return ch.getBoolean("soundOnSmashEnabled",false);
    }
    public Sound getSoundOnSmash(){
        String s = ch.getString("soundOnSmash",null);
        Sound sound = getSoundByString(s);
        return sound;
    }
    public boolean sendMessageOnRevive(){
        return ch.getBoolean("messageOnBeingRevivedEnabled",false);
    }
    public String getMessageOnRevive(String reviverName){
        String message = ch.getString("messageOnBeingRevived",null);
        if(message==null)return null;
        return craftMessage(message,reviverName);
    }
    public boolean playSoundOnRevive(){
        return ch.getBoolean("soundOnBeingRevivedEnabled",false);
    }
    public Sound getSoundOnRevive(){
        String s = ch.getString("soundOnBeingRevived",null);
        Sound sound = getSoundByString(s);
        return sound;
    }
    public boolean setHealthOnRevive(){
        return ch.getBoolean("setHealthOnReviveEnabled",false);
    }
    public double getHealthOnRevive(){
        return ch.getDouble("setHealthOnRevive",0);
    }
    public boolean setHungerOnRevive(){
        return ch.getBoolean("setFoodLvlOnReviveEnabled",false);
    }
    public int getHungerOnRevive(){
        return ch.getInt("setFoodLvlOnRevive",0);
    }
    public boolean applyEffectsOnRevive(){
        return ch.getBoolean("effectsOnReviveEnabled",false);
    }
    public List<PotionEffect>getEffectsOnRevive(){
        List<PotionEffect> list = new ArrayList<>();
        String basePath = "effectsOnRevive";
        ConfigurationSection section =  ch.getCfg().getConfigurationSection("data.effectsOnRevive");
        if(section==null||section.getKeys(false).isEmpty())return list;
        for(String s:section.getKeys(false)){
            String name = ch.getString(basePath+"."+s+".name","");
            PotionEffectType type = null;
            for(PotionEffectType t: PotionEffectType.values()){
                if(t.getName().equalsIgnoreCase(name)){
                    type = t;
                    break;
                }
            }
            if(type==null)continue;
            int dur_seconds = ch.getInt(basePath+"."+s+".dur_seconds",0);
            if(dur_seconds==0)continue;
            int amplifier = ch.getInt(basePath+"."+s+".amplifier",0);
            if(amplifier==0){
                amplifier = 1;
            }
            list.add(new PotionEffect(type,dur_seconds*20,amplifier));
        }
        return list;
    }
    public boolean sendMessageOnFreeze(){
        return ch.getBoolean("messageOnFreezeEnabled",false);
    }
    public String getMessageOnFreeze(String killer){
        String message = ch.getString("messageOnFreeze",null);
        if(message==null)return null;
        return craftMessage(message,killer);
    }
    public boolean playSoundOnFreeze(){
        return ch.getBoolean("soundOnFreezeEnabled",false);
    }
    public Sound getSoundOnFreeze(){
        String s = ch.getString("soundOnFreeze",null);
        Sound sound = getSoundByString(s);
        return sound;
    }
    private Sound getSoundByString(String soundString){
        if(soundString==null)return null;
        Sound sound = Sound.ENTITY_PLAYER_BURP;
        try {
            sound = Sound.valueOf(soundString);
        }catch (Exception ignored){

        }
        return sound;
    }
    private String craftMessage(String message,String playerName){
        if(message==null){
            return null;
        }
        if(playerName!=null){
            message = message.replace("%player%",playerName);
        }
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}
