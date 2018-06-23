package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.staff.StaffMember;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Unlink your discord account to your minecraft account", usage = "/<command>")
public class Command_unlinkdiscord extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!plugin.dc.enabled)
        {
            msg("The discord verification system is currently disabled", ChatColor.RED);
            return true;
        }

        StaffMember admin = plugin.al.getStaffMember(playerSender);
        if (admin.getDiscordID() == null)
        {
            msg("Your minecraft account is not linked to a discord account", ChatColor.RED);
            return true;
        }
        admin.setDiscordID(null);
        msg("Your minecraft account has been successfully unlinked from the discord account", ChatColor.GREEN);
        return true;
    }
}
