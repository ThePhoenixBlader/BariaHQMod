package me.bariahq.bariahqmod.bridge;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.staff.StaffMember;
import me.bariahq.bariahqmod.util.FLog;
import me.totalfreedom.bukkittelnet.BukkitTelnet;
import me.totalfreedom.bukkittelnet.api.TelnetCommandEvent;
import me.totalfreedom.bukkittelnet.api.TelnetPreLoginEvent;
import me.totalfreedom.bukkittelnet.api.TelnetRequestDataTagsEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.Map;

public class BukkitTelnetBridge extends FreedomService
{
    private BukkitTelnet bukkitTelnetPlugin = null;

    public BukkitTelnetBridge(BariaHQMod plugin)
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

    public int getTelnetSessionAmount()
    {
        return getBukkitTelnetPlugin().appender.getSessions().size();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTelnetPreLogin(TelnetPreLoginEvent event)
    {

        final String ip = event.getIp();
        if (ip == null || ip.isEmpty())
        {
            return;
        }

        final StaffMember staffMember = plugin.al.getEntryByIpFuzzy(ip);

        if (staffMember == null || !staffMember.isActive() || !staffMember.getRank().hasConsoleVariant())
        {
            return;
        }

        event.setBypassPassword(true);
        event.setName(staffMember.getName());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTelnetCommand(TelnetCommandEvent event)
    {
        if (plugin.cb.isCommandBlocked(event.getCommand(), event.getSender()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTelnetRequestDataTags(TelnetRequestDataTagsEvent event)
    {
        final Iterator<Map.Entry<Player, Map<String, Object>>> it = event.getDataTags().entrySet().iterator();
        while (it.hasNext())
        {
            final Map.Entry<Player, Map<String, Object>> entry = it.next();
            final Player player = entry.getKey();
            final Map<String, Object> playerTags = entry.getValue();

            boolean isAdmin = false;
            boolean isAdministrator = false;
            boolean isSeniorAdmin = false;

            final StaffMember staffMember = plugin.al.getStaffMember(player);
            if (staffMember != null)
            {
                boolean active = staffMember.isActive();

                isAdmin = active;
                isSeniorAdmin = active && staffMember.getRank() == Rank.SENIOR_ADMIN;
                isAdministrator = active && (isSeniorAdmin || staffMember.getRank() == Rank.ADMIN);
            }

            playerTags.put("tfm.admin.isAdmin", isAdmin);
            playerTags.put("tfm.admin.isTelnetAdmin", isAdministrator);
            playerTags.put("tfm.admin.isSeniorAdmin", isSeniorAdmin);

            playerTags.put("tfm.playerdata.getTag", plugin.pl.getPlayer(player).getTag());

            playerTags.put("tfm.essentialsBridge.getNickname", plugin.esb.getNickname(player.getName()));
        }
    }

    public BukkitTelnet getBukkitTelnetPlugin()
    {
        if (bukkitTelnetPlugin == null)
        {
            try
            {
                final Plugin bukkitTelnet = Bukkit.getServer().getPluginManager().getPlugin("BukkitTelnet");
                if (bukkitTelnet != null)
                {
                    if (bukkitTelnet instanceof BukkitTelnet)
                    {
                        bukkitTelnetPlugin = (BukkitTelnet) bukkitTelnet;
                    }
                }
            }
            catch (Exception ex)
            {
                FLog.severe(ex);
            }
        }
        return bukkitTelnetPlugin;
    }

}
