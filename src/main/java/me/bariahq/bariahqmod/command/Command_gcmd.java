package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Send a command as someone else.", usage = "/<command> <fromname> <outcommand>", aliases = "sudo")
public class Command_gcmd extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 2)
        {
            return false;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        final String outCommand = StringUtils.join(args, " ", 1, args.length);

        if (plugin.cb.isCommandBlocked(outCommand, sender))
        {
            msg("The command you are trying to send is blocked");
            return true;
        }
        if (plugin.al.isStaffMember(player) && !ConfigEntry.SERVER_OWNERS.getStringList().contains(player.getName()) && !ConfigEntry.SERVER_FOUNDERS.getStringList().contains(player.getName()) && !FUtil.BHQDEVS.contains(player.getName()))
        {
            msg("You can not force a staff member to run a command");
            return true;
        }
        try
        {
            msg("Sending command as " + player.getName() + ": " + outCommand);
            if (server.dispatchCommand(player, outCommand))
            {
                msg("Command sent.");
            }
            else
            {
                msg("Unknown error sending command.");
            }
        }
        catch (Throwable ex)
        {
            msg("Error sending command: " + ex.getMessage());
        }

        return true;
    }
}
