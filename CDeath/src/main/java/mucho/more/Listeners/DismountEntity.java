package mucho.more.Listeners;

import mucho.more.MuchoHelpers.MuchoDebuger;
import mucho.more.PlayerDeathHandlers.PlayerDeathUtils;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class DismountEntity implements Listener {
    @EventHandler
    public void onEntityDismount(EntityDismountEvent e){
        if(!(e.getDismounted() instanceof Bat))return;
        MuchoDebuger.broadcast("Its bat!");
        if(!(e.getEntity() instanceof Player p))return;
        PlayerDeathUtils pdu = new PlayerDeathUtils(p,null);
        if(!pdu.isPlayerDeadAlready())return;
        e.setCancelled(true);
    }
}
