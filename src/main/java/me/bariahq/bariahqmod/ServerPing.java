package me.bariahq.bariahqmod;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPing extends FreedomService
{

    public ServerPing(BariaHQMod plugin)
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
    public void onServerPing(ServerListPingEvent event)
    {
        final String ip = event.getAddress().getHostAddress().trim();

        String lineone = ConfigEntry.MOTD_LINE_ONE.getString().replace("%mcversion%", plugin.si.getVersion());
        String linetwo = ConfigEntry.MOTD_LINE_TWO.getString().replace("%mcversion%", plugin.si.getVersion());
        String baseMotd = FUtil.colorize(lineone + "\n" + linetwo);

        if (!ConfigEntry.MOTD_COLORFUL_MOTD.getBoolean())
        {
            event.setMotd(baseMotd);
            return;
        }

        // Colorful MOTD
        final StringBuilder motd = new StringBuilder();
        for (String word : baseMotd.split(" "))
        {
            motd.append(FUtil.randomChatColor()).append(word).append(" ");
        }

        event.setMotd(motd.toString().trim());
    }

}
