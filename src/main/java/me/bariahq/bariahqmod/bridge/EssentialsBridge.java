package me.bariahq.bariahqmod.bridge;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.command.Command_vanish;
import me.bariahq.bariahqmod.util.FLog;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class EssentialsBridge extends FreedomService
{

    private Essentials essentialsPlugin = null;

    public EssentialsBridge(BariaHQMod plugin)
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
        Command_vanish.VANISHED.clear();
    }

    public Essentials getEssentialsPlugin()
    {
        if (essentialsPlugin == null)
        {
            try
            {
                final Plugin essentials = Bukkit.getServer().getPluginManager().getPlugin("UMC-Essentials");
                if (essentials != null)
                {
                    if (essentials instanceof Essentials)
                    {
                        essentialsPlugin = (Essentials) essentials;
                    }
                }
            }
            catch (Exception ex)
            {
                FLog.severe(ex);
            }
        }
        return essentialsPlugin;
    }

    public User getEssentialsUser(String username)
    {
        try
        {
            final Essentials essentials = getEssentialsPlugin();
            if (essentials != null)
            {
                return essentials.getUserMap().getUser(username);
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
        return null;
    }

    public void setNickname(String username, String nickname)
    {
        try
        {
            final User user = getEssentialsUser(username);
            if (user != null)
            {
                user.setNickname(nickname);
                user.setDisplayNick();
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
    }

    public String getNickname(String username)
    {
        try
        {
            final User user = getEssentialsUser(username);
            if (user != null)
            {
                return user.getNickname();
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
        return null;
    }
    
    public String getDisplayName(String username)
    {
        String name = getNickname(username);
        if (name == null)
        {
            name = username;
        }
        return name;
    }

    public long getLastActivity(String username)
    {
        try
        {
            final User user = getEssentialsUser(username);
            if (user != null)
            {
                return FUtil.<Long>getField(user, "lastActivity"); // This is weird
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
        return 0L;
    }
    
    public void setVanished(String username, boolean vanished)
    {
        try
        {
            User user = getEssentialsUser(username);
            if (user != null)
            {
                user.setVanished(vanished);
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if (Command_vanish.VANISHED.contains(player))
        {
            Command_vanish.VANISHED.remove(player);
        }
    }

    public boolean isEssentialsEnabled()
    {
        try
        {
            final Essentials essentials = getEssentialsPlugin();
            if (essentials != null)
            {
                return essentials.isEnabled();
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
        return false;
    }
}
