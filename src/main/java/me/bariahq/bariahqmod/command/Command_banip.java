package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.banning.Ban;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH)
@CommandParameters(description = "Ban an ip", usage = "/<command> <ip> [reason]")
public class Command_banip extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        String ip = args[0];
        if (ip.split("\\.").length != 4)
        {
            msg("That is not a valid ip!", ChatColor.RED);
            return true;
        }
        String reason = null;
        if (args.length == 2)
        {
            reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }

        Ban ban = Ban.forPlayerIp(ip, sender, null, reason);
        plugin.bm.addBan(ban);

        final StringBuilder bcast = new StringBuilder()
                .append(ChatColor.RED)
                .append(sender.getName())
                .append(" - ")
                .append("Banning IP: ")
                .append(ip);
        if (reason != null)
        {
            bcast.append(" - Reason: ").append(ChatColor.YELLOW).append(reason);
        }
        if (args.length == 3)
        {
            if (args[0].equals("-pur"))
            {
                bcast.append(ChatColor.RED).append(" - PUR");
            }
        }
        FUtil.bcastMsg(bcast.toString());

        for (Player player : server.getOnlinePlayers())
        {
            if (player.getAddress().getAddress().getHostAddress().equals(ip))
            {
                player.kickPlayer(ban.bakeKickMessage());
            }
        }
        return true;
    }
}
