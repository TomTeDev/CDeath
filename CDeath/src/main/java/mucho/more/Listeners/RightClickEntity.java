package mucho.more.Listeners;

import mucho.more.CDeath;
import mucho.more.PlayerDeathHandlers.PlayerDeathUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class RightClickEntity implements Listener {
    private CDeath plugin = CDeath.getPlugin(CDeath.class);
    @EventHandler
    public void onEntityClick(PlayerInteractAtEntityEvent e){
        Entity clickedEntity = e.getRightClicked();
        if(!(clickedEntity instanceof Player revived))return;
        Player reviver = e.getPlayer();
        PlayerDeathUtils pduReviver = new PlayerDeathUtils(reviver,null);
        if(pduReviver.isPlayerDeadAlready())return;
        PlayerDeathUtils pduRevived = new PlayerDeathUtils(revived,reviver.getName());
        if(!pduRevived.isPlayerDeadAlready())return;
        pduRevived.revivePlayer();
        if(plugin.getDataHolder().playSoundToReviver()){
            Sound sound = plugin.getDataHolder().getReviverSound();
            if(sound!=null){
                reviver.playSound(reviver.getLocation(),sound,10f,1f);
            }

        }
        if(plugin.getDataHolder().sendMessageToReviver()){
            String message = plugin.getDataHolder().getReviverMessageAfterRevive(revived.getName());
            if(message!=null){
                reviver.sendMessage(message);
            }
        }
    }
}
