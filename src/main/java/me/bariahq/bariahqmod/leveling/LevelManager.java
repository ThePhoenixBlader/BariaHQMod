package me.bariahq.bariahqmod.leveling;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.shop.ShopData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class LevelManager extends FreedomService
{

    public LevelManager(BariaHQMod plugin)
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

    public Level getLevel(Player player)
    {
        ShopData sd = plugin.sh.getData(player);
        return sd.getLevel();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        Level level = getLevel(player);
        if (!plugin.al.isStaffMember(player))
        {
            plugin.pl.getPlayer(player).setTag(level.getTag());
        }
    }
}
