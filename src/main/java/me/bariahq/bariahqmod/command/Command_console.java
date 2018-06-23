package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.IMPOSTOR, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Run console commands", usage = "/<command> <cmd>")
public class Command_console extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (!ConfigEntry.OVERLORD_IPS.getStringList().contains(playerSender.getAddress().getAddress().getHostAddress()))
        {
            if (!plugin.al.isSeniorAdmin(playerSender) && !FUtil.isManager(sender.getName()))
            {
                noPerms();
                return true;
            }
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.join(args, " "));
        msg("Command sent!", ChatColor.GREEN);
        return true;
    }
}
