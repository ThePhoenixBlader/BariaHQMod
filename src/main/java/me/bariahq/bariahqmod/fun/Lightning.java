package me.bariahq.bariahqmod.fun;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.shop.ShopData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SuppressWarnings("LocalVariableHidesMemberVariable")
public class Lightning extends FreedomService
{
    public static List<Player> lpl = new ArrayList();
    public static int amount = 1;
    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    public Lightning(BariaHQMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        Location l = p.getTargetBlock((Set<Material>) null, 600).getLocation();
        ShopData sd = plugin.sh.getData(p);
        if (sd.isThorHammer() && event.getItem() != null && event.getItem().equals(getThorHammer()))
        {
            // Cool down time in seconds
            long cooldownTime = 5;
            if (cooldowns.containsKey(p.getName()))
            {
                long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0)
                {
                    p.sendMessage(ChatColor.RED + "You can't use Thor's hammer for another " + secondsLeft + " seconds!");
                    return;
                }
            }
            cooldowns.put(p.getName(), System.currentTimeMillis());
            p.getWorld().strikeLightning(l);
        }
        else if (lpl.contains(p))
        {
            for (int i = 0; i < amount; i++)
            {
                p.getWorld().strikeLightning(l);
            }
        }
    }

    public ItemStack getThorHammer()
    {
        ItemStack hammer = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta hammerMeta = hammer.getItemMeta();
        hammerMeta.setDisplayName(ChatColor.RED + "Thor's Hammer");
        List<String> lore = new ArrayList();
        lore.add(ChatColor.BLUE + "Use this to smite down lil' shits");
        lore.add(ChatColor.BLUE + "that want to get on your level");
        hammerMeta.setLore(lore);
        hammerMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
        hammerMeta.setUnbreakable(true);
        hammer.setItemMeta(hammerMeta);
        return hammer;
    }
}
