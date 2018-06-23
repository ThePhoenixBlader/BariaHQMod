package me.bariahq.bariahqmod.bridge;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.util.FLog;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.LibsDisguises;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LibsDisguisesBridge extends FreedomService
{
    private LibsDisguises libsDisguisesPlugin = null;

    public LibsDisguisesBridge(BariaHQMod plugin)
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

    public LibsDisguises getLibsDisguisesPlugin()
    {
        if (libsDisguisesPlugin == null)
        {
            try
            {
                final Plugin libsDisguises = Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises");
                if (libsDisguises != null)
                {
                    if (libsDisguises instanceof LibsDisguises)
                    {
                        libsDisguisesPlugin = (LibsDisguises) libsDisguises;
                    }
                }
            }
            catch (Exception ex)
            {
                FLog.severe(ex);
            }
        }
        return libsDisguisesPlugin;
    }

    public Boolean isDisguised(Player player)
    {
        try
        {
            final LibsDisguises libsDisguises = getLibsDisguisesPlugin();
            if (libsDisguises != null)
            {
                return DisguiseAPI.isDisguised(player);
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
        return null;
    }

    public void undisguiseAll(Boolean staff)
    {
        try
        {
            final LibsDisguises libsDisguises = getLibsDisguisesPlugin();

            if (libsDisguises == null)
            {
                return;
            }

            for (Player player : server.getOnlinePlayers())
            {
                if (DisguiseAPI.isDisguised(player))
                {
                    if (!staff && plugin.al.isStaffMember(player))
                    {
                        continue;
                    }
                    DisguiseAPI.undisguiseToAll(player);
                }
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
    }
}
