package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH)
@CommandParameters(description = "Sets everyone's Worldedit block modification limit to default or the number specified.", usage = "/<command> [amount]", aliases = "setl,swl")
public class Command_setlimit extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        int amount = 2500;
        if (args.length > 0)
        {
            try
            {
                amount = Math.max(1, Math.min(10000, Integer.parseInt(args[0])));
            }
            catch (NumberFormatException ex)
            {
                msg("Invalid number: " + args[0], ChatColor.RED);
                return true;
            }
        }
        FUtil.staffAction(sender.getName(), "Setting everyone's Worldedit block modification limit to " + amount + ".", true);
        for (final Player player : server.getOnlinePlayers())
        {
            plugin.web.setLimit(player, amount);
        }
        return true;
    }
}