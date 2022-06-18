package mucho.more.Listeners;

import mucho.more.PlayerDeathHandlers.PlayerDeathUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnInteract implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        PlayerDeathUtils pdu = new PlayerDeathUtils(p,null);
        if(!pdu.isPlayerDeadAlready())return;
        e.setCancelled(true);
    }
}
