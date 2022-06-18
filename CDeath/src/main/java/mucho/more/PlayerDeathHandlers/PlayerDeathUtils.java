package mucho.more.PlayerDeathHandlers;

import mucho.more.CDeath;
import mucho.more.MuchoHelpers.CheckForNull;
import mucho.more.MuchoHelpers.MuchoDebuger;
import mucho.more.MuchoHelpers.MuchoSerializer;
import mucho.more.MuchoHelpers.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathUtils {
    private Player p;
    private UUID playerID;
    private String playerName;
    private String reviverOrKiller;
    private final CDeath plugin = CDeath.getPlugin(CDeath.class);
    public PlayerDeathUtils(Player p,@Nullable String reviverOrKiller){
        this.p = p;
        this.playerID = p.getUniqueId();
        this.playerName = p.getName();
        this.reviverOrKiller = reviverOrKiller;
    }

    /**
     * Either create players grave and freeze him or
     * kills him for second time (this time for good)
     */
    public void handlePlayerDeath(){
        if(isPlayerDeadAlready()){
           smashPlayer();
        }else{
           freezePlayer();
        }
    }

    /**
     * Returns if player is already in "armorstand" state
     */
    public boolean isPlayerDeadAlready(){
        for(UUID id: plugin.getDataHolder().getDeathPlayersIds()){
            if(id.equals(playerID))return true;
        }
        return false;
    }

    /**
     * Creates armorstand with customname at player death location
     * and adds Armorstand id to map together with playerId
     */
    private void spawnArmorStandGrave(){
        ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().clone().add(0,1.5,0), EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setCustomNameVisible(true);
        MuchoDebuger.broadcast("Spawn name?");
        armorStand.setCustomName(craftCustomAsName());
        MuchoDebuger.broadcast("Spawn name?1");
        plugin.getDataHolder().addPlayerArmorStandId(playerID,armorStand.getUniqueId());
        MuchoDebuger.broadcast("Spawn name?2");
    }

    /**
     * Removes armorstand that marks player death place
     */
    private void removeArmorStandGrave(){
        UUID armorStandID = plugin.getDataHolder().getPlayerArmorStandID(playerID);
        if(armorStandID==null)return;
        Entity armorStand = Bukkit.getEntity(armorStandID);
        if(armorStand!=null){
            armorStand.remove();
            plugin.getDataHolder().removePlayerArmorStandId(playerID);
            return;
        }else{
            MuchoDebuger.error("Could not found grave armorstand for playerID: "+playerID);
        }
    }
    /**
     * Removes bats on which player rides after death
     */
    private void removeBat(){
        UUID batID = plugin.getDataHolder().getPlayerBatID(playerID);
        if(batID==null)return;
        Entity bat = Bukkit.getEntity(batID);
        if(bat!=null){
            bat.remove();
            plugin.getDataHolder().removePlayerBatID(playerID);
            return;
        }else{
            MuchoDebuger.error("Could not found bat for playerID: "+playerID);
        }
    }

    /**
     * Returns armorstand that's closest to given location or null if didnt found any
     */
    @CheckForNull
    private Entity findNearestArmorstand(Location loc){
        Collection<Entity> collection = loc.getWorld().getNearbyEntities(loc.add(-4,-4,-4),8,8,8);
        Entity as = null;
        for(Entity e:collection){
            if(!(e instanceof ArmorStand))continue;
            if(as!=null){
                double currentDistance =  loc.distance(as.getLocation());
                double newDistance = loc.distance(e.getLocation());
                if(newDistance<currentDistance){
                    as = e;
                }
            }else{
                as = e;
            }
        }
        return as;
    }
    /**
     * Returns player grave location or null if not found any
     */
    @CheckForNull
    private Location getGraveLocation(){
        for(Map.Entry<UUID,String> entry: plugin.getDataHolder().getDeathLocPlayerIdPairMap().entrySet()){
            String locString = entry.getValue();
            UUID id = entry.getKey();
            if(id.equals(playerID))return MuchoSerializer.deserializeLocation(locString);
        }
        return null;
    }

    /**
     * Spawns invisible bat with no ai and sets player as its passenger
     */
    private void spawnMagicalBat(){
        Bat bat = (Bat) p.getWorld().spawnEntity(p.getLocation().clone().add(0,-1,0), EntityType.BAT);
        bat.setInvisible(true);
        bat.setAI(false);
        bat.setInvulnerable(true);
        bat.addPassenger(p);
        plugin.getDataHolder().addPlayerBatID(playerID,bat.getUniqueId());
    }
    private String craftCustomAsName(){
        return plugin.getDataHolder().getHologramName(playerName);
    }

    /**
     * Handle player death in freeze state
     */
    public void smashPlayer(){
        removeArmorStandGrave();
        removeBat();
        cancelPlayerSmashRunnable();
        removeBlindness();
        plugin.getDataHolder().removePlayerDeathLoc(playerID);
        doSmashThings();
    }

    /**
     * Puts player in freeze state
     */
    public void freezePlayer(){
        MuchoDebuger.broadcast("CD1");
        spawnArmorStandGrave();
        MuchoDebuger.broadcast("CD2");
        spawnMagicalBat();
        MuchoDebuger.broadcast("CD3");
        applyBlindness();
        MuchoDebuger.broadcast("CD4");
        plugin.getDataHolder().addPlayerDeathLoc(playerID,p.getLocation());
        int runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                smashPlayer();
            }
        },plugin.getDataHolder().getDeathTimer()* 20L);
        plugin.getDataHolder().addSmashPlayerTaskId(playerID,runnableID);
        doFreezeThings();
    }
    public void doFreezeThings(){
        if(plugin.getDataHolder().sendMessageOnFreeze()){
            String message = plugin.getDataHolder().getMessageOnFreeze(reviverOrKiller);
            if(message!=null){
                p.sendMessage(message);
            }
        }
        if(plugin.getDataHolder().playSoundOnFreeze()){
            Sound sound = plugin.getDataHolder().getSoundOnFreeze();
            if(sound!=null){
                p.playSound(p.getLocation(),sound,10f,1f);
            }
        }
    }
    public void revivePlayer(){
        removeArmorStandGrave();
        removeBat();
        removeBlindness();
        cancelPlayerSmashRunnable();
        plugin.getDataHolder().removePlayerDeathLoc(playerID);
        doReviveThings();
    }
    private void doSmashThings(){
        if(p==null||!p.isOnline())return;
        if(plugin.getDataHolder().sendMessageOnSmash()){
            String message = plugin.getDataHolder().getMessageOnSmash(reviverOrKiller);
            if(message!=null){
                p.sendMessage(message);
            }
        }
        if(plugin.getDataHolder().playSoundOnSmash()){
            Sound sound = plugin.getDataHolder().getSoundOnSmash();
            if(sound!=null){
                p.playSound(p.getLocation(),sound,10f,1f);
            }
        }
        p.setHealth(0);
    }
    private void doReviveThings(){
        if(p==null||!p.isOnline())return;
        if(plugin.getDataHolder().sendMessageOnRevive()){
            String message = plugin.getDataHolder().getMessageOnRevive(reviverOrKiller);
            if(message!=null){
                p.sendMessage(message);
            }
        }
        if(plugin.getDataHolder().playSoundOnRevive()){
            Sound sound = plugin.getDataHolder().getSoundOnRevive();
            if(sound!=null){
                p.playSound(p.getLocation(),sound,10f,1f);
            }
        }
        if(plugin.getDataHolder().setHealthOnRevive()){
            double health = plugin.getDataHolder().getHealthOnRevive();
            double maxHealth = 20;
            try {
                maxHealth =  p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            }catch (Exception ignored){

            }
            if(health>maxHealth){
                health = maxHealth;
            }
            if(health!=0){
                p.setHealth(health);
            }
        }
        if(plugin.getDataHolder().setHungerOnRevive()){
            int hunger = plugin.getDataHolder().getHungerOnRevive();
            int maxHunger = 20;
            if(hunger>maxHunger){
                hunger = maxHunger;
            }
            p.setFoodLevel(hunger);
        }
        if(plugin.getDataHolder().playSoundOnRevive()){
            Sound sound = plugin.getDataHolder().getSoundOnRevive();
            if(sound!=null){
                p.playSound(p.getLocation(),sound,10f,1f);
            }
        }
        if(plugin.getDataHolder().applyEffectsOnRevive()){
            List<PotionEffect> effects = plugin.getDataHolder().getEffectsOnRevive();
            MuchoDebuger.broadcast("Lista efektow rozmiar: "+effects.size());
            for(PotionEffect e: effects){
                if(e==null)continue;
                try {
                    p.addPotionEffect(e);
                }catch (Exception ignored){

                }
            }
        }
    }
    private void applyBlindness(){
        Player p = Bukkit.getPlayer(playerID);
        if(p==null||!p.isOnline())return;
        int duration = plugin.getDataHolder().getDeathTimer() *20;
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,duration,3,true,false));
    }
    private void removeBlindness(){
        Player p = Bukkit.getPlayer(playerID);
        if(p==null||!p.isOnline())return;
        p.removePotionEffect(PotionEffectType.BLINDNESS);
    }
    public void cancelPlayerSmashRunnable(){
        int runnableID = plugin.getDataHolder().getSmashPlayerTaskId(playerID);
        Bukkit.getScheduler().cancelTask(runnableID);
        plugin.getDataHolder().removeSmashPlayerTaskID(playerID);
    }
}
