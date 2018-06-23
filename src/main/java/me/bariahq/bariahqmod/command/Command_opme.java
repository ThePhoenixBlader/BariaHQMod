package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Automatically ops user.", usage = "/<command>")
public class Command_opme extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!plugin.al.isStaffMember(sender) && plugin.da.isAdminDeopped(sender.getName()))
        {
            msg("You can not op yourself because you have been deopped by an administrator.", ChatColor.RED);
            return true;
        }
        FUtil.staffAction(sender.getName(), "Opping " + sender.getName(), false);
        sender.setOp(true);
        plugin.da.setAdminDeopped(sender.getName(), false);
        sender.sendMessage(FreedomCommand.YOU_ARE_OP);

        return true;
    }
}
