package mucho.more.PlayerDeathHandlers;

import mucho.more.CDeath;
import mucho.more.MuchoHelpers.CheckForNull;
import mucho.more.MuchoHelpers.MuchoDebuger;
import mucho.more.MuchoHelpers.MuchoSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathUtils {
    private Player p;
    private UUID playerID;
    private String playerName;
    private final CDeath plugin = CDeath.getPlugin(CDeath.class);
    public PlayerDeathUtils(Player p){
        this.p = p;
        this.playerID = p.getUniqueId();
        this.playerName = p.getName();
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
        ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(craftCustomAsName());
        plugin.getDataHolder().addPlayerArmorStandId(playerID,armorStand.getUniqueId());
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
        Bat bat = (Bat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
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
        doSmashThings();
    }

    /**
     * Puts player in freeze state
     */
    public void freezePlayer(){
        spawnArmorStandGrave();
        spawnMagicalBat();
        applyBlindness();
        int runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                smashPlayer();
            }
        },plugin.getDataHolder().getDeathTimer()* 20L);
        plugin.getDataHolder().addSmashPlayerTaskId(playerID,runnableID);
    }

    public void revivePlayer(){
        removeArmorStandGrave();
        removeBat();
        removeBlindness();
        cancelPlayerSmashRunnable();
        doReviveThings();
    }
    private void doSmashThings(){
        ?
    }
    private void doReviveThings(){
        ?
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
