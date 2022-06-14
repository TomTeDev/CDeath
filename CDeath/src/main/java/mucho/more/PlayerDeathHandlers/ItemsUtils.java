package mucho.more.PlayerDeathHandlers;

import mucho.more.CDeath;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemsUtils {
    private CDeath plugin = CDeath.getPlugin(CDeath.class);
    public ItemStack getReviveItem(){
        Material m = plugin.getDataHolder().getItemMaterial();
        String name = plugin.getDataHolder().getItemName();
        List<String> lore = plugin.getDataHolder().getItemLore();
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public boolean isThatReviveItem(ItemStack item){
        return item.isSimilar(getReviveItem());
    }
}
