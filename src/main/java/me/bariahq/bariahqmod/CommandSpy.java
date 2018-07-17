package me.bariahq.bariahqmod;

import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy extends FreedomService
{

    public CommandSpy(BariaHQMod plugin)
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        for (Player player : server.getOnlinePlayers())
        {
            if (plugin.al.isStaffMember(player) && plugin.al.getStaffMember(player).isCommandSpy())
            {
                if (plugin.al.isStaffMember(event.getPlayer()) && !plugin.al.isSeniorAdmin(player))
                {
                    continue;
                }

                if (player != event.getPlayer())
                {
                    FUtil.playerMsg(player, event.getPlayer().getName() + ": " + event.getMessage());
                }
            }
        }
    }
}
