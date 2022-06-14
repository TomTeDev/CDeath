package mucho.more.Listeners;

import mucho.more.PlayerDeathHandlers.PlayerDeathUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player p))return;
        double damage = e.getDamage();
        double playerHealth = p.getHealth();
        if(damage<playerHealth)return;
        e.setCancelled(true);
        new PlayerDeathUtils(p).handlePlayerDeath();
    }
}
