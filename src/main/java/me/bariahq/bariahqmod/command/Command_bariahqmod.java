package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows information about BariaHQMod or reloads it", usage = "/<command> [reload]", aliases = "bhqm")
public class Command_bariahqmod extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 1)
        {
            if (!args[0].equals("reload"))
            {
                return false;
            }

            if (!plugin.al.isStaffMember(sender))
            {
                noPerms();
                return true;
            }

            plugin.config.load();
            plugin.services.stop();
            plugin.services.start();

            final String message = String.format("%s v%s reloaded.",
                    BariaHQMod.pluginName,
                    BariaHQMod.pluginVersion);

            msg(message);
            FLog.info(message);
            return true;
        }

        BariaHQMod.BuildProperties build = BariaHQMod.build;
        msg("BariaHQMod for 'BariaHQ', an all-op server.", ChatColor.GOLD);
        msg("Running on " + ConfigEntry.SERVER_NAME.getString() + ".", ChatColor.GOLD);
        msg("Created by ZeroEpoch1969", ChatColor.GOLD);
        msg(String.format("Version "
                        + ChatColor.BLUE + "%s - %s Build %s " + ChatColor.GOLD + "("
                        + ChatColor.BLUE + "%s" + ChatColor.GOLD + ")",
                build.codename,
                build.version,
                build.number,
                build.head), ChatColor.GOLD);
        msg(String.format("Compiled "
                        + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                        + ChatColor.BLUE + "%s",
                build.date,
                build.author), ChatColor.GOLD);
        msg("Visit " + ChatColor.AQUA + "http://github.com/BariaHQ/BariaHQMod"
                + ChatColor.GREEN + " for more information.", ChatColor.GREEN);

        return true;
    }
}