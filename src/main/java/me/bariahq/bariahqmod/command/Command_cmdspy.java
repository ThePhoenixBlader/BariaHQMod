package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.staff.StaffMember;
import me.bariahq.bariahqmod.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Spy on commands", usage = "/<command>", aliases = "commandspy")
public class Command_cmdspy extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        StaffMember staffMember = plugin.al.getStaffMember(playerSender);
        staffMember.setCommandSpy(!staffMember.isCommandSpy());
        plugin.al.save();
        plugin.al.updateTables();
        msg("CommandSpy " + (staffMember.isCommandSpy() ? "enabled." : "disabled."));


        return true;
    }
}
