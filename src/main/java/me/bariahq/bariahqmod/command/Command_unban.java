package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.banning.Ban;
import me.bariahq.bariahqmod.player.PlayerData;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Unbans a player", usage = "/<command> <username>")
public class Command_unban extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 1)
        {
            String username;
            final List<String> ips = new ArrayList<>();
            final PlayerData entry = plugin.pl.getData(args[0]);

            if (entry == null)
            {
                msg("Can't find that user.");
                return true;
            }

            username = entry.getUsername();
            ips.addAll(entry.getIps());

            FUtil.staffAction(sender.getName(), "Unbanning " + username + " and IPs: " + StringUtils.join(ips, ", "), true);
            plugin.bm.removeBan(plugin.bm.getByUsername(username));

            for (String ip : ips)
            {
                Ban ban = plugin.bm.getByIp(ip);
                if (ban != null)
                {
                    plugin.bm.removeBan(ban);
                }
                ban = plugin.bm.getByIp(FUtil.getFuzzyIp(ip));
                if (ban != null)
                {
                    plugin.bm.removeBan(ban);
                }
            }
            return true;
        }

        return false;
    }
}
