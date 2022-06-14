package mucho.more.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class RightClickEntity implements Listener {
    @EventHandler
    public void onEntityClick(PlayerInteractAtEntityEvent e){
        Entity clickedEntity = e.getRightClicked();
    }
}
